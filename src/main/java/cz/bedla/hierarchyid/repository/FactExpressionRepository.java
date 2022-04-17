package cz.bedla.hierarchyid.repository;

import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;
import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.db.fact.FactDefinitionToExpressionConvertor;
import cz.bedla.hierarchyid.db.fact.FactExpressionDefinition;
import cz.bedla.hierarchyid.db.fact.FactExpressionToDefinitionConverter;
import cz.bedla.hierarchyid.expression.Expression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.math.NumberUtils.createInteger;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class FactExpressionRepository {
    private static final String DT_STR_PREFIX = "str:";
    private static final String DT_INT_PREFIX = "int:";

    private final FactDefinitionToExpressionConvertor definitionToExpressionConvertor;
    private final FactExpressionToDefinitionConverter expressionToDefinitionConvertor;
    private final JdbcTemplate jdbcTemplate;

    public FactExpressionRepository(
            FactDefinitionToExpressionConvertor definitionToExpressionConvertor,
            FactExpressionToDefinitionConverter expressionToDefinitionConvertor,
            JdbcTemplate jdbcTemplate
    ) {
        this.definitionToExpressionConvertor = definitionToExpressionConvertor;
        this.expressionToDefinitionConvertor = expressionToDefinitionConvertor;
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<Expression> getExpression(int parentId) {
        var list = jdbcTemplate.query("""
                        SELECT CAST(HIERARCHY AS VARCHAR(128)) AS HIERARCHY, TYPE, LOGICAL_OPERATOR, 
                               VAL_COLUMN_NAME, VAL_OPERATOR, VAL_VALUE
                        FROM foo.EXPRESSION_FACT_ITEM 
                        WHERE PARENT_ID = ?
                        """,
                (rs, rowNum) -> {
                    var type = ExpressionDefinitionType.valueOf(rs.getString("TYPE"));
                    return new FactExpressionDefinition(
                            rs.getString("HIERARCHY"),
                            type,
                            type == ExpressionDefinitionType.LOGICAL_OPERATOR ? LogicalOperator.valueOf(rs.getString("LOGICAL_OPERATOR")) : null,
                            type == ExpressionDefinitionType.VALUE ? createFact(rs) : null
                    );
                }, parentId);

        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(definitionToExpressionConvertor.convert(list));
        }
    }

    public int insertExpression(Expression expression) {
        // simulate richer DB model
        var newParentId = findMaxParentId() + 1;
        insertExpression(expression, newParentId);
        return newParentId;
    }

    public void insertExpression(Expression expression, int parentId) {
        var list = expressionToDefinitionConvertor.convert(expression);
        var valuesList = list.stream()
                .map(it -> {
                    var logicalOperatorStr = it.getType() == ExpressionDefinitionType.LOGICAL_OPERATOR ? it.getLogicalOperator() : null;
                    var fact = it.getValue();
                    return new Object[]{
                            parentId, it.getHierarchyId(), it.getType().name(), logicalOperatorStr,
                            fact != null ? fact.columnName() : null,
                            fact != null ? fact.operator().name() : null,
                            fact != null ? createValueStr(fact.value()) : null
                    };
                })
                .toList();

        jdbcTemplate.batchUpdate("""
                        INSERT INTO foo.EXPRESSION_FACT_ITEM (PARENT_ID, HIERARCHY, TYPE, LOGICAL_OPERATOR,
                                                              VAL_COLUMN_NAME, VAL_OPERATOR, VAL_VALUE) 
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """,
                valuesList,
                new int[]{
                        Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
    }

    public boolean updateExpression(Expression expression, int parentId) {
        var updatedCount = jdbcTemplate.update("DELETE FROM foo.EXPRESSION_FACT_ITEM WHERE PARENT_ID = ?", parentId);
        if (updatedCount == 0) {
            return false;
        }

        insertExpression(expression, parentId);

        return true;
    }

    private int findMaxParentId() {
        return jdbcTemplate.queryForObject("SELECT COALESCE(MAX(PARENT_ID), 0) FROM foo.EXPRESSION_FACT_ITEM", Integer.class);
    }

    private String createValueStr(Object value) {
        if (value instanceof Number n) {
            return DT_INT_PREFIX + n;
        } else if (value instanceof String s) {
            return DT_STR_PREFIX + s;
        } else {
            throw new IllegalStateException("Unsupported data type: " + value);
        }
    }

    private Fact createFact(ResultSet rs) throws SQLException {
        return new Fact(
                rs.getString("VAL_COLUMN_NAME"),
                Fact.Operator.valueOf(rs.getString("VAL_OPERATOR")),
                createValue(rs.getString("VAL_VALUE")));
    }

    private Object createValue(String value) {
        if (startsWith(value, DT_STR_PREFIX)) {
            return removeStart(value, DT_STR_PREFIX);
        } else if (startsWith(value, DT_INT_PREFIX)) {
            return createInteger(removeStart(value, DT_INT_PREFIX));
        } else {
            throw new IllegalStateException("Unsupported value prefix: " + value);
        }
    }
}

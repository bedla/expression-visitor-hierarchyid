package cz.bedla.hierarchyid.repository;

import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;
import cz.bedla.hierarchyid.db.id.IdDefinitionToExpressionConvertor;
import cz.bedla.hierarchyid.db.id.IdExpressionDefinition;
import cz.bedla.hierarchyid.db.id.IdExpressionToDefinitionConverter;
import cz.bedla.hierarchyid.expression.Expression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class IdExpressionRepository {
    private final IdDefinitionToExpressionConvertor definitionToExpressionConvertor;
    private final IdExpressionToDefinitionConverter expressionToDefinitionConvertor;
    private final JdbcTemplate jdbcTemplate;

    public IdExpressionRepository(
            IdDefinitionToExpressionConvertor definitionToExpressionConvertor,
            IdExpressionToDefinitionConverter expressionToDefinitionConvertor,
            JdbcTemplate jdbcTemplate
    ) {
        this.definitionToExpressionConvertor = definitionToExpressionConvertor;
        this.expressionToDefinitionConvertor = expressionToDefinitionConvertor;
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<Expression> getExpression(int parentId) {
        var list = jdbcTemplate.query("""
                        SELECT CAST(HIERARCHY AS VARCHAR(128)) AS HIERARCHY, TYPE, LOGICAL_OPERATOR, 
                               VAL_ID
                        FROM foo.EXPRESSION_ID_ITEM 
                        WHERE PARENT_ID = ?
                        """,
                (rs, rowNum) -> {
                    var type = ExpressionDefinitionType.valueOf(rs.getString("TYPE"));
                    return new IdExpressionDefinition(
                            rs.getString("HIERARCHY"),
                            type,
                            type == ExpressionDefinitionType.LOGICAL_OPERATOR ? LogicalOperator.valueOf(rs.getString("LOGICAL_OPERATOR")) : null,
                            type == ExpressionDefinitionType.VALUE ? createId(rs) : null
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
                    var id = it.getValue();
                    return new Object[]{
                            parentId, it.getHierarchyId(), it.getType().name(), logicalOperatorStr,
                            id
                    };
                })
                .toList();

        jdbcTemplate.batchUpdate("""
                        INSERT INTO foo.EXPRESSION_ID_ITEM (PARENT_ID, HIERARCHY, TYPE, LOGICAL_OPERATOR,
                                                            VAL_ID) 
                        VALUES (?, ?, ?, ?, ?)
                        """,
                valuesList,
                new int[]{
                        Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.INTEGER});
    }

    public boolean updateExpression(Expression expression, int parentId) {
        var updatedCount = jdbcTemplate.update("DELETE FROM foo.EXPRESSION_ID_ITEM WHERE PARENT_ID = ?", parentId);
        if (updatedCount == 0) {
            return false;
        }

        insertExpression(expression, parentId);

        return true;
    }

    private int findMaxParentId() {
        return jdbcTemplate.queryForObject("SELECT COALESCE(MAX(PARENT_ID), 0) FROM foo.EXPRESSION_ID_ITEM", Integer.class);
    }

    private int createId(ResultSet rs) throws SQLException {
        return rs.getInt("VAL_ID");
    }
}

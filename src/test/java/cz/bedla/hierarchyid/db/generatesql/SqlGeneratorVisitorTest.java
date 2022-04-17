package cz.bedla.hierarchyid.db.generatesql;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.fact.FactExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SqlGeneratorVisitorTest {
    private final SqlGeneratorVisitor visitor = new SqlGeneratorVisitor();

    @ParameterizedTest
    @MethodSource("generateComplexProvider")
    void generateComplex(Expression expression, String expectedWhere) {
        var where = visitor.visit(expression);
        assertThat(where.toString())
                .isEqualTo(expectedWhere);
    }

    static Stream<Arguments> generateComplexProvider() {
        return Stream.of(
                Arguments.of(
                        new NotExpression(
                                new FactExpression(new Fact("col1", Fact.Operator.EQ, 111))
                        ), "NOT (col1 = 111)"),
                Arguments.of(
                        new AndExpression(
                                new FactExpression(new Fact("col1", Fact.Operator.EQ, 111)),
                                new FactExpression(new Fact("col2", Fact.Operator.EQ, 222))
                        ), "(col1 = 111 AND col2 = 222)"),
                Arguments.of(
                        new OrExpression(
                                new FactExpression(new Fact("col1", Fact.Operator.EQ, 111)),
                                new FactExpression(new Fact("col2", Fact.Operator.EQ, 222))
                        ), "(col1 = 111 OR col2 = 222)"),
                Arguments.of(
                        new AndExpression(
                                new FactExpression(new Fact("col1", Fact.Operator.EQ, 111)),
                                new NotExpression(
                                        new OrExpression(
                                                new FactExpression(new Fact("col2", Fact.Operator.EQ, 222)),
                                                new FactExpression(new Fact("col3", Fact.Operator.EQ, 333))
                                        )
                                )
                        ), "(col1 = 111 AND NOT ((col2 = 222 OR col3 = 333)))"),
                Arguments.of(
                        new OrExpression(
                                new AndExpression(
                                        new FactExpression(new Fact("col1", Fact.Operator.EQ, 111)),
                                        new FactExpression(new Fact("col2", Fact.Operator.EQ, 222))
                                ),
                                new NotExpression(
                                        new AndExpression(
                                                new FactExpression(new Fact("col3", Fact.Operator.EQ, 333)),
                                                new FactExpression(new Fact("col4", Fact.Operator.EQ, 444))
                                        )
                                )
                        ), "((col1 = 111 AND col2 = 222) OR NOT ((col3 = 333 AND col4 = 444)))")
        );
    }

    @ParameterizedTest
    @MethodSource("generateOperatorsProvider")
    void generateOperators(Expression expression, String expectedWhere) {
        var where = visitor.visit(expression);
        assertThat(where.toString())
                .isEqualTo(expectedWhere);
    }

    static Stream<Arguments> generateOperatorsProvider() {
        return Stream.of(
                Arguments.of(
                        new FactExpression(new Fact("col1", Fact.Operator.EQ, 111)),
                        "col1 = 111"),
                Arguments.of(
                        new FactExpression(new Fact("col2", Fact.Operator.NOT_EQ, 222)),
                        "col2 <> 222"),
                Arguments.of(
                        new FactExpression(new Fact("col3", Fact.Operator.LT, 333)),
                        "col3 < 333"),
                Arguments.of(
                        new FactExpression(new Fact("col4", Fact.Operator.LTE, 444)),
                        "col4 <= 444"),
                Arguments.of(
                        new FactExpression(new Fact("col5", Fact.Operator.GT, 555)),
                        "col5 > 555"),
                Arguments.of(
                        new FactExpression(new Fact("col6", Fact.Operator.GTE, 666)),
                        "col6 >= 666")
        );
    }

    @ParameterizedTest
    @MethodSource("generateDataTypesProvider")
    void generateDataTypes(Expression expression, String expectedWhere) {
        var where = visitor.visit(expression);
        assertThat(where.toString())
                .isEqualTo(expectedWhere);
    }

    static Stream<Arguments> generateDataTypesProvider() {
        return Stream.of(
                Arguments.of(
                        new FactExpression(new Fact("col1", Fact.Operator.EQ, 111)),
                        "col1 = 111"),
                Arguments.of(
                        new FactExpression(new Fact("col2", Fact.Operator.NOT_EQ, "xxx")),
                        "col2 <> 'xxx'"),
                Arguments.of(
                        new FactExpression(new Fact("col3", Fact.Operator.LT, null)),
                        "col3 < NULL")
        );
    }

    @Test
    void invalidDataType() {
        assertThatThrownBy(() ->
                visitor.visit(new FactExpression(new Fact("col1", Fact.Operator.EQ, LocalDateTime.now()))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unsupported value type: ");
    }
}

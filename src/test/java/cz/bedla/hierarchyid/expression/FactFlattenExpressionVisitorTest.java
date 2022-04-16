package cz.bedla.hierarchyid.expression;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.fact.FactExpression;
import cz.bedla.hierarchyid.expression.fact.FactFlattenExpressionVisitor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FactFlattenExpressionVisitorTest {
    private final FactFlattenExpressionVisitor visitor = new FactFlattenExpressionVisitor();

    @Test
    void flatten() {
        var expr1 = new FactExpression(new Fact("x1", Fact.Operator.EQ, 123));
        var expr2 = new FactExpression(new Fact("x2", Fact.Operator.GT, 456));
        var expr3 = new FactExpression(new Fact("x3", Fact.Operator.NOT_EQ, 789));
        var expression = new OrExpression(List.of(
                new AndExpression(List.of(expr1, expr2, expr3)),
                new NotExpression(expr3)
        ));

        visitor.visit(expression);

        var flatten = visitor.getFlatten();
        assertThat(flatten)
                .containsExactly("[X1]", "AND", "[X2]", "AND", "[X3]", "OR", "NOT", "[X3]");

        var factIndex = visitor.getIndex();
        assertThat(factIndex)
                .hasSize(3)
                .containsEntry(expr1.getValue(), "X1")
                .containsEntry(expr2.getValue(), "X2")
                .containsEntry(expr3.getValue(), "X3");
    }
}

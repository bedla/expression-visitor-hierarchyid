package cz.bedla.hierarchyid.expression.visitor;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.VariableExpression;
import cz.bedla.hierarchyid.expression.fact.FactExpression;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FlattenExpressionVisitorTest {
    private final FlattenExpressionVisitor visitor = new FlattenExpressionVisitor();

    @Test
    void single() {
        visitor.visit(new VariableExpression("xxx"));
        var flatten = visitor.getFlatten();
        assertThat(flatten)
                .containsExactly("[X1]");

        var factIndex = visitor.getIndex();
        assertThat(factIndex)
                .hasSize(1)
                .containsEntry("xxx", "X1");
    }

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

        var index = visitor.getIndex();
        assertThat(index)
                .hasSize(3)
                .containsEntry(expr1.getValue(), "X1")
                .containsEntry(expr2.getValue(), "X2")
                .containsEntry(expr3.getValue(), "X3");
    }
}

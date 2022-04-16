package cz.bedla.hierarchyid.expression;

import java.util.ArrayList;
import java.util.List;

public class FlattenVisitor implements ExpressionVisitor<Void, String> {
    private final List<String> flatten = new ArrayList<>();

    public List<String> getFlatten() {
        return flatten;
    }

    @Override
    public Void visit(TerminalExpression<String> expression) {
        flatten.add(expression.getValue());
        return null;
    }

    @Override
    public Void visit(AndExpression expression) {
        var expressions = expression.getExpressions();
        for (int i = 0; i < expressions.size(); i++) {
            if (i >= 1) {
                flatten.add("AND");
            }
            Expression expr = expressions.get(i);
            expr.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(OrExpression expression) {
        var expressions = expression.getExpressions();
        for (int i = 0; i < expressions.size(); i++) {
            if (i >= 1) {
                flatten.add("OR");
            }
            Expression expr = expressions.get(i);
            expr.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(NotExpression expression) {
        flatten.add("NOT");
        expression.getExpression().accept(this);
        return null;
    }
}

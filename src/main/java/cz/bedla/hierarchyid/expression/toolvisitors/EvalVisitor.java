package cz.bedla.hierarchyid.expression.toolvisitors;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

public class EvalVisitor implements ExpressionVisitor<Boolean, Boolean> {
    @Override
    public Boolean visit(TerminalExpression<Boolean> expression) {
        return expression.getValue();
    }

    @Override
    public Boolean visit(AndExpression expression) {
        return expression.getExpressions().stream()
                .allMatch(it -> it.accept(this));
    }

    @Override
    public Boolean visit(OrExpression expression) {
        return expression.getExpressions().stream()
                .anyMatch(it -> it.accept(this));
    }

    @Override
    public Boolean visit(NotExpression expression) {
        var result = expression.getExpression().accept(this);
        return !result;
    }
}

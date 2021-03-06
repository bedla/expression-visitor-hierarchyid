package cz.bedla.hierarchyid.expression.samplevisitors;

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
        return expression.getLeftExpression().accept(this) && expression.getRightExpression().accept(this);
    }

    @Override
    public Boolean visit(OrExpression expression) {
        return expression.getLeftExpression().accept(this) || expression.getRightExpression().accept(this);
    }

    @Override
    public Boolean visit(NotExpression expression) {
        var result = expression.getExpression().accept(this);
        return !result;
    }
}

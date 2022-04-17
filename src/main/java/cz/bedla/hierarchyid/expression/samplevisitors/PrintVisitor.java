package cz.bedla.hierarchyid.expression.samplevisitors;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

public class PrintVisitor implements ExpressionVisitor<String, Object> {
    @Override
    public String visit(TerminalExpression<Object> expression) {
        return String.valueOf(expression.getValue());
    }

    @Override
    public String visit(AndExpression expression) {
        var left = expression.getLeftExpression().accept(this);
        var right = expression.getRightExpression().accept(this);
        return "(" + left + " AND " + right + ")";
    }

    @Override
    public String visit(OrExpression expression) {
        var left = expression.getLeftExpression().accept(this);
        var right = expression.getRightExpression().accept(this);
        return "(" + left + " OR " + right + ")";
    }

    @Override
    public String visit(NotExpression expression) {
        return "!" + expression.getExpression().accept(this);
    }
}

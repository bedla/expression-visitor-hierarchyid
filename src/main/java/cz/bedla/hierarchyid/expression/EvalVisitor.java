package cz.bedla.hierarchyid.expression;

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

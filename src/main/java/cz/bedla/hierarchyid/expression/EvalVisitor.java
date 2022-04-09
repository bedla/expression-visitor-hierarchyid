package cz.bedla.hierarchyid.expression;

class EvalVisitor implements ExpressionVisitor<Boolean> {
    @Override
    public Boolean visit(BooleanExpression expression) {
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
        return !expression.getExpression().accept(this);
    }
}

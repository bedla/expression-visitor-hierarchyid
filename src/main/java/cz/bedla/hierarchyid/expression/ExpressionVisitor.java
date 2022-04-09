package cz.bedla.hierarchyid.expression;

public interface ExpressionVisitor<T> {
    T visit(BooleanExpression expression);

    T visit(AndExpression expression);

    T visit(OrExpression expression);

    T visit(NotExpression expression);

    default T visit(Expression expression) {
        if (expression instanceof BooleanExpression) {
            return visit((BooleanExpression) expression);
        } else if (expression instanceof AndExpression) {
            return visit((AndExpression) expression);
        } else if (expression instanceof OrExpression) {
            return visit((OrExpression) expression);
        } else if (expression instanceof NotExpression) {
            return visit((NotExpression) expression);
        } else {
            throw new IllegalStateException("Unsupported expression: " + expression);
        }
    }
}

package cz.bedla.hierarchyid.expression;

public interface ExpressionVisitor<R, TERM_V> {
    R visit(TerminalExpression<TERM_V> expression);

    R visit(AndExpression expression);

    R visit(OrExpression expression);

    R visit(NotExpression expression);

    default R visit(Expression expression) {
        if (expression instanceof TerminalExpression) {
            return visit((TerminalExpression<TERM_V>) expression);
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

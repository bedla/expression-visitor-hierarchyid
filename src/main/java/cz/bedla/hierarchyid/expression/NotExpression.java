package cz.bedla.hierarchyid.expression;

import java.util.Objects;

public class NotExpression implements Expression {
    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R, TERM_V> R accept(ExpressionVisitor<R, TERM_V> visitor) {
        return visitor.visit(this);
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "(NOT " + expression + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotExpression that = (NotExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}

package cz.bedla.hierarchyid.expression;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

public class OrExpression implements Expression {
    private final Expression leftExpression;
    private final Expression rightExpression;

    public OrExpression(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = notNull(leftExpression, "leftExpression cannot be null");
        this.rightExpression = notNull(rightExpression, "rightExpression cannot be null");
    }

    @Override
    public <R, TERM_V> R accept(ExpressionVisitor<R, TERM_V> visitor) {
        return visitor.visit(this);
    }


    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    @Override
    public String toString() {
        return "(OR " + leftExpression + " " + rightExpression + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrExpression that = (OrExpression) o;
        return Objects.equals(leftExpression, that.leftExpression) && Objects.equals(rightExpression, that.rightExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftExpression, rightExpression);
    }
}

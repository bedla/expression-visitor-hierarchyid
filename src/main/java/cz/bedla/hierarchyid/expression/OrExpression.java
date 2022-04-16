package cz.bedla.hierarchyid.expression;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

public class OrExpression implements Expression {
    private final List<Expression> expressions;

    public OrExpression(Expression... expressions) {
        this(List.of(expressions));
    }

    public OrExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public <R, TERM_V> R accept(ExpressionVisitor<R, TERM_V> visitor) {
        return visitor.visit(this);
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public String toString() {
        return "(OR " + expressions.stream()
                .map(Object::toString)
                .collect(joining(" ", "", "")) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrExpression that = (OrExpression) o;
        return Objects.equals(expressions, that.expressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions);
    }
}

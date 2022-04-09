package cz.bedla.hierarchyid.expression;

import java.util.List;

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
    public <T> T accept(ExpressionVisitor<T> visitor) {
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
}

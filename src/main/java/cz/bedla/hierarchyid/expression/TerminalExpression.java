package cz.bedla.hierarchyid.expression;

public abstract class TerminalExpression<V> implements Expression {
    private final V value;

    public TerminalExpression(V value) {
        this.value = value;
    }

    @Override
    public <R, TERM_V> R accept(ExpressionVisitor<R, TERM_V> visitor) {
        return visitor.visit(this);
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Term(" + value + ")";
    }
}

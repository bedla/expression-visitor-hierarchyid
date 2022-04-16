package cz.bedla.hierarchyid.expression;

public interface Expression {
    <R, TERM_V> R accept(ExpressionVisitor<R, TERM_V> visitor);
}

package cz.bedla.hierarchyid.expression;

public interface Expression {
    <T> T accept(ExpressionVisitor<T> visitor);
}

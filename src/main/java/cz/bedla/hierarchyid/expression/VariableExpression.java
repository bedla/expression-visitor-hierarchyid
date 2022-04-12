package cz.bedla.hierarchyid.expression;

public class VariableExpression implements Expression {
    private final String variableName;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String toString() {
        return variableName;
    }
}

package cz.bedla.hierarchyid.expression;

public class VariableExpression extends TerminalExpression<String> {
    public VariableExpression(String variableName) {
        super(variableName);
    }

    @Override
    public String toString() {
        return getValue();
    }
}

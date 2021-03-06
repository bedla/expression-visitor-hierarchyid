package cz.bedla.hierarchyid.expression;

import java.util.Objects;

public class VariableExpression extends TerminalExpression<String> {
    public VariableExpression(String variableName) {
        super(variableName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableExpression that = (VariableExpression) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return getValue();
    }
}

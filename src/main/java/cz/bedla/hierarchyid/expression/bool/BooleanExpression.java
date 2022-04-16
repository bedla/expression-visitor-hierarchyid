package cz.bedla.hierarchyid.expression.bool;

import cz.bedla.hierarchyid.expression.TerminalExpression;

import java.util.Objects;

public class BooleanExpression extends TerminalExpression<Boolean> {
    public BooleanExpression(boolean bool) {
        super(bool);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanExpression that = (BooleanExpression) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}

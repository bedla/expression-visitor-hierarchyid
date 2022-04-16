package cz.bedla.hierarchyid.expression.id;

import cz.bedla.hierarchyid.expression.TerminalExpression;

import java.util.Objects;

public class IdExpression extends TerminalExpression<Integer> {
    public IdExpression(int id) {
        super(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdExpression that = (IdExpression) o;
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

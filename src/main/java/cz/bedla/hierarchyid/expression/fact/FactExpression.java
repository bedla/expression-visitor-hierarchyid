package cz.bedla.hierarchyid.expression.fact;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.TerminalExpression;

import java.util.Objects;

public class FactExpression extends TerminalExpression<Fact> {
    public FactExpression(Fact fact) {
        super(fact);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactExpression that = (FactExpression) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "FactExpression{" +
                "value=" + getValue() +
                '}';
    }
}

package cz.bedla.hierarchyid.expression;

import cz.bedla.hierarchyid.db.Fact;

public class FactExpression extends TerminalExpression<Fact> {
    public FactExpression(Fact fact) {
        super(fact);
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}

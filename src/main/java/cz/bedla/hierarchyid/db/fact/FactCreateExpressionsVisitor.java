package cz.bedla.hierarchyid.db.fact;

import cz.bedla.hierarchyid.db.CreateExpressionsVisitor;
import cz.bedla.hierarchyid.expression.fact.FactExpression;

public class FactCreateExpressionsVisitor extends CreateExpressionsVisitor<Fact, FactExpressionDefinition, FactExpressionDefinitionNode> {
    @Override
    protected FactExpression createTerminalExpression(Fact value) {
        return new FactExpression(value);
    }
}

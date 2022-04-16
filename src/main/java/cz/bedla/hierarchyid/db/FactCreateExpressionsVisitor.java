package cz.bedla.hierarchyid.db;

import cz.bedla.hierarchyid.expression.FactExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

public class FactCreateExpressionsVisitor extends CreateExpressionsVisitor<Fact, FactExpressionDefinition, FactExpressionDefinitionNode> {
    @Override
    protected TerminalExpression<Fact> createTerminalExpression(Fact value) {
        return new FactExpression(value);
    }
}

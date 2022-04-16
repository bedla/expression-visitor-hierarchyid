package cz.bedla.hierarchyid.db.fact;

import cz.bedla.hierarchyid.db.DefinitionToExpressionConvertor;

public class FactDefinitionToExpressionConvertor extends DefinitionToExpressionConvertor<Fact, FactExpressionDefinition, FactExpressionDefinitionNode> {
    @Override
    protected FactExpressionDefinitionNode createNode(int id, FactExpressionDefinition expressionDefinition) {
        return new FactExpressionDefinitionNode(id, expressionDefinition);
    }

    @Override
    protected FactCreateExpressionsVisitor createCreateExpressionVisitor() {
        return new FactCreateExpressionsVisitor();
    }
}

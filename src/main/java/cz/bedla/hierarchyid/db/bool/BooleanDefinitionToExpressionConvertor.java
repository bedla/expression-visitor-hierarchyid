package cz.bedla.hierarchyid.db.bool;

import cz.bedla.hierarchyid.db.DefinitionToExpressionConvertor;

public class BooleanDefinitionToExpressionConvertor extends DefinitionToExpressionConvertor<Boolean, BooleanExpressionDefinition, BooleanExpressionDefinitionNode> {
    @Override
    protected BooleanExpressionDefinitionNode createNode(int id, BooleanExpressionDefinition expressionDefinition) {
        return new BooleanExpressionDefinitionNode(id, expressionDefinition);
    }

    @Override
    protected BooleanCreateExpressionsVisitor createCreateExpressionVisitor() {
        return new BooleanCreateExpressionsVisitor();
    }
}

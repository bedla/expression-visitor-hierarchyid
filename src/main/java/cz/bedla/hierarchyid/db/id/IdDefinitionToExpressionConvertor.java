package cz.bedla.hierarchyid.db.id;

import cz.bedla.hierarchyid.db.DefinitionToExpressionConvertor;
import cz.bedla.hierarchyid.db.fact.Fact;

public class IdDefinitionToExpressionConvertor extends DefinitionToExpressionConvertor<Integer, IdExpressionDefinition, IdExpressionDefinitionNode> {
    @Override
    protected IdExpressionDefinitionNode createNode(int id, IdExpressionDefinition expressionDefinition) {
        return new IdExpressionDefinitionNode(id, expressionDefinition);
    }

    @Override
    protected IdCreateExpressionsVisitor createCreateExpressionVisitor() {
        return new IdCreateExpressionsVisitor();
    }
}

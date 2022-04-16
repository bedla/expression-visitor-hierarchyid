package cz.bedla.hierarchyid.db.fact;

import cz.bedla.hierarchyid.db.ExpressionDefinitionNode;

class FactExpressionDefinitionNode extends ExpressionDefinitionNode<Fact, FactExpressionDefinition, FactExpressionDefinitionNode> {
    FactExpressionDefinitionNode(int id, FactExpressionDefinition expressionDefinition) {
        super(id, expressionDefinition);
    }
}

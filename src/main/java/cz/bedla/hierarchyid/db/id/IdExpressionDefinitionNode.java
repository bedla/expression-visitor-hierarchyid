package cz.bedla.hierarchyid.db.id;

import cz.bedla.hierarchyid.db.ExpressionDefinitionNode;
import cz.bedla.hierarchyid.db.fact.Fact;

class IdExpressionDefinitionNode extends ExpressionDefinitionNode<Integer, IdExpressionDefinition, IdExpressionDefinitionNode> {
    IdExpressionDefinitionNode(int id, IdExpressionDefinition expressionDefinition) {
        super(id, expressionDefinition);
    }
}

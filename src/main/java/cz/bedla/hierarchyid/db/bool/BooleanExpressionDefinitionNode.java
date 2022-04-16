package cz.bedla.hierarchyid.db.bool;

import cz.bedla.hierarchyid.db.ExpressionDefinitionNode;

class BooleanExpressionDefinitionNode extends ExpressionDefinitionNode<Boolean, BooleanExpressionDefinition, BooleanExpressionDefinitionNode> {
    BooleanExpressionDefinitionNode(int id, BooleanExpressionDefinition expressionDefinition) {
        super(id, expressionDefinition);
    }
}

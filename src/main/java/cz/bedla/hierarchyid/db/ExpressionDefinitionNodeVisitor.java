package cz.bedla.hierarchyid.db;

public interface ExpressionDefinitionNodeVisitor<VAL, ED extends ExpressionDefinition<VAL, ED>, NODE extends ExpressionDefinitionNode<VAL, ED, NODE>, RES> {
    RES visit(NODE node);
}

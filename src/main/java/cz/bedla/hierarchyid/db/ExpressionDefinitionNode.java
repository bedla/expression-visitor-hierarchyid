package cz.bedla.hierarchyid.db;

import java.util.ArrayList;
import java.util.List;

class ExpressionDefinitionNode {
    private final int id;
    private final ExpressionDefinition expressionDefinition;
    private final List<ExpressionDefinitionNode> children;

    ExpressionDefinitionNode(int id, ExpressionDefinition expressionDefinition) {
        this.id = id;
        this.expressionDefinition = expressionDefinition;
        this.children = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public ExpressionDefinition getExpressionDefinition() {
        return expressionDefinition;
    }

    public List<ExpressionDefinitionNode> getChildren() {
        return children;
    }

    public <T> T accept(ExpressionDefinitionNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", expressionDefinition=" + expressionDefinition +
                '}';
    }
}

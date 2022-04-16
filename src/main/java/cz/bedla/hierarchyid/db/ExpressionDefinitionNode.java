package cz.bedla.hierarchyid.db;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpressionDefinitionNode<VAL, ED extends ExpressionDefinition<VAL, ED>, SELF extends ExpressionDefinitionNode<VAL, ED, SELF>> {
    private final int id;
    private final ED expressionDefinition;
    private final List<SELF> children;

    public ExpressionDefinitionNode(int id, ED expressionDefinition) {
        this.id = id;
        this.expressionDefinition = expressionDefinition;
        this.children = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public ED getExpressionDefinition() {
        return expressionDefinition;
    }

    public List<SELF> getChildren() {
        return children;
    }

    public <RES> RES accept(ExpressionDefinitionNodeVisitor<VAL, ED, SELF, RES> visitor) {
        return visitor.visit(self());
    }

    private SELF self() {
        return (SELF) this;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", expressionDefinition=" + expressionDefinition +
                '}';
    }
}

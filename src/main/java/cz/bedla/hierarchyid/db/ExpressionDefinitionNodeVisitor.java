package cz.bedla.hierarchyid.db;

public interface ExpressionDefinitionNodeVisitor<T> {
    T visit(ExpressionDefinitionNode node);
}

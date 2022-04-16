package cz.bedla.hierarchyid.db;

public class FactCreateExpressionDefinitionVisitor extends CreateExpressionDefinitionVisitor<Fact, FactExpressionDefinition> {
    @Override
    protected FactExpressionDefinition createExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, Fact value) {
        return new FactExpressionDefinition(type, logicalOperator, value);
    }
}

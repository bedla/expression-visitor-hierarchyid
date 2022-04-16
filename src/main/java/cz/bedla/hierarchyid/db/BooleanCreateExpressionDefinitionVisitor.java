package cz.bedla.hierarchyid.db;

public class BooleanCreateExpressionDefinitionVisitor extends CreateExpressionDefinitionVisitor<Boolean, BooleanExpressionDefinition> {
    @Override
    protected BooleanExpressionDefinition createExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, Boolean value) {
        return new BooleanExpressionDefinition(type, logicalOperator, value);
    }
}

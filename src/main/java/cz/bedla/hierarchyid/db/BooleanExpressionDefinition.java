package cz.bedla.hierarchyid.db;

public class BooleanExpressionDefinition extends ExpressionDefinition<Boolean, BooleanExpressionDefinition> {
    public BooleanExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, Boolean value) {
        this("", type, logicalOperator, value);
    }

    public BooleanExpressionDefinition(String hierarchyId, ExpressionDefinitionType type, LogicalOperator logicalOperator, Boolean value) {
        super(hierarchyId, type, logicalOperator, value);
    }

    @Override
    protected BooleanExpressionDefinition create(String hierarchyId, ExpressionDefinitionType type, LogicalOperator logicalOperator, Boolean value) {
        return new BooleanExpressionDefinition(hierarchyId, type, logicalOperator, value);
    }
}

package cz.bedla.hierarchyid.db.bool;

import cz.bedla.hierarchyid.db.CreateExpressionDefinitionVisitor;
import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;

public class BooleanCreateExpressionDefinitionVisitor extends CreateExpressionDefinitionVisitor<Boolean, BooleanExpressionDefinition> {
    @Override
    protected BooleanExpressionDefinition createExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, Boolean value) {
        return new BooleanExpressionDefinition(type, logicalOperator, value);
    }
}

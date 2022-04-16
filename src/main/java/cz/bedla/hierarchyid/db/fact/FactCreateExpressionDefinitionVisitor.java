package cz.bedla.hierarchyid.db.fact;

import cz.bedla.hierarchyid.db.CreateExpressionDefinitionVisitor;
import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;

public class FactCreateExpressionDefinitionVisitor extends CreateExpressionDefinitionVisitor<Fact, FactExpressionDefinition> {
    @Override
    protected FactExpressionDefinition createExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, Fact value) {
        return new FactExpressionDefinition(type, logicalOperator, value);
    }
}

package cz.bedla.hierarchyid.db.id;

import cz.bedla.hierarchyid.db.CreateExpressionDefinitionVisitor;
import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;

public class IdCreateExpressionDefinitionVisitor extends CreateExpressionDefinitionVisitor<Integer, IdExpressionDefinition> {
    @Override
    protected IdExpressionDefinition createExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, Integer value) {
        return new IdExpressionDefinition(type, logicalOperator, value);
    }
}

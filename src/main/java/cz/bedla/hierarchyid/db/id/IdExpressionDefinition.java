package cz.bedla.hierarchyid.db.id;

import cz.bedla.hierarchyid.db.ExpressionDefinition;
import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;

public class IdExpressionDefinition extends ExpressionDefinition<Integer, IdExpressionDefinition> {
    public IdExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, Integer value) {
        this("", type, logicalOperator, value);
    }

    public IdExpressionDefinition(String hierarchyId, ExpressionDefinitionType type, LogicalOperator logicalOperator, Integer value) {
        super(hierarchyId, type, logicalOperator, value);
    }

    @Override
    protected IdExpressionDefinition create(String hierarchyId, ExpressionDefinitionType type, LogicalOperator logicalOperator, Integer value) {
        return new IdExpressionDefinition(hierarchyId, type, logicalOperator, value);
    }
}

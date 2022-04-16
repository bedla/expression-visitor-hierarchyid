package cz.bedla.hierarchyid.db.fact;

import cz.bedla.hierarchyid.db.ExpressionDefinition;
import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;

public class FactExpressionDefinition extends ExpressionDefinition<Fact, FactExpressionDefinition> {
    public FactExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, Fact value) {
        this("", type, logicalOperator, value);
    }

    public FactExpressionDefinition(String hierarchyId, ExpressionDefinitionType type, LogicalOperator logicalOperator, Fact value) {
        super(hierarchyId, type, logicalOperator, value);
    }

    @Override
    protected FactExpressionDefinition create(String hierarchyId, ExpressionDefinitionType type, LogicalOperator logicalOperator, Fact value) {
        return new FactExpressionDefinition(hierarchyId, type, logicalOperator, value);
    }
}

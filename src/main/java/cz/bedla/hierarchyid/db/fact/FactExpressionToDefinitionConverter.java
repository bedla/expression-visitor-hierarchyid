package cz.bedla.hierarchyid.db.fact;

import cz.bedla.hierarchyid.db.ExpressionToDefinitionConverter;

public class FactExpressionToDefinitionConverter extends ExpressionToDefinitionConverter<Fact, FactExpressionDefinition> {
    @Override
    protected FactCreateExpressionDefinitionVisitor createCreateExpressionDefinitionVisitor() {
        return new FactCreateExpressionDefinitionVisitor();
    }
}

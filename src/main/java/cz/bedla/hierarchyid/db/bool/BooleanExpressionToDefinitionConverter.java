package cz.bedla.hierarchyid.db.bool;

import cz.bedla.hierarchyid.db.ExpressionToDefinitionConverter;

public class BooleanExpressionToDefinitionConverter extends ExpressionToDefinitionConverter<Boolean, BooleanExpressionDefinition> {
    @Override
    protected BooleanCreateExpressionDefinitionVisitor createCreateExpressionDefinitionVisitor() {
        return new BooleanCreateExpressionDefinitionVisitor();
    }
}

package cz.bedla.hierarchyid.db;

public class BooleanExpressionToDefinitionConverter extends ExpressionToDefinitionConverter<Boolean, BooleanExpressionDefinition> {
    @Override
    protected BooleanCreateExpressionDefinitionVisitor createCreateExpressionDefinitionVisitor() {
        return new BooleanCreateExpressionDefinitionVisitor();
    }
}

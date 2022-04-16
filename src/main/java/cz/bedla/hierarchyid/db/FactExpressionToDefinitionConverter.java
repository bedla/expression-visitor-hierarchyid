package cz.bedla.hierarchyid.db;

public class FactExpressionToDefinitionConverter extends ExpressionToDefinitionConverter<Fact, FactExpressionDefinition> {
    @Override
    protected FactCreateExpressionDefinitionVisitor createCreateExpressionDefinitionVisitor() {
        return new FactCreateExpressionDefinitionVisitor();
    }
}

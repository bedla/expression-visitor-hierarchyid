package cz.bedla.hierarchyid.db.id;

import cz.bedla.hierarchyid.db.ExpressionToDefinitionConverter;

public class IdExpressionToDefinitionConverter extends ExpressionToDefinitionConverter<Integer, IdExpressionDefinition> {
    @Override
    protected IdCreateExpressionDefinitionVisitor createCreateExpressionDefinitionVisitor() {
        return new IdCreateExpressionDefinitionVisitor();
    }
}

package cz.bedla.hierarchyid.db.id;

import cz.bedla.hierarchyid.db.CreateExpressionsVisitor;
import cz.bedla.hierarchyid.expression.id.IdExpression;

public class IdCreateExpressionsVisitor extends CreateExpressionsVisitor<Integer, IdExpressionDefinition, IdExpressionDefinitionNode> {
    @Override
    protected IdExpression createTerminalExpression(Integer value) {
        return new IdExpression(value);
    }
}

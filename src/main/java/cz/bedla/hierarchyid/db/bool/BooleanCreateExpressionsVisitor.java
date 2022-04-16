package cz.bedla.hierarchyid.db.bool;

import cz.bedla.hierarchyid.db.CreateExpressionsVisitor;
import cz.bedla.hierarchyid.expression.bool.BooleanExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

public class BooleanCreateExpressionsVisitor extends CreateExpressionsVisitor<Boolean, BooleanExpressionDefinition, BooleanExpressionDefinitionNode> {
    @Override
    protected TerminalExpression<Boolean> createTerminalExpression(Boolean value) {
        return new BooleanExpression(value);
    }
}

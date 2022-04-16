package cz.bedla.hierarchyid.db;

import cz.bedla.hierarchyid.expression.BooleanExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

public class BooleanCreateExpressionsVisitor extends CreateExpressionsVisitor<Boolean, BooleanExpressionDefinition, BooleanExpressionDefinitionNode> {
    @Override
    protected TerminalExpression<Boolean> createTerminalExpression(Boolean value) {
        return new BooleanExpression(value);
    }
}

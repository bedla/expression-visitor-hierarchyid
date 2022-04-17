package cz.bedla.hierarchyid.db;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

public abstract class CreateExpressionsVisitor<VAL, ED extends ExpressionDefinition<VAL, ED>, NODE extends ExpressionDefinitionNode<VAL, ED, NODE>> implements ExpressionDefinitionNodeVisitor<VAL, ED, NODE, Expression> {

    protected abstract TerminalExpression<VAL> createTerminalExpression(VAL value);

    @Override
    public Expression visit(NODE node) {
        var expressionDefinition = node.getExpressionDefinition();
        if (expressionDefinition.getType() == ExpressionDefinitionType.VALUE) {
            return createTerminalExpression(expressionDefinition.getValue());
        } else if (expressionDefinition.getType() == ExpressionDefinitionType.LOGICAL_OPERATOR) {
            var children = node.getChildren();
            if (expressionDefinition.getLogicalOperator() == LogicalOperator.AND) {
                if (children.size() == 2) {
                    var left = visit(children.get(0));
                    var right = visit(children.get(1));
                    return new AndExpression(left, right);
                } else {
                    throw new IllegalStateException("Invalid AND expressionDefinition: " + expressionDefinition);
                }
            } else if (expressionDefinition.getLogicalOperator() == LogicalOperator.OR) {
                if (children.size() == 2) {
                    var left = visit(children.get(0));
                    var right = visit(children.get(1));
                    return new OrExpression(left, right);
                } else {
                    throw new IllegalStateException("Invalid OR expressionDefinition: " + expressionDefinition);
                }
            } else if (expressionDefinition.getLogicalOperator() == LogicalOperator.NOT) {
                if (children.size() == 1) {
                    return new NotExpression(visit(children.get(0)));
                } else {
                    throw new IllegalStateException("Invalid NOT expressionDefinition: " + expressionDefinition);
                }
            } else {
                throw new IllegalStateException("Unsupported logicalOperator: " + expressionDefinition.getLogicalOperator());
            }
        } else {
            throw new IllegalStateException("Unsupported expressionDefinition: " + expressionDefinition);
        }
    }
}

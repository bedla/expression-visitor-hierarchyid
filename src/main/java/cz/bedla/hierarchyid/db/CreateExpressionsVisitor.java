package cz.bedla.hierarchyid.db;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.BooleanExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;

class CreateExpressionsVisitor implements ExpressionDefinitionNodeVisitor<Expression> {
    @Override
    public Expression visit(ExpressionDefinitionNode node) {
        var expressionDefinition = node.getExpressionDefinition();
        if (expressionDefinition.getType() == ExpressionDefinition.Type.VALUE) {
            return new BooleanExpression(expressionDefinition.getOperand());
        } else if (expressionDefinition.getType() == ExpressionDefinition.Type.OPERATOR) {
            var children = node.getChildren();
            if (expressionDefinition.getOperator() == ExpressionDefinition.Operator.AND) {
                var expressions = children.stream()
                        .map(this::visit)
                        .toList();
                return new AndExpression(expressions);
            } else if (expressionDefinition.getOperator() == ExpressionDefinition.Operator.OR) {
                var expressions = children.stream()
                        .map(this::visit)
                        .toList();
                return new OrExpression(expressions);
            } else if (expressionDefinition.getOperator() == ExpressionDefinition.Operator.NOT) {
                if (children.size() == 1) {
                    return new NotExpression(visit(children.get(0)));
                } else {
                    throw new IllegalStateException("Invalid NOT exprDefinition: " + expressionDefinition);
                }
            } else {
                throw new IllegalStateException("Unsupported operator: " + expressionDefinition.getOperator());
            }
        } else {
            throw new IllegalStateException("Unsupported exprDefinition: " + expressionDefinition);
        }
    }
}

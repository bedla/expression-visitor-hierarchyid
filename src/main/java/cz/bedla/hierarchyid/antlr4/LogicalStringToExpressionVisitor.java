package cz.bedla.hierarchyid.antlr4;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.BooleanExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.VariableExpression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;

import java.util.Map;
import java.util.Objects;

public class LogicalStringToExpressionVisitor extends SimpleBooleanBaseVisitor<Expression> {
    private final Map<String, Boolean> variables;

    public LogicalStringToExpressionVisitor() {
        this(null);
    }

    public LogicalStringToExpressionVisitor(Map<String, Boolean> variables) {
        this.variables = variables;
    }

    @Override
    public Expression visitParse(SimpleBooleanParser.ParseContext ctx) {
        return super.visit(ctx.expression());
    }

    @Override
    public Expression visitIdentifierExpression(SimpleBooleanParser.IdentifierExpressionContext ctx) {
        var variableName = ctx.IDENTIFIER().getText();
        if (variables == null) {
            return new VariableExpression(variableName);
        } else {
            return new BooleanExpression(Objects.requireNonNull(variables.get(variableName),
                    "Unable to find variable: " + variableName));
        }
    }

    @Override
    public Expression visitAndExpression(SimpleBooleanParser.AndExpressionContext ctx) {
        return new AndExpression(visit(ctx.left), visit(ctx.right));
    }

    @Override
    public Expression visitOrExpression(SimpleBooleanParser.OrExpressionContext ctx) {
        return new OrExpression(visit(ctx.left), visit(ctx.right));
    }

    @Override
    public Expression visitNotExpression(SimpleBooleanParser.NotExpressionContext ctx) {
        return new NotExpression(this.visit(ctx.expression()));
    }
}

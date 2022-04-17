package cz.bedla.hierarchyid.antlr4;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.VariableExpression;

public class StringToExpressionVisitor extends SimpleBooleanBaseVisitor<Expression> {
    @Override
    public Expression visitParse(SimpleBooleanParser.ParseContext ctx) {
        return super.visit(ctx.expression());
    }

    @Override
    public Expression visitIdentifierExpression(SimpleBooleanParser.IdentifierExpressionContext ctx) {
        var variableName = ctx.IDENTIFIER().getText();
        return new VariableExpression(variableName);
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

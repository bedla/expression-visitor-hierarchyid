package cz.bedla.hierarchyid.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;

public class ToStringWithParenthesesVisitor extends SimpleBooleanBaseVisitor<String> {
    @Override
    public String visitParse(SimpleBooleanParser.ParseContext ctx) {
        return super.visit(ctx.expression());
    }

    @Override
    public String visitIdentifierExpression(SimpleBooleanParser.IdentifierExpressionContext ctx) {
        return ctx.IDENTIFIER().getText();
    }

    @Override
    public String visitAndExpression(SimpleBooleanParser.AndExpressionContext ctx) {
        return parentheses(ctx, visit(ctx.left) + " AND " + visit(ctx.right));
    }

    @Override
    public String visitOrExpression(SimpleBooleanParser.OrExpressionContext ctx) {
        return parentheses(ctx, visit(ctx.left) + " OR " + visit(ctx.right));
    }

    @Override
    public String visitNotExpression(SimpleBooleanParser.NotExpressionContext ctx) {
        return parentheses(ctx, "NOT " + this.visit(ctx.expression()));
    }

    private String parentheses(ParserRuleContext ctx, String expressionString) {
        if (ctx.getParent() instanceof SimpleBooleanParser.ParseContext) {
            return expressionString;
        } else {
            return "(" + expressionString + ")";
        }
    }
}

package cz.bedla.hierarchyid.antlr4;

import java.util.Map;
import java.util.Objects;

public class Antlr4EvalVisitor extends SimpleBooleanBaseVisitor<Boolean> {
    private final Map<String, Boolean> variables;

    public Antlr4EvalVisitor(Map<String, Boolean> variables) {
        this.variables = variables;
    }

    @Override
    public Boolean visitParse(SimpleBooleanParser.ParseContext ctx) {
        return super.visit(ctx.expression());
    }

    @Override
    public Boolean visitIdentifierExpression(SimpleBooleanParser.IdentifierExpressionContext ctx) {
        var variableName = ctx.IDENTIFIER().getText();
        return Objects.requireNonNull(variables.get(variableName),
                "Unable to find variable: " + variableName);
    }

    @Override
    public Boolean visitAndExpression(SimpleBooleanParser.AndExpressionContext ctx) {
        return (boolean) visit(ctx.left) & visit(ctx.right);
    }

    @Override
    public Boolean visitOrExpression(SimpleBooleanParser.OrExpressionContext ctx) {
        return (boolean) visit(ctx.left) | visit(ctx.right);
    }

    @Override
    public Boolean visitNotExpression(SimpleBooleanParser.NotExpressionContext ctx) {
        return !this.visit(ctx.expression());
    }
}

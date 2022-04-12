package cz.bedla.hierarchyid.antlr4;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Antlr4Main {
    public static void main(String[] args) {
        var variables = Map.of(
                "A", false,
                "B", true,
                "C", false,
                "D", true
        );

        List.of(
                "NOT A OR B",
                "A AND NOT B AND C",
                "NOT D",
                "A AND B OR C AND D",
                "A OR B AND C OR D",
                "A AND B AND C AND D",
                "A OR B OR C OR D",
                "A OR B AND C AND C OR D OR D",
                "A OR B",
                "A AND B",
                "A AND NOT B",
                "A AND B OR C",
                "A AND B AND C OR D",
                "A OR B AND C",
                "NOT A AND B OR C",
                "NOT A OR B AND C"
        ).forEach(expression -> {
            var lexer = new SimpleBooleanLexer(CharStreams.fromString(expression));
            var parser = new SimpleBooleanParser(new CommonTokenStream(lexer));
            var result = new EvalVisitor(variables).visit(parser.parse());
            parser.reset();
            var parentheses = new AddParenthesesVisitor().visitParse(parser.parse());
            System.out.printf("%-70s -> %-6s ->\t%s\n", expression, result, parentheses);
        });
    }

    private static class EvalVisitor extends SimpleBooleanBaseVisitor<Boolean> {
        private final Map<String, Boolean> variables;

        private EvalVisitor(Map<String, Boolean> variables) {
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

    private static class AddParenthesesVisitor extends SimpleBooleanBaseVisitor<String> {
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
}

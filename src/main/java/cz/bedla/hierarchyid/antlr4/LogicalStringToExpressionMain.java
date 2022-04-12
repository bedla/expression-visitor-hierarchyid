package cz.bedla.hierarchyid.antlr4;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.BooleanExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LogicalStringToExpressionMain {
    public static void main(String[] args) {
        var variables = Map.of(
                "A", false,
                "B", true,
                "C", true,
                "D", false
        );

        List.of(
                "A AND B OR C AND D",
                "A AND B AND C AND D"
        ).forEach(expression -> {
            var lexer = new SimpleBooleanLexer(CharStreams.fromString(expression));
            var parser = new SimpleBooleanParser(new CommonTokenStream(lexer));
            var result = new LogicalStringToExpressionVisitor(variables).visit(parser.parse());
            System.out.printf("%-70s -> %s\n", expression, result);
        });

    }

    private static class LogicalStringToExpressionVisitor extends SimpleBooleanBaseVisitor<Expression> {
        private final Map<String, Boolean> variables;

        private LogicalStringToExpressionVisitor(Map<String, Boolean> variables) {
            this.variables = variables;
        }

        @Override
        public Expression visitParse(SimpleBooleanParser.ParseContext ctx) {
            return super.visit(ctx.expression());
        }

        @Override
        public Expression visitIdentifierExpression(SimpleBooleanParser.IdentifierExpressionContext ctx) {
            var variableName = ctx.IDENTIFIER().getText();
            return new BooleanExpression(Objects.requireNonNull(variables.get(variableName),
                    "Unable to find variable: " + variableName));
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
}

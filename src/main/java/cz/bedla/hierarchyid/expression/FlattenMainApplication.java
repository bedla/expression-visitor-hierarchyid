package cz.bedla.hierarchyid.expression;

import cz.bedla.hierarchyid.antlr4.LogicalStringToExpressionVisitor;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanLexer;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class FlattenMainApplication {
    public static void main(String[] args) {
        List.of(
                List.of("A", "AND B", "OR C", "AND D"),
                List.of("A", "AND B", "AND C", "AND D"),
                List.of("NOT A", "AND B", "AND C", "AND D"),
                List.of("NOT A", "AND B", "OR C", "AND D"),
                List.of("NOT A", "OR B", "AND C", "OR D")
        ).forEach(item -> {
            var expression = item.stream().collect(joining(" ", "", ""));
            var lexer = new SimpleBooleanLexer(CharStreams.fromString(expression));
            var parser = new SimpleBooleanParser(new CommonTokenStream(lexer));
            var result = new LogicalStringToExpressionVisitor().visit(parser.parse());
            var flattenVisitor = new FlattenVisitor();
            flattenVisitor.visit(result);
            System.out.printf("%-35s -> %-35s -> %s\n", expression, result, flattenVisitor.flatten.stream().collect(joining(" ", "", "")));
        });

    }

    private static class FlattenVisitor implements ExpressionVisitor<Void> {
        private final List<String> flatten = new ArrayList<>();

        @Override
        public Void visit(VariableExpression expression) {
            flatten.add(expression.getVariableName());
            return null;
        }

        @Override
        public Void visit(BooleanExpression expression) {
            flatten.add(String.valueOf(expression.getValue()));
            return null;
        }

        @Override
        public Void visit(AndExpression expression) {
            var expressions = expression.getExpressions();
            for (int i = 0; i < expressions.size(); i++) {
                if (i >= 1) {
                    flatten.add("AND");
                }
                Expression expr = expressions.get(i);
                expr.accept(this);
            }
            return null;
        }

        @Override
        public Void visit(OrExpression expression) {
            var expressions = expression.getExpressions();
            for (int i = 0; i < expressions.size(); i++) {
                if (i >= 1) {
                    flatten.add("OR");
                }
                Expression expr = expressions.get(i);
                expr.accept(this);
            }
            return null;
        }

        @Override
        public Void visit(NotExpression expression) {
            flatten.add("NOT");
            expression.getExpression().accept(this);
            return null;
        }
    }
}


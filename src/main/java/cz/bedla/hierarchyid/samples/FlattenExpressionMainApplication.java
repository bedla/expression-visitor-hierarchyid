package cz.bedla.hierarchyid.samples;

import cz.bedla.hierarchyid.antlr4.LogicalStringToExpressionVisitor;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanLexer;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanParser;
import cz.bedla.hierarchyid.expression.samplevisitors.FlattenVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class FlattenExpressionMainApplication {
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
            System.out.printf("%-35s -> %-35s -> %s\n", expression, result, flattenVisitor.getFlatten().stream().collect(joining(" ", "", "")));
        });
    }
}


package cz.bedla.hierarchyid.samples;

import cz.bedla.hierarchyid.antlr4.StringToExpressionParser;
import cz.bedla.hierarchyid.expression.samplevisitors.FlattenVisitor;

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
            var expressionStr = item.stream().collect(joining(" ", "", ""));
            var result = new StringToExpressionParser().parse(expressionStr);
            var flattenVisitor = new FlattenVisitor();
            flattenVisitor.visit(result);
            System.out.printf("%-35s -> %-35s -> %s\n", expressionStr, result, flattenVisitor.getFlatten().stream().collect(joining(" ", "", "")));
        });
    }
}


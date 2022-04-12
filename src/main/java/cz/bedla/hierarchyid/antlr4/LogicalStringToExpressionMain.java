package cz.bedla.hierarchyid.antlr4;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;
import java.util.Map;

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

}

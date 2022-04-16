package cz.bedla.hierarchyid.main;

import cz.bedla.hierarchyid.antlr4.LogicalStringToExpressionVisitor;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanLexer;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;

public class LogicalStringToExpressionMain {
    public static void main(String[] args) {
        List.of(
                "A AND B OR C AND D",
                "A AND B AND C AND D"
        ).forEach(expression -> {
            var lexer = new SimpleBooleanLexer(CharStreams.fromString(expression));
            var parser = new SimpleBooleanParser(new CommonTokenStream(lexer));
            var result = new LogicalStringToExpressionVisitor().visit(parser.parse());
            System.out.printf("%-70s -> %s\n", expression, result);
        });

    }

}

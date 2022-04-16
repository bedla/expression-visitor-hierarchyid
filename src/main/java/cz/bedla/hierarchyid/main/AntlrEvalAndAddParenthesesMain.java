package cz.bedla.hierarchyid.main;

import cz.bedla.hierarchyid.antlr4.Antlr4EvalVisitor;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanLexer;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanParser;
import cz.bedla.hierarchyid.antlr4.ToStringWithParenthesesVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;
import java.util.Map;

public class AntlrEvalAndAddParenthesesMain {
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
            var result = new Antlr4EvalVisitor(variables).visit(parser.parse());
            parser.reset();
            var parentheses = new ToStringWithParenthesesVisitor().visitParse(parser.parse());
            System.out.printf("%-70s -> %-6s ->\t%s\n", expression, result, parentheses);
        });
    }
}

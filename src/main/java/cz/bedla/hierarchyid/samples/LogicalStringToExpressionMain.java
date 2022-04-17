package cz.bedla.hierarchyid.samples;

import cz.bedla.hierarchyid.antlr4.StringToExpressionParser;

import java.util.List;

public class LogicalStringToExpressionMain {
    public static void main(String[] args) {
        List.of(
                "A AND B OR C AND D",
                "A AND B AND C AND D"
        ).forEach(expressionStr -> {
            var result = new StringToExpressionParser().parse(expressionStr);
            System.out.printf("%-70s -> %s\n", expressionStr, result);
        });
    }
}

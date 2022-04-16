package cz.bedla.hierarchyid.rest;

import cz.bedla.hierarchyid.antlr4.LogicalStringToExpressionVisitor;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanLexer;
import cz.bedla.hierarchyid.antlr4.SimpleBooleanParser;
import cz.bedla.hierarchyid.expression.Expression;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;


public abstract class RestDtoToExpressionConverter<DTO extends ExpressionDto, V> {
    private static final String KEY_PREFIX = "X";
    private static final String NOT_OPERATOR_STR = "NOT";
    private static final String AND_OPERATOR_STR = "AND";
    private static final String OR_OPERATOR_STR = "OR";

    public Expression convert(List<DTO> list) {
        validateList(list);

        var variableIndex = new HashMap<String, DTO>();
        var expressionStr = createExpressionString(list, variableIndex);

        var lexer = new SimpleBooleanLexer(CharStreams.fromString(expressionStr));
        var parser = new SimpleBooleanParser(new CommonTokenStream(lexer));
        var expression = new LogicalStringToExpressionVisitor().visit(parser.parse());

        return createReplaceVariablesVisitor(variableIndex).visit(expression);
    }

    protected abstract RestDtoReplaceVariablesVisitor<DTO, V> createReplaceVariablesVisitor(Map<String, DTO> variableIndex);

    private String createExpressionString(List<DTO> list, Map<String, DTO> index) {
        var expressionList = new ArrayList<String>();
        for (DTO item : list) {
            var key = KEY_PREFIX + (index.size() + 1);
            index.put(key, item);
            if (item.leftOperator() == LeftOperator.NOT) {
                expressionList.add(NOT_OPERATOR_STR);
            }
            expressionList.add(key);
            if (item.rightOperator() != null) {
                switch (item.rightOperator()) {
                    case AND -> expressionList.add(AND_OPERATOR_STR);
                    case OR -> expressionList.add(OR_OPERATOR_STR);
                }
            }
        }
        return expressionList.stream().collect(joining(" ", "", ""));
    }

    private void validateList(List<DTO> list) {
        requireNonNull(list, "List cannot be null");

        if (list.isEmpty()) {
            throw new IllegalStateException("List cannot be empty");
        }

        var lastItemRightOperator = list.get(list.size() - 1).rightOperator();
        if (lastItemRightOperator != null) {
            throw new IllegalStateException("Right operator in list on last item cannot be set, but contains: " + lastItemRightOperator);
        }

        if (list.size() >= 2) {
            for (int i = 0; i < list.size() - 1; i++) {
                DTO item = list.get(i);
                if (item.rightOperator() == null) {
                    throw new IllegalStateException("List at index=" + i + " contains item that has null right operator");
                }
            }
        }

        additionalValidation(list);
    }

    protected abstract void additionalValidation(List<DTO> list);
}

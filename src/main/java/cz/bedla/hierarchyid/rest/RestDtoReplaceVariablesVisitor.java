package cz.bedla.hierarchyid.rest;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

import java.util.Map;

public abstract class RestDtoReplaceVariablesVisitor<DTO extends ExpressionDto, V> implements ExpressionVisitor<Expression, String> {
    private final Map<String, DTO> variableIndex;

    public RestDtoReplaceVariablesVisitor(Map<String, DTO> variableIndex) {
        this.variableIndex = variableIndex;
    }

    @Override
    public Expression visit(TerminalExpression<String> expression) {
        var variableName = expression.getValue();
        var item = variableIndex.get(variableName);
        if (item == null) {
            throw new IllegalStateException("Unable to find variable=" + variableName + " in index=" + variableIndex);
        }
        return createTerminalExpression(item);
    }

    protected abstract TerminalExpression<V> createTerminalExpression(DTO item);

    @Override
    public Expression visit(AndExpression expression) {
        var left = expression.getLeftExpression().accept(this);
        var right = expression.getRightExpression().accept(this);
        return new AndExpression(left, right);
    }

    @Override
    public Expression visit(OrExpression expression) {
        var left = expression.getLeftExpression().accept(this);
        var right = expression.getRightExpression().accept(this);
        return new OrExpression(left, right);
    }

    @Override
    public Expression visit(NotExpression expression) {
        var result = expression.getExpression().accept(this);
        return new NotExpression(result);
    }
}

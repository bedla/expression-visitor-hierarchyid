package cz.bedla.hierarchyid.rest.fact;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.fact.FactExpression;
import cz.bedla.hierarchyid.rest.RestDtoReplaceVariablesVisitor;

import java.util.Map;

public class FactRestDtoReplaceVariablesVisitor extends RestDtoReplaceVariablesVisitor<FactExpressionDto, Fact> {
    public FactRestDtoReplaceVariablesVisitor(Map<String, FactExpressionDto> variableIndex) {
        super(variableIndex);
    }

    @Override
    protected FactExpression createTerminalExpression(FactExpressionDto item) {
        return new FactExpression(new Fact(item.columnName(), convert(item.operator()), item.value()));
    }

    private Fact.Operator convert(FactExpressionDto.Operator operator) {
        return switch (operator) {
            case EQ -> Fact.Operator.EQ;
            case NOT_EQ -> Fact.Operator.NOT_EQ;
            case LT -> Fact.Operator.LT;
            case LTE -> Fact.Operator.LTE;
            case GT -> Fact.Operator.GT;
            case GTE -> Fact.Operator.GTE;
        };
    }
}

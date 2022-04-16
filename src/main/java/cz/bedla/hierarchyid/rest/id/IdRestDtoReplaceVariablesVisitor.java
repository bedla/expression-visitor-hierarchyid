package cz.bedla.hierarchyid.rest.id;

import cz.bedla.hierarchyid.expression.id.IdExpression;
import cz.bedla.hierarchyid.rest.RestDtoReplaceVariablesVisitor;

import java.util.Map;

public class IdRestDtoReplaceVariablesVisitor extends RestDtoReplaceVariablesVisitor<IdExpressionDto, Integer> {
    public IdRestDtoReplaceVariablesVisitor(Map<String, IdExpressionDto> variableIndex) {
        super(variableIndex);
    }

    @Override
    protected IdExpression createTerminalExpression(IdExpressionDto item) {
        return new IdExpression(item.id());
    }
}

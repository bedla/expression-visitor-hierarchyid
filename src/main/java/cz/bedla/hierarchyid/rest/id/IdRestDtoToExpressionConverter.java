package cz.bedla.hierarchyid.rest.id;

import cz.bedla.hierarchyid.rest.RestDtoToExpressionConverter;

import java.util.List;
import java.util.Map;

public class IdRestDtoToExpressionConverter extends RestDtoToExpressionConverter<IdExpressionDto, Integer> {
    @Override
    protected IdRestDtoReplaceVariablesVisitor createReplaceVariablesVisitor(Map<String, IdExpressionDto> variableIndex) {
        return new IdRestDtoReplaceVariablesVisitor(variableIndex);
    }

    @Override
    protected void additionalValidation(List<IdExpressionDto> list) {
    }
}

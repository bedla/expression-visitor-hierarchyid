package cz.bedla.hierarchyid.rest.fact;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.rest.RestDtoToExpressionConverter;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toSet;

public class FactRestDtoToExpressionConverter extends RestDtoToExpressionConverter<FactExpressionDto, Fact> {
    @Override
    protected FactRestDtoReplaceVariablesVisitor createReplaceVariablesVisitor(Map<String, FactExpressionDto> variableIndex) {
        return new FactRestDtoReplaceVariablesVisitor(variableIndex);
    }

    @Override
    protected void additionalValidation(List<FactExpressionDto> list) {
        var columnNames = list.stream()
                .map(FactExpressionDto::columnName)
                .collect(toSet());
        if (columnNames.size() != list.size()) {
            throw new IllegalStateException("Fact list does not contain unique column names");
        }
    }
}

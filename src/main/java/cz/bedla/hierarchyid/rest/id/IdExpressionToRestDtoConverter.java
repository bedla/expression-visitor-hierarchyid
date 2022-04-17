package cz.bedla.hierarchyid.rest.id;

import cz.bedla.hierarchyid.rest.ExpressionToRestDtoConverter;
import cz.bedla.hierarchyid.rest.LeftOperator;
import cz.bedla.hierarchyid.rest.RightOperator;


public class IdExpressionToRestDtoConverter extends ExpressionToRestDtoConverter<IdExpressionDto> {
    @Override
    protected IdExpressionDto createExpressionDto(LeftOperator leftOperator, Object item, RightOperator rightOperator) {
        if (item instanceof Integer id) {
            return new IdExpressionDto(
                    leftOperator,
                    id,
                    rightOperator
            );
        } else {
            throw new IllegalStateException("Unsupported item type: " + item);
        }
    }
}

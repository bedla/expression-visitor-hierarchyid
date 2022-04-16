package cz.bedla.hierarchyid.rest.id;

import cz.bedla.hierarchyid.expression.id.IdFlattenExpressionVisitor;
import cz.bedla.hierarchyid.rest.ExpressionToRestDtoConverter;
import cz.bedla.hierarchyid.rest.LeftOperator;
import cz.bedla.hierarchyid.rest.RightOperator;


public class IdExpressionToRestDtoConverter extends ExpressionToRestDtoConverter<IdExpressionDto, Integer> {

    @Override
    protected IdFlattenExpressionVisitor createFlattenExpressionVisitor() {
        return new IdFlattenExpressionVisitor();
    }

    @Override
    protected IdExpressionDto createExpressionDto(LeftOperator leftOperator, Integer item, RightOperator rightOperator) {
        return new IdExpressionDto(
                leftOperator,
                item,
                rightOperator
        );
    }
}

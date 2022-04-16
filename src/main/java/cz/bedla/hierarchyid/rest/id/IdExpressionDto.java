package cz.bedla.hierarchyid.rest.id;

import cz.bedla.hierarchyid.rest.ExpressionDto;
import cz.bedla.hierarchyid.rest.LeftOperator;
import cz.bedla.hierarchyid.rest.RightOperator;

public record IdExpressionDto(
        LeftOperator leftOperator,
        int id,
        RightOperator rightOperator) implements ExpressionDto {
}

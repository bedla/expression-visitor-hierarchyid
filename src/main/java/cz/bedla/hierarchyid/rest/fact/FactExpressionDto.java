package cz.bedla.hierarchyid.rest.fact;

import cz.bedla.hierarchyid.rest.ExpressionDto;
import cz.bedla.hierarchyid.rest.LeftOperator;
import cz.bedla.hierarchyid.rest.RightOperator;

import static java.util.Objects.requireNonNull;

public record FactExpressionDto(
        LeftOperator leftOperator,
        String columnName,
        Operator operator,
        Object value,
        RightOperator rightOperator) implements ExpressionDto {

    public FactExpressionDto {
        requireNonNull(columnName, "columnName cannot be null");
        requireNonNull(operator, "operator cannot be null");
    }

    public enum Operator {
        EQ, NOT_EQ, LT, LTE, GT, GTE
    }

}

package cz.bedla.hierarchyid.rest.fact;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.rest.ExpressionToRestDtoConverter;
import cz.bedla.hierarchyid.rest.LeftOperator;
import cz.bedla.hierarchyid.rest.RightOperator;


public class FactExpressionToRestDtoConverter extends ExpressionToRestDtoConverter<FactExpressionDto> {
    @Override
    protected FactExpressionDto createExpressionDto(LeftOperator leftOperator, Object item, RightOperator rightOperator) {
        if (item instanceof Fact fact) {
            return new FactExpressionDto(
                    leftOperator,
                    fact.columnName(), operator(fact.operator()), fact.value(),
                    rightOperator
            );
        } else {
            throw new IllegalStateException("Unsupported item type: " + item);
        }
    }

    private FactExpressionDto.Operator operator(Fact.Operator operator) {
        return switch (operator) {
            case EQ -> FactExpressionDto.Operator.EQ;
            case NOT_EQ -> FactExpressionDto.Operator.NOT_EQ;
            case LT -> FactExpressionDto.Operator.LT;
            case LTE -> FactExpressionDto.Operator.LTE;
            case GT -> FactExpressionDto.Operator.GT;
            case GTE -> FactExpressionDto.Operator.GTE;
        };
    }
}

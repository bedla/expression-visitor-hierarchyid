package cz.bedla.hierarchyid.rest.fact;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.fact.FactFlattenExpressionVisitor;
import cz.bedla.hierarchyid.rest.ExpressionToRestDtoConverter;
import cz.bedla.hierarchyid.rest.LeftOperator;
import cz.bedla.hierarchyid.rest.RightOperator;


public class FactExpressionToRestDtoConverter extends ExpressionToRestDtoConverter<FactExpressionDto, Fact> {

    @Override
    protected FactFlattenExpressionVisitor createFlattenExpressionVisitor() {
        return new FactFlattenExpressionVisitor();
    }

    @Override
    protected FactExpressionDto createExpressionDto(LeftOperator leftOperator, Fact item, RightOperator rightOperator) {
        return new FactExpressionDto(
                leftOperator,
                item.columnName(), operator(item.operator()), item.value(),
                rightOperator
        );
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

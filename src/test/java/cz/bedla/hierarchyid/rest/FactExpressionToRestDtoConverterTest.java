package cz.bedla.hierarchyid.rest;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.fact.FactExpression;
import cz.bedla.hierarchyid.rest.fact.FactExpressionDto;
import cz.bedla.hierarchyid.rest.fact.FactExpressionToRestDtoConverter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FactExpressionToRestDtoConverterTest {
    private final FactExpressionToRestDtoConverter converter = new FactExpressionToRestDtoConverter();

    @Test
    void convert() {
        var expr1 = new FactExpression(new Fact("x1", Fact.Operator.EQ, 123));
        var expr2 = new FactExpression(new Fact("x2", Fact.Operator.GT, 456));
        var expr3 = new FactExpression(new Fact("x3", Fact.Operator.NOT_EQ, 789));
        var expression = new OrExpression(
                new AndExpression(expr1, new AndExpression(expr2, expr3)),
                new NotExpression(expr3)
        );

        var list = converter.convert(expression);
        assertThat(list)
                .containsExactly(
                        new FactExpressionDto(null, "x1", FactExpressionDto.Operator.EQ, 123, RightOperator.AND),
                        new FactExpressionDto(null, "x2", FactExpressionDto.Operator.GT, 456, RightOperator.AND),
                        new FactExpressionDto(null, "x3", FactExpressionDto.Operator.NOT_EQ, 789, RightOperator.OR),
                        new FactExpressionDto(LeftOperator.NOT, "x3", FactExpressionDto.Operator.NOT_EQ, 789, null)
                );
    }
}

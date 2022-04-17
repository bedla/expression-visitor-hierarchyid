package cz.bedla.hierarchyid.rest;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.id.IdExpression;
import cz.bedla.hierarchyid.rest.id.IdExpressionDto;
import cz.bedla.hierarchyid.rest.id.IdExpressionToRestDtoConverter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdExpressionToRestDtoConverterTest {
    private final IdExpressionToRestDtoConverter converter = new IdExpressionToRestDtoConverter();

    @Test
    void convert() {
        var expr1 = new IdExpression(123);
        var expr2 = new IdExpression(456);
        var expr3 = new IdExpression(789);
        var expression = new OrExpression(
                new AndExpression(expr1, new AndExpression(expr2, expr3)),
                new NotExpression(expr3)
        );

        var list = converter.convert(expression);
        assertThat(list)
                .containsExactly(
                        new IdExpressionDto(null, 123, RightOperator.AND),
                        new IdExpressionDto(null, 456, RightOperator.AND),
                        new IdExpressionDto(null, 789, RightOperator.OR),
                        new IdExpressionDto(LeftOperator.NOT, 789, null)
                );
    }
}

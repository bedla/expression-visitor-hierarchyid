package cz.bedla.hierarchyid.rest;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.id.IdExpression;
import cz.bedla.hierarchyid.rest.id.IdExpressionDto;
import cz.bedla.hierarchyid.rest.id.IdRestDtoToExpressionConverter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IdRestDtoToExpressionConverterTest {
    private final IdRestDtoToExpressionConverter converter = new IdRestDtoToExpressionConverter();

    @Test
    void convert() {
        var expression = converter.convert(List.of(
                new IdExpressionDto(null, 123, RightOperator.AND),
                new IdExpressionDto(null, 456, RightOperator.OR),
                new IdExpressionDto(LeftOperator.NOT, 789, null)
        ));

        var expected = new OrExpression(
                new AndExpression(
                        new IdExpression(123),
                        new IdExpression(456)
                ),
                new NotExpression(new IdExpression(789))
        );

        assertThat(expression)
                .isEqualTo(expected);
    }
}

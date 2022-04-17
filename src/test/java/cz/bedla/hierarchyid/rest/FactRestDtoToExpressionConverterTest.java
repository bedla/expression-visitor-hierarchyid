package cz.bedla.hierarchyid.rest;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.fact.FactExpression;
import cz.bedla.hierarchyid.rest.fact.FactExpressionDto;
import cz.bedla.hierarchyid.rest.fact.FactRestDtoToExpressionConverter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FactRestDtoToExpressionConverterTest {
    private final FactRestDtoToExpressionConverter converter = new FactRestDtoToExpressionConverter();

    @Test
    void convert() {
        var expression = converter.convert(List.of(
                new FactExpressionDto(null, "x1", FactExpressionDto.Operator.EQ, 123, RightOperator.AND),
                new FactExpressionDto(null, "x2", FactExpressionDto.Operator.GT, 456, RightOperator.OR),
                new FactExpressionDto(LeftOperator.NOT, "x3", FactExpressionDto.Operator.NOT_EQ, 789, null)
        ));

        var expected = new OrExpression(
                new AndExpression(
                        new FactExpression(new Fact("x1", Fact.Operator.EQ, 123)),
                        new FactExpression(new Fact("x2", Fact.Operator.GT, 456))
                ),
                new NotExpression(new FactExpression(new Fact("x3", Fact.Operator.NOT_EQ, 789)))
        );

        assertThat(expression)
                .isEqualTo(expected);
    }

    @Test
    void validateInput_duplicateColumnName() {
        assertThatThrownBy(() -> converter.convert(List.of(
                new FactExpressionDto(null, "duplicate", FactExpressionDto.Operator.EQ, 123, RightOperator.AND),
                new FactExpressionDto(null, "duplicate", FactExpressionDto.Operator.EQ, 123, RightOperator.AND),
                new FactExpressionDto(null, "duplicate", FactExpressionDto.Operator.EQ, 123, null)
        )))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Fact list does not contain unique column names");
    }
}

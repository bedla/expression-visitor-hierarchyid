package cz.bedla.hierarchyid.db.fact;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.fact.FactExpression;
import cz.bedla.hierarchyid.rest.LeftOperator;
import cz.bedla.hierarchyid.rest.RightOperator;
import cz.bedla.hierarchyid.rest.fact.FactExpressionDto;
import cz.bedla.hierarchyid.rest.fact.FactRestDtoToExpressionConverter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FactDefinitionToExpressionConvertorTest {

    private final FactDefinitionToExpressionConvertor convertor = new FactDefinitionToExpressionConvertor();

    private final FactExpressionToDefinitionConverter toDefinitionConverter = new FactExpressionToDefinitionConverter();

    @Test
    void convert() {
        var toSaveExpression = new FactRestDtoToExpressionConverter().convert(List.of(
                new FactExpressionDto(null, "COL_AAA", FactExpressionDto.Operator.EQ, "foo", RightOperator.AND),
                new FactExpressionDto(LeftOperator.NOT, "COL_BBB", FactExpressionDto.Operator.LT, 123, RightOperator.OR),
                new FactExpressionDto(null, "COL_CCC", FactExpressionDto.Operator.GTE, 1000, null)
        ));
        var toSaveList = toDefinitionConverter.convert(toSaveExpression);

        var expression = convertor.convert(toSaveList);

        var expectedExpression = new OrExpression(
                new AndExpression(
                        new FactExpression(new Fact("COL_AAA", Fact.Operator.EQ, "foo")),
                        new NotExpression(new FactExpression(new Fact("COL_BBB", Fact.Operator.LT, 123)))),
                new FactExpression(new Fact("COL_CCC", Fact.Operator.GTE, 1000)));
        assertThat(expression)
                .isEqualTo(expectedExpression);
    }
}

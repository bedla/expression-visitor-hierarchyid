package cz.bedla.hierarchyid.rest;

import cz.bedla.hierarchyid.expression.TerminalExpression;
import cz.bedla.hierarchyid.expression.VariableExpression;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RestDtoToExpressionConverterTest {
    private final MyRestDtoToExpressionConverter converter = new MyRestDtoToExpressionConverter();


    @Test
    void invalidInput() {
        assertThatThrownBy(() -> converter.convert(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("List cannot be null");
    }

    @Test
    void validateInput_Empty() {
        assertThatThrownBy(() -> converter.convert(List.of()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("List cannot be empty");
    }

    @Test
    void validateInput_rightOperator() {
        var softly = new SoftAssertions();

        softly.assertThatThrownBy(() -> converter.convert(List.of(new MyExpressionDto(null, "x", RightOperator.OR))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Right operator in list on last item cannot be set, but contains: OR");

        softly.assertThatThrownBy(() -> converter.convert(List.of(
                        new MyExpressionDto(null, "a", RightOperator.OR),
                        new MyExpressionDto(null, "b", RightOperator.AND)
                )))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Right operator in list on last item cannot be set, but contains: AND");

        softly.assertThatThrownBy(() -> converter.convert(List.of(
                        new MyExpressionDto(null, "a", RightOperator.AND),
                        new MyExpressionDto(null, "b", null),
                        new MyExpressionDto(null, "c", null)
                )))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("List at index=1 contains item that has null right operator");

        softly.assertAll();
    }

    private record MyExpressionDto(LeftOperator leftOperator,
                                   String key,
                                   RightOperator rightOperator) implements ExpressionDto {
    }

    private static class MyRestDtoToExpressionConverter extends RestDtoToExpressionConverter<MyExpressionDto, String> {
        @Override
        protected RestDtoReplaceVariablesVisitor<MyExpressionDto, String> createReplaceVariablesVisitor(Map<String, MyExpressionDto> variableIndex) {
            return new MyReplaceVariablesVisitor(variableIndex);
        }

        @Override
        protected void additionalValidation(List<MyExpressionDto> list) {
        }
    }

    private static class MyReplaceVariablesVisitor extends RestDtoReplaceVariablesVisitor<MyExpressionDto, String> {
        public MyReplaceVariablesVisitor(Map<String, MyExpressionDto> variableIndex) {
            super(variableIndex);
        }

        @Override
        protected TerminalExpression<String> createTerminalExpression(MyExpressionDto item) {
            return new VariableExpression(item.key());
        }
    }
}

package cz.bedla.hierarchyid.antlr4;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.VariableExpression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class StringToExpressionParserTest {
    private final StringToExpressionParser parser = new StringToExpressionParser();

    @ParameterizedTest
    @MethodSource("stringToExpressionProvider")
    void stringToExpression(String expressionStr, Expression expectedExpression) {
        var expression = parser.parse(expressionStr);
        assertThat(expression)
                .isEqualTo(expectedExpression);
    }

    @ParameterizedTest
    @MethodSource("stringToExpressionInvalidInputProvider")
    void invalidInput(String expressionStr, Consumer<Exception> asserter) {
        try {
            parser.parse(expressionStr);
            fail("Exception should be thrown");
        } catch (Exception e) {
            asserter.accept(e);
        }
    }

    static Stream<Arguments> stringToExpressionProvider() {
        return Stream.of(
                Arguments.of("foo", new VariableExpression("foo")),
                Arguments.of(" foo ", new VariableExpression("foo")),
                Arguments.of(" foo AND bar ", new AndExpression(
                        new VariableExpression("foo"),
                        new VariableExpression("bar"))),
                Arguments.of(" NOT foo AND NOT bar ", new AndExpression(
                        new NotExpression(new VariableExpression("foo")),
                        new NotExpression(new VariableExpression("bar")))),
                Arguments.of(" foo OR NOT bar ", new OrExpression(new VariableExpression("foo"), new NotExpression(new VariableExpression("bar"))))
        );
    }

    static Stream<Arguments> stringToExpressionInvalidInputProvider() {
        return Stream.of(
                Arguments.of(null, (Consumer<Exception>) e ->
                        assertThat(e)
                                .isInstanceOf(NullPointerException.class)
                                .hasMessage("expressionStr cannot be empty")),
                Arguments.of("", (Consumer<Exception>) e ->
                        assertThat(e)
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("expressionStr cannot be empty")),
                Arguments.of("AND", (Consumer<Exception>) e ->
                        assertThat(e)
                                .isInstanceOf(IllegalStateException.class)
                                .hasMessage("line 1:0 mismatched input 'AND' expecting {'NOT', IDENTIFIER}")),
                Arguments.of("A ND", (Consumer<Exception>) e ->
                        assertThat(e)
                                .isInstanceOf(IllegalStateException.class)
                                .hasMessage("line 1:2 extraneous input 'ND' expecting <EOF>")),
                Arguments.of("AND OR NOT", (Consumer<Exception>) e ->
                        assertThat(e)
                                .isInstanceOf(IllegalStateException.class)
                                .hasMessage("line 1:0 mismatched input 'AND' expecting {'NOT', IDENTIFIER}"))
        );
    }
}

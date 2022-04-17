package cz.bedla.hierarchyid.antlr4;

import cz.bedla.hierarchyid.expression.Expression;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static org.apache.commons.lang3.Validate.notEmpty;

public class StringToExpressionParser {
    private final BaseErrorListener errorListener;

    public StringToExpressionParser() {
        this.errorListener = new ThrowingErrorListener();
    }

    public Expression parse(String expressionStr) {
        notEmpty(expressionStr, "expressionStr cannot be empty");

        var lexer = new SimpleBooleanLexer(CharStreams.fromString(expressionStr));
        var parser = new SimpleBooleanParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        return new StringToExpressionVisitor().visit(parser.parse());
    }

    private static class ThrowingErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line,
                                int charPositionInLine,
                                String msg,
                                RecognitionException e) {
            throw new IllegalStateException("line " + line + ":" + charPositionInLine + " " + msg);
        }
    }
}

package cz.bedla.hierarchyid.expression.toolvisitors;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

import static java.util.stream.Collectors.joining;

public class PrintVisitor implements ExpressionVisitor<String, Object> {
    @Override
    public String visit(TerminalExpression<Object> expression) {
        return String.valueOf(expression.getValue());
    }

    @Override
    public String visit(AndExpression expression) {
        return expression.getExpressions().stream()
                .map(it -> it.accept(this))
                .collect(joining(" AND ", "(", ")"));
    }

    @Override
    public String visit(OrExpression expression) {
        return expression.getExpressions().stream()
                .map(it -> it.accept(this))
                .collect(joining(" OR ", "(", ")"));
    }

    @Override
    public String visit(NotExpression expression) {
        return "!" + expression.getExpression().accept(this);
    }
}

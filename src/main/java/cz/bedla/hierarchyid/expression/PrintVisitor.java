package cz.bedla.hierarchyid.expression;

import static java.util.stream.Collectors.joining;

class PrintVisitor implements ExpressionVisitor<String> {
    @Override
    public String visit(VariableExpression expression) {
        return expression.getVariableName();
    }

    @Override
    public String visit(BooleanExpression expression) {
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

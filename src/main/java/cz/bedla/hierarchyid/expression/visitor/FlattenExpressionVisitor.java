package cz.bedla.hierarchyid.expression.visitor;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class FlattenExpressionVisitor implements ExpressionVisitor<Void, Object> {
    private final List<String> flatten = new ArrayList<>();

    private final Map<Object, String> index = new IdentityHashMap<>();

    public List<String> getFlatten() {
        return flatten;
    }

    public Map<Object, String> getIndex() {
        return index;
    }

    @Override
    public Void visit(TerminalExpression<Object> expression) {
        var idStr = index.computeIfAbsent(expression.getValue(), key -> "X" + (index.size() + 1));
        flatten.add("[" + idStr + "]");
        return null;
    }

    @Override
    public Void visit(AndExpression expression) {
        var expressions = expression.getExpressions();
        for (int i = 0; i < expressions.size(); i++) {
            if (i >= 1) {
                flatten.add("AND");
            }
            Expression expr = expressions.get(i);
            expr.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(OrExpression expression) {
        var expressions = expression.getExpressions();
        for (int i = 0; i < expressions.size(); i++) {
            if (i >= 1) {
                flatten.add("OR");
            }
            Expression expr = expressions.get(i);
            expr.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(NotExpression expression) {
        flatten.add("NOT");
        expression.getExpression().accept(this);
        return null;
    }
}

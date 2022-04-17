package cz.bedla.hierarchyid.expression.samplevisitors;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

import java.util.ArrayList;
import java.util.List;

public class FlattenVisitor implements ExpressionVisitor<Void, String> {
    private final List<String> flatten = new ArrayList<>();

    public List<String> getFlatten() {
        return flatten;
    }

    @Override
    public Void visit(TerminalExpression<String> expression) {
        flatten.add(expression.getValue());
        return null;
    }

    @Override
    public Void visit(AndExpression expression) {
        expression.getLeftExpression().accept(this);
        flatten.add("AND");
        expression.getRightExpression().accept(this);
        return null;
    }

    @Override
    public Void visit(OrExpression expression) {
        expression.getLeftExpression().accept(this);
        flatten.add("OR");
        expression.getRightExpression().accept(this);
        return null;
    }

    @Override
    public Void visit(NotExpression expression) {
        flatten.add("NOT");
        expression.getExpression().accept(this);
        return null;
    }
}

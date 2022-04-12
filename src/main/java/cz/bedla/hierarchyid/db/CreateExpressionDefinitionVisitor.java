package cz.bedla.hierarchyid.db;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.BooleanExpression;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.VariableExpression;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

class CreateExpressionDefinitionVisitor implements ExpressionVisitor<CreateExpressionDefinitionVisitor.Node> {
    private final Deque<Node> currentParent = new LinkedList<>();

    @Override
    public Node visit(VariableExpression expression) {
        throw new IllegalStateException("not supported yet");
    }

    @Override
    public Node visit(BooleanExpression expression) {
        var node = new Node();
        node.parent = currentParent.peekFirst();
        node.expressionDefinition = new ExpressionDefinition(ExpressionDefinition.Type.VALUE, null, expression.getValue());
        node.children = new ArrayList<>();
        return node;
    }

    @Override
    public Node visit(AndExpression expression) {
        var node = new Node();

        var children = new ArrayList<Node>();
        doWithParent(node, () -> {
            for (Expression it : expression.getExpressions()) {
                children.add(it.accept(this));
            }
        });

        node.parent = currentParent.peekFirst();
        node.expressionDefinition = new ExpressionDefinition(ExpressionDefinition.Type.OPERATOR, ExpressionDefinition.Operator.AND, null);
        node.children = children;
        return node;
    }

    @Override
    public Node visit(OrExpression expression) {
        var node = new Node();

        var children = new ArrayList<Node>();
        doWithParent(node, () -> {
            for (Expression it : expression.getExpressions()) {
                children.add(it.accept(this));
            }
        });

        node.parent = currentParent.peekFirst();
        node.expressionDefinition = new ExpressionDefinition(ExpressionDefinition.Type.OPERATOR, ExpressionDefinition.Operator.OR, null);
        node.children = children;
        return node;
    }

    @Override
    public Node visit(NotExpression expression) {
        var node = new Node();

        var children = new ArrayList<Node>();
        doWithParent(node, () ->
                children.add(expression.getExpression().accept(this))
        );

        node.parent = currentParent.peekFirst();
        node.expressionDefinition = new ExpressionDefinition(ExpressionDefinition.Type.OPERATOR, ExpressionDefinition.Operator.NOT, null);
        node.children = children;
        return node;
    }

    private void doWithParent(Node parent, Runnable action) {
        try {
            currentParent.push(parent);
            action.run();
        } finally {
            currentParent.pop();
        }
    }

    public static class Node {
        Node parent;
        ExpressionDefinition expressionDefinition;
        List<Node> children;
    }
}

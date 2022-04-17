package cz.bedla.hierarchyid.db;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public abstract class CreateExpressionDefinitionVisitor<VAL, ED extends ExpressionDefinition<VAL, ED>> implements ExpressionVisitor<CreateExpressionDefinitionVisitor.Node<ED>, VAL> {
    private final Deque<Node<ED>> currentParent = new LinkedList<>();

    protected abstract ED createExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, VAL value);

    @Override
    public Node<ED> visit(TerminalExpression<VAL> expression) {
        var node = new Node<ED>();
        node.parent = currentParent.peekFirst();
        node.expressionDefinition = createExpressionDefinition(ExpressionDefinitionType.VALUE, null, expression.getValue());
        node.children = new ArrayList<>();
        return node;
    }

    @Override
    public Node<ED> visit(AndExpression expression) {
        var node = new Node<ED>();

        var children = new ArrayList<Node<ED>>();
        doWithParent(node, () -> {
            children.add(expression.getLeftExpression().accept(this));
            children.add(expression.getRightExpression().accept(this));
        });

        node.parent = currentParent.peekFirst();
        node.expressionDefinition = createExpressionDefinition(ExpressionDefinitionType.LOGICAL_OPERATOR, LogicalOperator.AND, null);
        node.children = children;
        return node;
    }

    @Override
    public Node<ED> visit(OrExpression expression) {
        var node = new Node<ED>();

        var children = new ArrayList<Node<ED>>();
        doWithParent(node, () -> {
            children.add(expression.getLeftExpression().accept(this));
            children.add(expression.getRightExpression().accept(this));
        });

        node.parent = currentParent.peekFirst();
        node.expressionDefinition = createExpressionDefinition(ExpressionDefinitionType.LOGICAL_OPERATOR, LogicalOperator.OR, null);
        node.children = children;
        return node;
    }

    @Override
    public Node<ED> visit(NotExpression expression) {
        var node = new Node<ED>();

        var children = new ArrayList<Node<ED>>();
        doWithParent(node, () ->
                children.add(expression.getExpression().accept(this))
        );

        node.parent = currentParent.peekFirst();
        node.expressionDefinition = createExpressionDefinition(ExpressionDefinitionType.LOGICAL_OPERATOR, LogicalOperator.NOT, null);
        node.children = children;
        return node;
    }

    private void doWithParent(Node<ED> parent, Runnable action) {
        try {
            currentParent.push(parent);
            action.run();
        } finally {
            currentParent.pop();
        }
    }

    public static class Node<ED> {
        Node<ED> parent;
        ED expressionDefinition;
        List<Node<ED>> children;
    }
}

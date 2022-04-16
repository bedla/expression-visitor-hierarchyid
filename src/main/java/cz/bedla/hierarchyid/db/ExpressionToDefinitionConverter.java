package cz.bedla.hierarchyid.db;

import cz.bedla.hierarchyid.expression.Expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public abstract class ExpressionToDefinitionConverter<VAL, ED extends ExpressionDefinition<VAL, ED>> {
    public List<ED> convert(Expression expression) {
        var visitor = createCreateExpressionDefinitionVisitor();
        var rootExpressionDefinitionNode = visitor.visit(expression);
        return createExpressionDefinitions(rootExpressionDefinitionNode);
    }

    protected abstract CreateExpressionDefinitionVisitor<VAL, ED> createCreateExpressionDefinitionVisitor();

    private List<ED> createExpressionDefinitions(CreateExpressionDefinitionVisitor.Node<ED> rootNode) {
        var list = new ArrayList<ED>();

        Deque<CreateExpressionDefinitionVisitor.Node<ED>> stack = new LinkedList<>();
        stack.push(rootNode);
        while (!stack.isEmpty()) {
            var curNode = stack.pop();
            if (curNode.parent == null) {
                list.add(curNode.expressionDefinition.copyWith("/"));
            } else {
                var path = findPath(curNode);
                list.add(curNode.expressionDefinition.copyWith(path));
            }
            curNode.children.forEach(stack::push);
        }

        list.sort(Comparator.comparing(ExpressionDefinition::getHierarchyId));

        return list;
    }

    private String findPath(CreateExpressionDefinitionVisitor.Node<ED> node) {
        var list = new ArrayList<>();

        var curNode = node;
        while (curNode.parent != null) {
            var idx = curNode.parent.children.indexOf(curNode);
            list.add(idx + 1);
            curNode = curNode.parent;
        }

        Collections.reverse(list);
        return "/" + list.stream()
                .map(Object::toString)
                .collect(joining("/", "", "")) + "/";
    }
}

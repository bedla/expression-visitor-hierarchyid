package cz.bedla.hierarchyid.db;

import com.google.common.base.Splitter;
import cz.bedla.hierarchyid.expression.Expression;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toSet;

public abstract class DefinitionToExpressionConvertor<VAL, ED extends ExpressionDefinition<VAL, ED>, NODE extends ExpressionDefinitionNode<VAL, ED, NODE>> {
    public Expression convert(List<ED> definitions) {
        var root = createExpressionTree(definitions);
        return createCreateExpressionVisitor().visit(root);
    }

    protected abstract NODE createNode(int id, ED expressionDefinition);

    protected abstract CreateExpressionsVisitor<VAL, ED, NODE> createCreateExpressionVisitor();

    private NODE createExpressionTree(List<ED> list) {
        validateHierarchyIdUnique(list);

        var stack = new LinkedList<NODE>();

        var root = findAndRemoveRoot(list);
        stack.push(root);
        while (!stack.isEmpty()) {
            var node = stack.pop();
            var hierarchyId = node.getExpressionDefinition().getHierarchyId();
            var curLevel = findAndRemovePrefixedNodes(list, hierarchyId);
            validateIncrementalLevelIds(curLevel);
            curLevel.forEach(it -> {
                stack.push(it);
                node.getChildren().add(it);
            });
        }
        return root;
    }

    private void validateIncrementalLevelIds(List<NODE> list) {
        for (int i = 1; i <= list.size(); i++) {
            var cur = list.get(i - 1);
            if (cur.getId() != i) {
                throw new IllegalStateException("Levels does not have incremental ids");
            }
        }
    }

    private void validateHierarchyIdUnique(List<ED> list) {
        var ids = list.stream()
                .map(ExpressionDefinition::getHierarchyId)
                .collect(toSet());
        if (ids.size() != list.size()) {
            throw new IllegalStateException("HierarchyIds are not unique");
        }
    }

    private NODE findAndRemoveRoot(List<ED> list) {
        var roots = list.stream()
                .filter(it -> "/".equals(it.getHierarchyId()))
                .toList();
        if (roots.size() == 1) {
            var root = roots.get(0);
            list.removeIf(it -> it.getHierarchyId().equals(root.getHierarchyId()));
            return createNode(0, root);
        } else {
            throw new IllegalStateException("Invalid number of roots");
        }
    }

    private List<NODE> findAndRemovePrefixedNodes(List<ED> list, String hierarchyIdPrefix) {
        var prefixArray = splitHierarchyId(hierarchyIdPrefix);

        var result = list.stream()
                .filter(it -> {
                    var curArray = splitHierarchyId(it.getHierarchyId());
                    return curArray.length == prefixArray.length + 1;
                })
                .map(it -> {
                    var array = splitHierarchyId(it.getHierarchyId());
                    var id = Integer.parseInt(array[array.length - 1]);
                    return createNode(id, it);
                })
                .sorted(Comparator.comparing(ExpressionDefinitionNode::getId))
                .toList();

        var toRemove = result.stream()
                .map(it -> it.getExpressionDefinition().getHierarchyId())
                .collect(toSet());
        list.removeIf(it -> toRemove.contains(it.getHierarchyId()));

        return result;
    }

    private String[] splitHierarchyId(String hierarchyId) {
        return Splitter.on('/')
                .omitEmptyStrings()
                .splitToStream(hierarchyId)
                .toArray(String[]::new);
    }
}

package cz.bedla.hierarchyid.db;

import com.google.common.base.Splitter;
import cz.bedla.hierarchyid.expression.Expression;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toSet;

public class DefinitionToExpressionConvertor {
    public Expression convert(List<ExpressionDefinition> definitions) {
        var root = createExpressionTree(definitions);
        var expression = new CreateExpressionsVisitor().visit(root);
        return expression;
    }

    private ExpressionDefinitionNode createExpressionTree(List<ExpressionDefinition> list) {
        validateHierarchyIdUnique(list);

        var stack = new LinkedList<ExpressionDefinitionNode>();

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

    private void validateIncrementalLevelIds(List<ExpressionDefinitionNode> list) {
        for (int i = 1; i <= list.size(); i++) {
            var cur = list.get(i - 1);
            if (cur.getId() != i) {
                throw new IllegalStateException("Levels does not have incremental ids");
            }
        }
    }

    private void validateHierarchyIdUnique(List<ExpressionDefinition> list) {
        var ids = list.stream()
                .map(ExpressionDefinition::getHierarchyId)
                .collect(toSet());
        if (ids.size() != list.size()) {
            throw new IllegalStateException("HierarchyIds are not unique");
        }
    }

    private ExpressionDefinitionNode findAndRemoveRoot(List<ExpressionDefinition> list) {
        var roots = list.stream()
                .filter(it -> "/".equals(it.getHierarchyId()))
                .toList();
        if (roots.size() == 1) {
            var root = roots.get(0);
            list.removeIf(it -> it.getHierarchyId().equals(root.getHierarchyId()));
            return new ExpressionDefinitionNode(0, root);
        } else {
            throw new IllegalStateException("Invalid number of roots");
        }
    }

    private List<ExpressionDefinitionNode> findAndRemovePrefixedNodes(List<ExpressionDefinition> list, String hierarchyIdPrefix) {
        var prefixArray = splitHierarchyId(hierarchyIdPrefix);

        var result = list.stream()
                .filter(it -> {
                    var curArray = splitHierarchyId(it.getHierarchyId());
                    return curArray.length == prefixArray.length + 1;
                })
                .map(it -> {
                    var array = splitHierarchyId(it.getHierarchyId());
                    var id = Integer.parseInt(array[array.length - 1]);
                    return new ExpressionDefinitionNode(id, it);
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

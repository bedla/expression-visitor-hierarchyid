package cz.bedla.hierarchyid.rest;

import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.visitor.FlattenExpressionVisitor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;


public abstract class ExpressionToRestDtoConverter<DTO> {
    public List<DTO> convert(Expression expression) {
        requireNonNull(expression, "expression cannot be null");

        var flattenVisitor = new FlattenExpressionVisitor();
        flattenVisitor.visit(expression);
        var flatten = flattenVisitor.getFlatten();

        var idLookupArray = createIdLookupArray(flatten);
        var indexValue = inverseMap(flattenVisitor.getIndex());

        return createDtoListFromFlatten(flatten, idLookupArray, indexValue);
    }

    private List<DTO> createDtoListFromFlatten(List<String> flatten, List<Integer> idLookupArray, Map<String, Object> indexValue) {
        var dontLookBehindIdx = -1;

        var result = new ArrayList<DTO>();
        for (int idIdx : idLookupArray) {
            var behind = lookBehind(flatten, idIdx, dontLookBehindIdx);
            var ahead = lookAhead(flatten, idIdx, dontLookBehindIdx);

            var item = findItemById(indexValue, flatten.get(idIdx));

            result.add(createExpressionDto(leftOperator(behind), item, rightOperator(ahead)));

            // idx of ID plus idx of OPERATOR
            dontLookBehindIdx = idIdx + 1;
        }
        return result;
    }

    protected abstract DTO createExpressionDto(LeftOperator leftOperator, Object item, RightOperator rightOperator);

    private List<Integer> createIdLookupArray(List<String> flatten) {
        var idIndexArray = new ArrayList<Integer>();
        for (int i = 0; i < flatten.size(); i++) {
            var str = flatten.get(i);
            if (str.startsWith("[") && str.endsWith("]")) {
                idIndexArray.add(i);
            }
        }
        return idIndexArray;
    }

    private Map<String, Object> inverseMap(Map<Object, String> map) {
        if (new HashSet<>(map.values()).size() != map.size()) {
            throw new IllegalStateException("Values in map are not unique: " + map.values());
        }

        return map.entrySet().stream()
                .collect(toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private Object findItemById(Map<String, Object> index, String id) {
        var key = StringUtils.removeEnd(StringUtils.removeStart(id, "["), "]");
        var item = index.get(key);
        if (item == null) {
            throw new IllegalStateException("Unable to find key=" + key + " in map: " + index);
        }
        return item;
    }

    private LeftOperator leftOperator(String behind) {
        return "NOT".equals(behind) ? LeftOperator.NOT : null;
    }

    private RightOperator rightOperator(String value) {
        if ("AND".equals(value)) {
            return RightOperator.AND;
        } else if ("OR".equals(value)) {
            return RightOperator.OR;
        } else {
            return null;
        }
    }

    private String lookBehind(List<String> flatten, int idIdx, int dontLookBehindIdx) {
        var behindIdx = idIdx - 1;
        return lookAtOperator(flatten, behindIdx, dontLookBehindIdx);
    }

    private String lookAhead(List<String> flatten, int idIdx, int dontLookBehindIdx) {
        var aheadIdx = idIdx + 1;
        return lookAtOperator(flatten, aheadIdx, dontLookBehindIdx);
    }

    private String lookAtOperator(List<String> flatten, int idx, int dontLookBehindIdx) {
        if (idx > dontLookBehindIdx && idx < flatten.size()) {
            var value = flatten.get(idx);
            return value.startsWith("[") && value.endsWith("]") ? null : value;
        } else {
            return null;
        }
    }
}

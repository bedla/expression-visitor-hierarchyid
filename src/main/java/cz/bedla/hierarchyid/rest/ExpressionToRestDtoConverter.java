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


public abstract class ExpressionToRestDtoConverter<DTO, TERM_V> {
    public List<DTO> convert(Expression expression) {
        requireNonNull(expression, "expression cannot be null");

        var flattenVisitor = createFlattenExpressionVisitor();
        flattenVisitor.visit(expression);
        var flatten = flattenVisitor.getFlatten();

        var idIndexList = createIdIndex(flatten);
        var indexValue = inverseMap(flattenVisitor.getIndex());

        return createDtoListFromFlatten(flatten, idIndexList, indexValue);
    }

    protected abstract FlattenExpressionVisitor<TERM_V> createFlattenExpressionVisitor();

    private List<DTO> createDtoListFromFlatten(List<String> flatten, List<Integer> idIndexList, Map<String, TERM_V> indexValue) {
        var dontLookBehindIdx = -1;

        var result = new ArrayList<DTO>();
        for (int idIdx : idIndexList) {
            var behind = lookBehind(flatten, idIdx, dontLookBehindIdx);
            var ahead = lookAhead(flatten, idIdx, dontLookBehindIdx);

            var item = findItemById(indexValue, flatten.get(idIdx));

            result.add(createExpressionDto(leftOperator(behind), item, rightOperator(ahead)));

            // idx of ID and idx of OPERATOR
            dontLookBehindIdx = idIdx + 1;
        }
        return result;
    }

    protected abstract DTO createExpressionDto(LeftOperator leftOperator, TERM_V item, RightOperator rightOperator);

    private List<Integer> createIdIndex(List<String> flatten) {
        var idIndexArray = new ArrayList<Integer>();
        for (int i = 0; i < flatten.size(); i++) {
            var str = flatten.get(i);
            if (str.startsWith("[") && str.endsWith("]")) {
                idIndexArray.add(i);
            }
        }
        return idIndexArray;
    }

    private Map<String, TERM_V> inverseMap(Map<TERM_V, String> map) {
        if (new HashSet<>(map.values()).size() != map.size()) {
            throw new IllegalStateException("Values in map are not unique: " + map.values());
        }

        return map.entrySet().stream()
                .collect(toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private TERM_V findItemById(Map<String, TERM_V> index, String id) {
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

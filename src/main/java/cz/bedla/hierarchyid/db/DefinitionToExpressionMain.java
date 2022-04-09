package cz.bedla.hierarchyid.db;

import java.util.ArrayList;
import java.util.List;

public class DefinitionToExpressionMain {
    public static void main(String[] args) {
        var list = new ArrayList<>(List.of(
                new ExpressionDefinition("/", ExpressionDefinition.Type.OPERATOR, ExpressionDefinition.Operator.AND, null),
                new ExpressionDefinition("/1/", ExpressionDefinition.Type.VALUE, null, true),
                new ExpressionDefinition("/2/", ExpressionDefinition.Type.OPERATOR, ExpressionDefinition.Operator.OR, null),
                new ExpressionDefinition("/2/1/", ExpressionDefinition.Type.VALUE, null, true),
                new ExpressionDefinition("/2/2/", ExpressionDefinition.Type.VALUE, null, false),
                new ExpressionDefinition("/2/3/", ExpressionDefinition.Type.VALUE, null, true)
        ));

        var expression = new DefinitionToExpressionConvertor().convert(list);
        System.out.println(expression);

        var expressionDefinitions = new ExpressionToDefinitionConverter().convert(expression);
        for (ExpressionDefinition item : expressionDefinitions) {
            System.out.println(item);
        }
    }
}



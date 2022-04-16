package cz.bedla.hierarchyid;

import cz.bedla.hierarchyid.db.BooleanDefinitionToExpressionConvertor;
import cz.bedla.hierarchyid.db.BooleanExpressionDefinition;
import cz.bedla.hierarchyid.db.BooleanExpressionToDefinitionConverter;
import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;

import java.util.ArrayList;
import java.util.List;

public class BooleanDefinitionToExpressionMain {
    public static void main(String[] args) {
        var list = new ArrayList<>(List.of(
                new BooleanExpressionDefinition("/", ExpressionDefinitionType.LOGICAL_OPERATOR, LogicalOperator.AND, null),
                new BooleanExpressionDefinition("/1/", ExpressionDefinitionType.VALUE, null, true),
                new BooleanExpressionDefinition("/2/", ExpressionDefinitionType.LOGICAL_OPERATOR, LogicalOperator.OR, null),
                new BooleanExpressionDefinition("/2/1/", ExpressionDefinitionType.VALUE, null, true),
                new BooleanExpressionDefinition("/2/2/", ExpressionDefinitionType.VALUE, null, false),
                new BooleanExpressionDefinition("/2/3/", ExpressionDefinitionType.VALUE, null, true)
        ));

        var expression = new BooleanDefinitionToExpressionConvertor().convert(list);
        System.out.println(expression);

        var expressionDefinitions = new BooleanExpressionToDefinitionConverter().convert(expression);
        for (BooleanExpressionDefinition item : expressionDefinitions) {
            System.out.println(item);
        }
    }
}



package cz.bedla.hierarchyid.samples;

import cz.bedla.hierarchyid.db.ExpressionDefinitionType;
import cz.bedla.hierarchyid.db.LogicalOperator;
import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.db.fact.FactDefinitionToExpressionConvertor;
import cz.bedla.hierarchyid.db.fact.FactExpressionDefinition;
import cz.bedla.hierarchyid.db.fact.FactExpressionToDefinitionConverter;

import java.util.ArrayList;
import java.util.List;

public class FactDefinitionToExpressionMain {
    public static void main(String[] args) {
        var list = new ArrayList<>(List.of(
                new FactExpressionDefinition("/", ExpressionDefinitionType.LOGICAL_OPERATOR, LogicalOperator.AND, null),
                new FactExpressionDefinition("/1/", ExpressionDefinitionType.VALUE, null, new Fact("a1", Fact.Operator.EQ, 123)),
                new FactExpressionDefinition("/2/", ExpressionDefinitionType.LOGICAL_OPERATOR, LogicalOperator.OR, null),
                new FactExpressionDefinition("/2/1/", ExpressionDefinitionType.VALUE, null, new Fact("a2", Fact.Operator.NOT_EQ, 456)),
                new FactExpressionDefinition("/2/2/", ExpressionDefinitionType.VALUE, null, new Fact("a3", Fact.Operator.LT, 789)),
                new FactExpressionDefinition("/2/3/", ExpressionDefinitionType.VALUE, null, new Fact("a4", Fact.Operator.GTE, 111))
        ));

        var expression = new FactDefinitionToExpressionConvertor().convert(list);
        System.out.println(expression);

        var expressionDefinitions = new FactExpressionToDefinitionConverter().convert(expression);
        for (FactExpressionDefinition item : expressionDefinitions) {
            System.out.println(item);
        }
    }
}



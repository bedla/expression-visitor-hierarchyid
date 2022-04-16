package cz.bedla.hierarchyid;

import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.bool.BooleanExpression;
import cz.bedla.hierarchyid.expression.toolvisitors.EvalVisitor;
import cz.bedla.hierarchyid.expression.Expression;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.toolvisitors.PrintVisitor;

public class ExpressionEvalMainApplication {
    public static void main(String[] args) {
        evalExpression(new BooleanExpression(true));
        evalExpression(new BooleanExpression(false));
        evalExpression(new NotExpression(new BooleanExpression(true)));
        evalExpression(new NotExpression(new BooleanExpression(false)));
        evalExpression(new OrExpression(new BooleanExpression(false)));
        evalExpression(new OrExpression(new BooleanExpression(false), new BooleanExpression(true)));
        evalExpression(new AndExpression(new BooleanExpression(false)));
        evalExpression(new AndExpression(new BooleanExpression(false), new BooleanExpression(true)));
        evalExpression(new OrExpression(
                new AndExpression(new BooleanExpression(false), new BooleanExpression(true)),
                new NotExpression(new BooleanExpression(true))));
        evalExpression(new OrExpression(
                new AndExpression(new BooleanExpression(true), new BooleanExpression(true)),
                new NotExpression(new BooleanExpression(false))));
    }

    private static void evalExpression(Expression expression) {
        var printVisitor = new PrintVisitor();
        var evalVisitor = new EvalVisitor();
        System.out.print(evalVisitor.visit(expression));
        System.out.print("\t = ");
        System.out.println(printVisitor.visit(expression));
    }
}


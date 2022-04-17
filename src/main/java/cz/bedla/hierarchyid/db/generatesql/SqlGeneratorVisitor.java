package cz.bedla.hierarchyid.db.generatesql;

import cz.bedla.hierarchyid.db.fact.Fact;
import cz.bedla.hierarchyid.expression.AndExpression;
import cz.bedla.hierarchyid.expression.ExpressionVisitor;
import cz.bedla.hierarchyid.expression.NotExpression;
import cz.bedla.hierarchyid.expression.OrExpression;
import cz.bedla.hierarchyid.expression.TerminalExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;

public class SqlGeneratorVisitor implements ExpressionVisitor<Expression, Fact> {
    @Override
    public Expression visit(TerminalExpression<Fact> expression) {
        var fact = expression.getValue();
        var columnName = fact.columnName();

        return switch (fact.operator()) {
            case EQ -> new EqualsTo(new Column(columnName), valueExpression(fact));
            case NOT_EQ -> new NotEqualsTo(new Column(columnName), valueExpression(fact));
            case LT -> new MinorThan()
                    .withLeftExpression(new Column(columnName))
                    .withRightExpression(valueExpression(fact));
            case LTE -> new MinorThanEquals()
                    .withLeftExpression(new Column(columnName))
                    .withRightExpression(valueExpression(fact));
            case GT -> new GreaterThan()
                    .withLeftExpression(new Column(columnName))
                    .withRightExpression(valueExpression(fact));
            case GTE -> new GreaterThanEquals()
                    .withLeftExpression(new Column(columnName))
                    .withRightExpression(valueExpression(fact));
        };
    }

    private Expression valueExpression(Fact fact) {
        var value = fact.value();
        if (value instanceof String s) {
            return new StringValue(s);
        } else if (value instanceof Integer i) {
            return new LongValue(i);
        } else {
            if (value != null) {
                throw new IllegalStateException("Unsupported value type: " + value);
            }
            return new NullValue();
        }
    }

    @Override
    public Expression visit(AndExpression expression) {
        var left = expression.getLeftExpression().accept(this);
        var right = expression.getRightExpression().accept(this);
        return new Parenthesis(
                new net.sf.jsqlparser.expression.operators.conditional.AndExpression(left, right)
        );
    }

    @Override
    public Expression visit(OrExpression expression) {
        var left = expression.getLeftExpression().accept(this);
        var right = expression.getRightExpression().accept(this);
        return new Parenthesis(
                new net.sf.jsqlparser.expression.operators.conditional.OrExpression(left, right)
        );
    }

    @Override
    public Expression visit(NotExpression expression) {
        return new net.sf.jsqlparser.expression.NotExpression(
                new Parenthesis(expression.getExpression().accept(this))
        );
    }
}

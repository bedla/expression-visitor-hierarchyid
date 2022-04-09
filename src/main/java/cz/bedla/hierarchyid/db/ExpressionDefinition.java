package cz.bedla.hierarchyid.db;

import java.util.Objects;

public class ExpressionDefinition {
    private final String hierarchyId;
    private final Type type;
    private final Operator operator;
    private final Boolean operand;

    public ExpressionDefinition(Type type, Operator operator, Boolean operand) {
        this("", type, operator, operand);
    }

    public ExpressionDefinition(String hierarchyId, Type type, Operator operator, Boolean operand) {
        this.hierarchyId = hierarchyId;
        this.type = type;
        this.operator = operator;
        this.operand = operand;
    }

    public String getHierarchyId() {
        return hierarchyId;
    }

    public Type getType() {
        return type;
    }

    public Operator getOperator() {
        return operator;
    }

    public Boolean getOperand() {
        return operand;
    }

    public ExpressionDefinition copyWith(String hierarchyId) {
        return new ExpressionDefinition(hierarchyId, this.type, this.operator, this.operand);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionDefinition that = (ExpressionDefinition) o;
        return Objects.equals(hierarchyId, that.hierarchyId) && type == that.type && operator == that.operator && Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hierarchyId, type, operator, operand);
    }

    @Override
    public String toString() {
        return "ExpressionDefinition{" +
                "hierarchyId='" + hierarchyId + '\'' +
                ", type=" + type +
                ", operator=" + operator +
                ", operand=" + operand +
                '}';
    }


    public enum Type {
        OPERATOR, VALUE
    }

    public enum Operator {
        AND, OR, NOT
    }
}

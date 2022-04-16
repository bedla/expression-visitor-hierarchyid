package cz.bedla.hierarchyid.db.fact;

public record Fact(String columnName, Operator operator, Object value) {
    public enum Operator {
        EQ, NOT_EQ, LT, LTE, GT, GTE
    }
}

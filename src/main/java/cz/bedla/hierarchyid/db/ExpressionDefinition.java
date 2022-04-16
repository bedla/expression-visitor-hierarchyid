package cz.bedla.hierarchyid.db;

import java.util.Objects;

public abstract class ExpressionDefinition<VAL, SELF extends ExpressionDefinition<VAL, SELF>> {
    private final String hierarchyId;
    private final ExpressionDefinitionType type;
    private final LogicalOperator logicalOperator;
    private final VAL value;

    public ExpressionDefinition(ExpressionDefinitionType type, LogicalOperator logicalOperator, VAL value) {
        this("", type, logicalOperator, value);
    }

    public ExpressionDefinition(String hierarchyId, ExpressionDefinitionType type, LogicalOperator logicalOperator, VAL value) {
        this.hierarchyId = hierarchyId;
        this.type = type;
        this.logicalOperator = logicalOperator;
        this.value = value;
    }

    public String getHierarchyId() {
        return hierarchyId;
    }

    public ExpressionDefinitionType getType() {
        return type;
    }

    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    public VAL getValue() {
        return value;
    }

    public SELF copyWith(String hierarchyId) {
        return create(hierarchyId, this.type, this.logicalOperator, this.value);
    }

    protected abstract SELF create(String hierarchyId, ExpressionDefinitionType type, LogicalOperator logicalOperator, VAL value);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionDefinition<?, ?> that = (ExpressionDefinition<?, ?>) o;
        return Objects.equals(hierarchyId, that.hierarchyId) && type == that.type && logicalOperator == that.logicalOperator && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hierarchyId, type, logicalOperator, value);
    }

    @Override
    public String toString() {
        return "ExpressionDefinition{" +
                "hierarchyId='" + hierarchyId + '\'' +
                ", type=" + type +
                ", logicalOperator=" + logicalOperator +
                ", value=" + value +
                '}';
    }
}

package oleg.savin.investment.order.searchcriteria;

public enum SortByField {
    CREATION_TIME("creationTime"),
    SUM("sum"),
    CLOSED_TIME("closedTime"),
    TYPE("type"),
    RESULT("result"),
    TICKER("ticker");
    private final String fieldName;

    SortByField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

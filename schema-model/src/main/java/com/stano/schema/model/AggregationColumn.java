package com.stano.schema.model;

public class AggregationColumn {
  private final AggregationType aggregationType;
  private final String sourceColumn;
  private final String destinationColumn;

  public AggregationColumn(AggregationType aggregationType, String sourceColumn, String destinationColumn) {
    this.aggregationType = aggregationType;
    this.sourceColumn = sourceColumn;
    this.destinationColumn = destinationColumn;
  }

  public AggregationType getAggregationType() {
    return aggregationType;
  }

  public String getSourceColumn() {
    return sourceColumn;
  }

  public String getDestinationColumn() {
    return destinationColumn;
  }
}

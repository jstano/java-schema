package com.stano.schema.model;

public class AggregationGroup {
  private final String source;
  private final String sourceDerivedFrom;
  private final String destination;

  public AggregationGroup(String source, String sourceDerivedFrom, String destination) {
    this.source = source;
    this.sourceDerivedFrom = sourceDerivedFrom;
    this.destination = destination;
  }

  public String getSource() {
    return source;
  }

  public String getDestination() {
    return destination;
  }

  public String getSourceDerivedFrom() {
    return sourceDerivedFrom;
  }
}

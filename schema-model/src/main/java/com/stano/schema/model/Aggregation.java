package com.stano.schema.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aggregation {
  private final String destinationTable;
  private final String dateColumn;
  private final String criteria;
  private final String timeStampColumn;
  private final AggregationFrequency aggregationFrequency;
  private final List<AggregationColumn> aggregationColumns;
  private final List<AggregationGroup> aggregationGroups;

  public Aggregation(String destinationTable,
                     String dateColumn,
                     String criteria,
                     String timeStampColumn,
                     AggregationFrequency aggregationFrequency,
                     List<AggregationColumn> aggregationColumns,
                     List<AggregationGroup> aggregationGroups) {
    this.destinationTable = destinationTable;
    this.dateColumn = dateColumn;
    this.criteria = criteria;
    this.timeStampColumn = timeStampColumn;
    this.aggregationFrequency = aggregationFrequency;
    this.aggregationColumns = Collections.unmodifiableList(new ArrayList<>(aggregationColumns));
    this.aggregationGroups = Collections.unmodifiableList(new ArrayList<>(aggregationGroups));
  }

  public String getDestinationTable() {
    return destinationTable;
  }

  public String getDateColumn() {
    return dateColumn;
  }

  public String getCriteria() {
    return criteria;
  }

  public String getTimeStampColumn() {
    return timeStampColumn;
  }

  public AggregationFrequency getAggregationFrequency() {
    return aggregationFrequency;
  }

  public List<AggregationGroup> getAggregationGroups() {
    return aggregationGroups;
  }

  public List<AggregationColumn> getAggregationColumns() {
    return aggregationColumns;
  }
}

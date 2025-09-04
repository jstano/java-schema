package com.stano.schema.model

import spock.lang.Specification

class AggregationSpec extends Specification {
  def "constructor should store values and expose unmodifiable lists"() {
    given:
    def cols = [
      new AggregationColumn(AggregationType.SUM, "amount", "total_amount"),
      new AggregationColumn(AggregationType.COUNT, "id", "count_id")
    ]
    def groups = [
      new AggregationGroup("country", null, "country"),
      new AggregationGroup("city", "country", "city")
    ]

    when:
    def agg = new Aggregation(
      "agg_sales",
      "sale_date",
      "status = 'CONFIRMED'",
      "ts_updated",
      AggregationFrequency.DAILY,
      cols,
      groups
    )

    then:
    agg.destinationTable == "agg_sales"
    agg.dateColumn == "sale_date"
    agg.criteria == "status = 'CONFIRMED'"
    agg.timeStampColumn == "ts_updated"
    agg.aggregationFrequency == AggregationFrequency.DAILY

    and: "returned lists are unmodifiable copies"
    agg.aggregationColumns.size() == 2
    agg.aggregationGroups.size() == 2
    agg.aggregationColumns[0].sourceColumn == "amount"
    agg.aggregationColumns[0].destinationColumn == "total_amount"
    agg.aggregationColumns[0].aggregationType == AggregationType.SUM
    agg.aggregationGroups[1].source == "city"
    agg.aggregationGroups[1].sourceDerivedFrom == "country"
    agg.aggregationGroups[1].destination == "city"

    when: "attempt to mutate provided input lists after construction"
    cols.clear()
    groups.clear()

    then: "internal state is not affected"
    agg.aggregationColumns.size() == 2
    agg.aggregationGroups.size() == 2

    when: "attempt to mutate returned lists"
    agg.aggregationColumns << new AggregationColumn(AggregationType.COUNT, "x", "y")

    then:
    thrown(UnsupportedOperationException)
  }
}

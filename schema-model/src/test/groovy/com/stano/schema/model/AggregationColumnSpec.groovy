package com.stano.schema.model

import spock.lang.Specification

class AggregationColumnSpec extends Specification {
  def "constructor should set fields and getters should return them"() {
    when:
    def col = new AggregationColumn(type, source, destination)

    then:
    col.aggregationType == type
    col.sourceColumn == source
    col.destinationColumn == destination

    where:
    type                    | source      | destination
    AggregationType.SUM     | "amount"   | "total_amount"
    AggregationType.COUNT   | "id"       | "count_id"
  }
}

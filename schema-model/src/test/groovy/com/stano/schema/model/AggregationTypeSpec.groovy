package com.stano.schema.model

import spock.lang.Specification

class AggregationTypeSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    AggregationType.values()*.name() == [
      'SUM', 'COUNT'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    AggregationType.valueOf(name) == constant
    constant.name() == name

    where:
    name     | constant
    'SUM'    | AggregationType.SUM
    'COUNT'  | AggregationType.COUNT
  }

  def "all enum values should be unique"() {
    given:
    def values = AggregationType.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "can be used in AggregationColumn without errors"() {
    when:
    def colSum = new AggregationColumn(AggregationType.SUM, "amount", "total_amount")
    def colCount = new AggregationColumn(AggregationType.COUNT, "id", "cnt")

    then:
    colSum.aggregationType == AggregationType.SUM
    colCount.aggregationType == AggregationType.COUNT
  }
}

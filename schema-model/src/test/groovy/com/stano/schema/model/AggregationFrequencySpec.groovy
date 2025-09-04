package com.stano.schema.model

import spock.lang.Specification

class AggregationFrequencySpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    AggregationFrequency.values()*.name() == [
      'DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    AggregationFrequency.valueOf(name) == constant
    constant.name() == name

    where:
    name       | constant
    'DAILY'    | AggregationFrequency.DAILY
    'WEEKLY'   | AggregationFrequency.WEEKLY
    'MONTHLY'  | AggregationFrequency.MONTHLY
    'YEARLY'   | AggregationFrequency.YEARLY
  }

  def "all enum values should be unique"() {
    given:
    def values = AggregationFrequency.values()

    expect:
    new HashSet(values as List).size() == values.length
  }
}

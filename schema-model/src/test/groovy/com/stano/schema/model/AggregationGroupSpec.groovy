package com.stano.schema.model

import spock.lang.Specification

class AggregationGroupSpec extends Specification {
  def "constructor should set fields and getters should return them (with and without derived source)"() {
    when:
    def group = new AggregationGroup(source, sourceDerivedFrom, destination)

    then:
    group.source == source
    group.sourceDerivedFrom == sourceDerivedFrom
    group.destination == destination

    where:
    source     | sourceDerivedFrom | destination
    "country" | null              | "country"
    "city"    | "country"        | "city"
  }
}

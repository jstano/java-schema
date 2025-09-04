package com.stano.schema.model

import spock.lang.Specification

class VerifyTypeSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    VerifyType.values()*.name() == [
      'DATE', 'SUM', 'GROUP_BY'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    VerifyType.valueOf(name) == constant
    constant.name() == name

    where:
    name        | constant
    'DATE'      | VerifyType.DATE
    'SUM'       | VerifyType.SUM
    'GROUP_BY'  | VerifyType.GROUP_BY
  }

  def "all enum values should be unique"() {
    given:
    def values = VerifyType.values()

    expect:
    new HashSet(values as List).size() == values.length
  }
}

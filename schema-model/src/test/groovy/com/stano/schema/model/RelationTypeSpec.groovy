package com.stano.schema.model

import spock.lang.Specification

class RelationTypeSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    RelationType.values()*.name() == [
      'CASCADE', 'ENFORCE', 'SETNULL', 'DONOTHING'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    RelationType.valueOf(name) == constant
    constant.name() == name

    where:
    name        | constant
    'CASCADE'   | RelationType.CASCADE
    'ENFORCE'   | RelationType.ENFORCE
    'SETNULL'   | RelationType.SETNULL
    'DONOTHING' | RelationType.DONOTHING
  }

  def "all enum values should be unique"() {
    given:
    def values = RelationType.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "can be used in Relation without errors"() {
    when:
    def rel1 = new Relation("orders", "user_id", "users", "id", RelationType.CASCADE, false)
    def rel2 = new Relation("orders", "user_id", "users", "id", RelationType.SETNULL, true)

    then:
    rel1.type == RelationType.CASCADE
    !rel1.disableUsageChecking
    rel2.type == RelationType.SETNULL
    rel2.disableUsageChecking
  }
}

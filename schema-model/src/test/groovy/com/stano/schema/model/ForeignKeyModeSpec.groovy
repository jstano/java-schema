package com.stano.schema.model

import spock.lang.Specification

class ForeignKeyModeSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    ForeignKeyMode.values()*.name() == [
      'NONE', 'RELATIONS', 'TRIGGERS'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    ForeignKeyMode.valueOf(name) == constant
    constant.name() == name

    where:
    name         | constant
    'NONE'       | ForeignKeyMode.NONE
    'RELATIONS'  | ForeignKeyMode.RELATIONS
    'TRIGGERS'   | ForeignKeyMode.TRIGGERS
  }

  def "all enum values should be unique"() {
    given:
    def values = ForeignKeyMode.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "Schema should store and return ForeignKeyMode via getter/setter"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))

    when:
    schema.setForeignKeyMode(ForeignKeyMode.RELATIONS)

    then:
    schema.getForeignKeyMode() == ForeignKeyMode.RELATIONS

    when:
    schema.setForeignKeyMode(ForeignKeyMode.TRIGGERS)

    then:
    schema.getForeignKeyMode() == ForeignKeyMode.TRIGGERS
  }
}

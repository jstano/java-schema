package com.stano.schema.model

import spock.lang.Specification

class TriggerTypeSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    TriggerType.values()*.name() == [
      'UPDATE', 'DELETE'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    TriggerType.valueOf(name) == constant
    constant.name() == name

    where:
    name      | constant
    'UPDATE'  | TriggerType.UPDATE
    'DELETE'  | TriggerType.DELETE
  }

  def "all enum values should be unique"() {
    given:
    def values = TriggerType.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "can be used in Trigger without errors"() {
    when:
    def tUpd = new Trigger("AFTER UPDATE ON t", TriggerType.UPDATE, DatabaseType.PGSQL)
    def tDel = new Trigger("BEFORE DELETE ON t", TriggerType.DELETE, DatabaseType.MSSQL)

    then:
    tUpd.triggerType == TriggerType.UPDATE
    tDel.triggerType == TriggerType.DELETE
  }
}

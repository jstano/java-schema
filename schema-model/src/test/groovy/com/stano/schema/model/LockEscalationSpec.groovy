package com.stano.schema.model

import spock.lang.Specification

class LockEscalationSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    LockEscalation.values()*.name() == [
      'DISABLE', 'AUTO', 'TABLE'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    LockEscalation.valueOf(name) == constant
    constant.name() == name

    where:
    name       | constant
    'DISABLE'  | LockEscalation.DISABLE
    'AUTO'     | LockEscalation.AUTO
    'TABLE'    | LockEscalation.TABLE
  }

  def "all enum values should be unique"() {
    given:
    def values = LockEscalation.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "Table should store and return LockEscalation via getter"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))

    when:
    def tblDisable = new Table(schema, "public", "t1", null, LockEscalation.DISABLE, false)
    def tblAuto = new Table(schema, "public", "t2", "exported", LockEscalation.AUTO, true)
    def tblTable = new Table(schema, "public", "t3", null, LockEscalation.TABLE, false)

    then:
    tblDisable.lockEscalation == LockEscalation.DISABLE
    tblAuto.lockEscalation == LockEscalation.AUTO
    tblTable.lockEscalation == LockEscalation.TABLE

    and: "other constructor args are preserved as well"
    tblAuto.schemaName == 'public'
    tblAuto.name == 't2'
    tblAuto.exportDateColumn == 'exported'
    tblAuto.noExport
  }
}

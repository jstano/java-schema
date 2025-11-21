package com.stano.schema.model

import spock.lang.Specification

class BooleanModeSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    BooleanMode.values()*.name() == [
      'NATIVE', 'YES_NO', 'YN'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    BooleanMode.valueOf(name) == constant
    constant.name() == name

    where:
    name      | constant
    'NATIVE'  | BooleanMode.NATIVE
    'YES_NO'  | BooleanMode.YES_NO
    'YN'      | BooleanMode.YN
  }

  def "all enum values should be unique"() {
    given:
    def values = BooleanMode.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "Column.needsCheckConstraints should depend on BooleanMode for BOOLEAN type"() {
    given: "a BOOLEAN column without other constraints"
    def col = new Column("flag", ColumnType.BOOLEAN, 0, false)

    expect:
    !col.needsCheckConstraints(BooleanMode.NATIVE)
    col.needsCheckConstraints(BooleanMode.YES_NO)
    col.needsCheckConstraints(BooleanMode.YN)
  }

  def "Column.needsCheckConstraints true when explicit constraints regardless of BooleanMode"() {
    given: "a column with explicit check constraint"
    def colWithCheck = new Column("x", ColumnType.INT, 0, false, "x > 0")

    and: "a column with min/max"
    def colWithAll = new Column(
      "y",
      ColumnType.DECIMAL,
      10,
      2,
      false,
      null,
      null,
      null,
      "0",
      "100",
      null,
      null,
      false
    )

    expect:
    colWithCheck.needsCheckConstraints(BooleanMode.NATIVE)
    colWithCheck.needsCheckConstraints(BooleanMode.YES_NO)
    colWithAll.needsCheckConstraints(BooleanMode.NATIVE)
    colWithAll.needsCheckConstraints(BooleanMode.YN)
  }
}

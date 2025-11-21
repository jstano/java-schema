package com.stano.schema.model

import spock.lang.Specification

class ColumnSpec extends Specification {
  def "constructor (name,type,length,required) should set fields and defaults"() {
    when:
    def col = new Column("code", ColumnType.VARCHAR, 50, true)

    then:
    col.name == "code"
    col.type == ColumnType.VARCHAR
    col.length == 50
    col.scale == 0
    col.required
    col.checkConstraint == null
    col.defaultConstraint == null
    col.generated == null
    col.minValue == null
    col.maxValue == null
    col.enumType == null
    col.elementType == null
    !col.ignoreCase

    and: "no constraints means no checks (and not BOOLEAN)"
    !col.needsCheckConstraints(BooleanMode.NATIVE)
    !col.needsCheckConstraints(BooleanMode.YES_NO)
  }

  def "constructor with checkConstraint should set it and other defaults"() {
    when:
    def col = new Column("amount", ColumnType.INT, 0, false, "amount > 0")

    then:
    !col.required
    col.checkConstraint == "amount > 0"
    col.defaultConstraint == null
    col.generated == null
    col.minValue == null
    col.maxValue == null
    col.enumType == null
    col.elementType == null
    !col.ignoreCase

    and:
    col.needsCheckConstraints(BooleanMode.NATIVE)
  }

  def "full constructor should assign all fields correctly"() {
    when:
    def col = new Column(
      "prices",
      ColumnType.ARRAY,
      0,
      4,
      false,
      "json_valid(prices)",
      "DEFAULT '[]'",
      null,
      null,
      null,
      null,
      ColumnType.DECIMAL, // elementType for ARRAY
      true
    )

    then:
    col.name == "prices"
    col.type == ColumnType.ARRAY
    col.length == 0
    col.scale == 4
    !col.required
    col.checkConstraint == "json_valid(prices)"
    col.defaultConstraint == "DEFAULT '[]'"
    col.generated == null
    col.minValue == null
    col.maxValue == null
    col.enumType == null
    col.elementType == ColumnType.DECIMAL
    col.ignoreCase

    and: "explicit check constraint triggers checks"
    col.needsCheckConstraints(BooleanMode.NATIVE)
  }

  def "needsCheckConstraints for BOOLEAN depends on BooleanMode when no explicit constraints"() {
    given:
    def col = new Column("active", ColumnType.BOOLEAN, 0, false)

    expect:
    !col.needsCheckConstraints(BooleanMode.NATIVE)
    col.needsCheckConstraints(BooleanMode.YES_NO)
    col.needsCheckConstraints(BooleanMode.YN)
  }

  def "hasMinOrMaxValue reflects presence of bounds"() {
    expect:
    // no min/max in simple ctor
    !new Column("x", ColumnType.INT, 0, false).hasMinOrMaxValue()

    and:
    new Column(
      "y",
      ColumnType.DECIMAL,
      10,
      2,
      false,
      null,
      null,
      null,
      "0",
      null,
      null,
      null,
      false
    ).hasMinOrMaxValue()

    and:
    new Column(
      "z",
      ColumnType.DECIMAL,
      10,
      2,
      false,
      null,
      null,
      null,
      null,
      "100",
      null,
      null,
      false
    ).hasMinOrMaxValue()
  }

  def "Table integration: hasColumn is case-insensitive and getColumn caches names ignoring case"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "users", null, LockEscalation.AUTO, false)

    and:
    table.columns.addAll([
      new Column("Id", ColumnType.SEQUENCE, 0, true),
      new Column("userName", ColumnType.VARCHAR, 100, false)
    ])

    expect:
    table.hasColumn("id")
    table.hasColumn("ID")
    table.hasColumn("UserName")
    !table.hasColumn("missing")

    and: "getColumn should find by any case when map is populated lazily"
    table.getColumn("id").name == "Id"
    table.getColumn("USERNAME").name == "userName"
  }

  def "Table integration: getIdentityColumn returns first SEQUENCE or LONGSEQUENCE"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def t1 = new Table(schema, "public", "t1", null, LockEscalation.AUTO, false)

    and:
    t1.columns.addAll([
      new Column("code", ColumnType.VARCHAR, 20, false),
      new Column("id", ColumnType.SEQUENCE, 0, true),
      new Column("id2", ColumnType.LONGSEQUENCE, 0, true)
    ])

    expect:
    t1.getIdentityColumn().name == "id"

    when:
    def t2 = new Table(schema, "public", "t2", null, LockEscalation.AUTO, false)
    t2.columns.addAll([
      new Column("code", ColumnType.VARCHAR, 20, false),
      new Column("id2", ColumnType.LONGSEQUENCE, 0, true)
    ])

    then:
    t2.getIdentityColumn().name == "id2"

    when:
    def t3 = new Table(schema, "public", "t3", null, LockEscalation.AUTO, false)
    t3.columns.add(new Column("code", ColumnType.VARCHAR, 20, false))

    then:
    t3.getIdentityColumn() == null
  }

  def "Table integration: getColumnsWithCheckConstraints collects columns per BooleanMode"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "orders", null, LockEscalation.AUTO, false)

    and:
    def c1 = new Column("flag", ColumnType.BOOLEAN, 0, false) // only counts when mode != NATIVE
    def c2 = new Column("amt", ColumnType.INT, 0, false, "amt > 0") // always counts
    def c3 = new Column(
      "price",
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
    table.columns.addAll([c1, c2, c3])

    when:
    def nativeCols = table.getColumnsWithCheckConstraints(BooleanMode.NATIVE)
    def ynCols = table.getColumnsWithCheckConstraints(BooleanMode.YN)

    then:
    nativeCols*.name == ["amt", "price"]
    ynCols*.name == ["flag", "amt", "price"]
  }
}

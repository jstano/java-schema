package com.stano.schema.model

import spock.lang.Specification

class KeyTypeSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    KeyType.values()*.name() == [
      'PRIMARY', 'UNIQUE', 'INDEX'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    KeyType.valueOf(name) == constant
    constant.name() == name

    where:
    name       | constant
    'PRIMARY'  | KeyType.PRIMARY
    'UNIQUE'   | KeyType.UNIQUE
    'INDEX'    | KeyType.INDEX
  }

  def "all enum values should be unique"() {
    given:
    def values = KeyType.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "integration: Keys created with each KeyType reflect their type and Table.getPrimaryKey uses PRIMARY"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "customer", null, LockEscalation.AUTO, false)

    and: "columns"
    table.columns.addAll([
      new Column("id", ColumnType.SEQUENCE, 0, true),
      new Column("code", ColumnType.VARCHAR, 50, false)
    ])

    and: "keys of different types"
    def idx = new Key(KeyType.INDEX, [new KeyColumn("code")])
    def uq = new Key(KeyType.UNIQUE, [new KeyColumn("code")])
    def pk = new Key(KeyType.PRIMARY, [new KeyColumn("id")])

    when:
    table.keys.addAll([idx, uq, pk])

    then:
    idx.type == KeyType.INDEX
    uq.type == KeyType.UNIQUE
    pk.type == KeyType.PRIMARY

    and: "Table.getPrimaryKey returns the PRIMARY key when present"
    table.getPrimaryKey().is(pk)
  }
}

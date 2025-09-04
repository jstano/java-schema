package com.stano.schema.model

import spock.lang.Specification

class KeySpec extends Specification {
  def "constructor with all parameters stores values and exposes unmodifiable, copied columns list"() {
    given:
    def inputCols = [new KeyColumn("id"), new KeyColumn("tenant_id")]

    when:
    def key = new Key(KeyType.UNIQUE, inputCols, true, true, true, "included_col")

    then:
    key.type == KeyType.UNIQUE
    key.cluster
    key.compress
    key.unique
    key.include == "included_col"

    and: "returned columns reflect initial entries and are unmodifiable"
    key.columns*.name == ["id", "tenant_id"]

    when: "mutate the original list after construction"
    inputCols.clear()

    then: "key retains its own copy"
    key.columns*.name == ["id", "tenant_id"]

    when: "attempt to mutate the returned list"
    key.columns << new KeyColumn("x")

    then:
    thrown(UnsupportedOperationException)
  }

  def "secondary constructor defaults flags to false and include to null"() {
    when:
    def key = new Key(KeyType.INDEX, [new KeyColumn("a")])

    then:
    key.type == KeyType.INDEX
    !key.cluster
    !key.compress
    !key.unique
    key.include == null
    key.columns*.name == ["a"]
  }

  def "containsColumn should return true only when the exact column name exists (case-sensitive)"() {
    given:
    def key = new Key(KeyType.PRIMARY, [new KeyColumn("Id"), new KeyColumn("code")])

    expect:
    key.containsColumn(testName) == expected

    where:
    testName | expected
    "Id"    | true
    "code"  | true
    "id"    | false  // case-sensitive per implementation
    "missing" | false
  }

  def "getColumnsAsString should join column names with commas in order"() {
    given:
    def key = new Key(KeyType.PRIMARY, [new KeyColumn("id"), new KeyColumn("tenant_id"), new KeyColumn("code")])

    expect:
    key.getColumnsAsString() == "id,tenant_id,code"
  }

  def "Table integration: getPrimaryKey and getPrimaryKeyColumns work as expected"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "users", null, LockEscalation.AUTO, false)

    and: "add columns and keys"
    table.columns.addAll([
      new Column("id", ColumnType.SEQUENCE, 0, true),
      new Column("tenant_id", ColumnType.INT, 0, true),
      new Column("code", ColumnType.VARCHAR, 50, false)
    ])
    table.keys.add(new Key(KeyType.INDEX, [new KeyColumn("code")]))
    def pk = new Key(KeyType.PRIMARY, [new KeyColumn("id"), new KeyColumn("tenant_id")])
    table.keys.add(pk)

    expect: "getPrimaryKey returns the PRIMARY key and primary key columns are in order"
    table.getPrimaryKey().is(pk)
    table.getPrimaryKeyColumns() == ["id", "tenant_id"]

    when: "remove keys"
    table.keys.clear()

    then: "no primary key present"
    table.getPrimaryKey() == null
    table.getPrimaryKeyColumns() == null
  }
}

package com.stano.schema.model

import spock.lang.Specification

class InitialDataSpec extends Specification {
  def "constructor should set fields and getters should return them for various database types"() {
    when:
    def init = new InitialData(sql, dbType)

    then:
    init.sql == sql
    init.databaseType == dbType

    where:
    sql                                  | dbType
    "insert into t(a) values (1)"       | DatabaseType.POSTGRES
    "INSERT INTO t(a) VALUES (42);"     | DatabaseType.SQL_SERVER
    "REPLACE INTO t(a) VALUES (7);"     | DatabaseType.MYSQL
  }

  def "supports null sql value and still returns correct fields"() {
    when:
    def init = new InitialData(null, DatabaseType.H2)

    then:
    init.sql == null
    init.databaseType == DatabaseType.H2
  }

  def "Table.getInitialData exposes a live mutable list (current contract)"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "orders", null, LockEscalation.AUTO, false)

    and: "initially empty"
    assert table.initialData.isEmpty()

    when: "add entries via the returned list"
    table.initialData.add(new InitialData("insert into orders(id) values (1)", DatabaseType.POSTGRES))
    table.initialData.add(new InitialData("insert into orders(id) values (2)", DatabaseType.POSTGRES))

    then: "the table reflects those additions (live list)"
    table.initialData*.sql == [
      "insert into orders(id) values (1)",
      "insert into orders(id) values (2)"
    ]

    when: "mutate the list further"
    table.initialData.remove(0)

    then:
    table.initialData*.sql == ["insert into orders(id) values (2)"]
  }
}

package com.stano.schema.model

import spock.lang.Specification

class OtherSqlSpec extends Specification {
  def "constructor should set fields and getters should return them for various combinations"() {
    when:
    def other = new OtherSql(dbType, ord, sql)

    then:
    other.databaseType == dbType
    other.order == ord
    other.sql == sql

    where:
    dbType                  | ord                  | sql
    DatabaseType.POSTGRES   | OtherSqlOrder.TOP    | "CREATE EXTENSION IF NOT EXISTS uuid-ossp;"
    DatabaseType.SQL_SERVER | OtherSqlOrder.BOTTOM | "PRINT 'Done';"
    DatabaseType.MYSQL      | OtherSqlOrder.TOP    | "SET sql_safe_updates = 0;"
    DatabaseType.H2         | OtherSqlOrder.BOTTOM | "-- noop"
  }

  def "supports null SQL and still returns correct fields"() {
    when:
    def other = new OtherSql(DatabaseType.H2, OtherSqlOrder.TOP, null)

    then:
    other.databaseType == DatabaseType.H2
    other.order == OtherSqlOrder.TOP
    other.sql == null
  }

  def "Schema should collect OtherSql entries and expose an unmodifiable copy"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def a = new OtherSql(DatabaseType.POSTGRES, OtherSqlOrder.TOP, "A;")
    def b = new OtherSql(DatabaseType.POSTGRES, OtherSqlOrder.BOTTOM, "B;")

    when: "add entries one by one"
    schema.addOtherSql(a)
    schema.addOtherSql(b)

    then: "schema retains both in insertion order"
    schema.otherSql*.sql == ["A;", "B;"]

    when: "attempt to mutate returned list"
    schema.otherSql << new OtherSql(DatabaseType.MYSQL, OtherSqlOrder.TOP, "C;")

    then:
    thrown(UnsupportedOperationException)
  }
}

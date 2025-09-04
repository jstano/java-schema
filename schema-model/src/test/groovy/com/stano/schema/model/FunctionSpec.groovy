package com.stano.schema.model

import spock.lang.Specification

class FunctionSpec extends Specification {
  def "constructor should set fields and getters should return them for various database types"() {
    when:
    def fn = new Function(schemaName, name, dbType, sql)

    then:
    fn.schemaName == schemaName
    fn.name == name
    fn.databaseType == dbType
    fn.sql == sql

    where:
    schemaName | name          | dbType             | sql
    "public"  | "fn_total"    | DatabaseType.PGSQL | "create function fn_total() returns int as \$\$ select 1 \$\$;"
    "dbo"     | "fn_compute"  | DatabaseType.MSSQL | "CREATE FUNCTION dbo.fn_compute() RETURNS INT AS BEGIN RETURN 42 END"
    "app"     | "fn_format"   | DatabaseType.MYSQL | "CREATE FUNCTION fn_format() RETURNS INT RETURN 7;"
  }

  def "supports null SQL and still returns correct fields"() {
    when:
    def fn = new Function("util", "fn_empty", DatabaseType.H2, null)

    then:
    fn.schemaName == "util"
    fn.name == "fn_empty"
    fn.databaseType == DatabaseType.H2
    fn.sql == null
  }

  def "Schema should accept multiple functions and expose an unmodifiable copy"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def input = [
      new Function("public", "fn_a", DatabaseType.PGSQL, "sql a"),
      new Function("dbo", "fn_b", DatabaseType.MSSQL, "sql b")
    ]

    when: "add functions and then mutate the input list"
    schema.addFunctions(input)
    input.clear()

    then: "schema retains its own copy"
    schema.functions*.name == ["fn_a", "fn_b"]

    when: "attempt to mutate the returned functions list"
    schema.functions << new Function("x", "fn_c", DatabaseType.HSQL, "sql c")

    then:
    thrown(UnsupportedOperationException)
  }
}

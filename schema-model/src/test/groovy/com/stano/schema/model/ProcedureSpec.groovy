package com.stano.schema.model

import spock.lang.Specification

class ProcedureSpec extends Specification {
  def "constructor should set fields and getters should return them for various database types"() {
    when:
    def proc = new Procedure(schemaName, name, dbType, sql)

    then:
    proc.schemaName == schemaName
    proc.name == name
    proc.databaseType == dbType
    proc.sql == sql

    where:
    schemaName | name            | dbType             | sql
    "public"  | "pr_total"     | DatabaseType.PGSQL | "create procedure pr_total() language plpgsql as \$\$ begin /* noop */ end \$\$;"
    "dbo"     | "pr_compute"   | DatabaseType.MSSQL | "CREATE PROCEDURE dbo.pr_compute AS BEGIN SELECT 42 END"
    "app"     | "pr_cleanup"   | DatabaseType.MYSQL | "CREATE PROCEDURE pr_cleanup() BEGIN SELECT 1; END"
  }

  def "supports null SQL and still returns correct fields"() {
    when:
    def proc = new Procedure("util", "pr_empty", DatabaseType.H2, null)

    then:
    proc.schemaName == "util"
    proc.name == "pr_empty"
    proc.databaseType == DatabaseType.H2
    proc.sql == null
  }

  def "Schema should accept multiple procedures and expose an unmodifiable copy"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def input = [
      new Procedure("public", "pr_a", DatabaseType.PGSQL, "sql a"),
      new Procedure("dbo", "pr_b", DatabaseType.MSSQL, "sql b")
    ]

    when: "add procedures and then mutate the input list"
    schema.addProcedures(input)
    input.clear()

    then: "schema retains its own copy"
    schema.procedures*.name == ["pr_a", "pr_b"]

    when: "attempt to mutate the returned procedures list"
    schema.procedures << new Procedure("x", "pr_c", DatabaseType.HSQL, "sql c")

    then:
    thrown(UnsupportedOperationException)
  }
}

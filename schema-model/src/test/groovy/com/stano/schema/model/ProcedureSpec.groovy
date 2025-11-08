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
    schemaName | name         | dbType                  | sql
    "app"      | "pr_cleanup" | DatabaseType.MYSQL      | "CREATE PROCEDURE pr_cleanup() BEGIN SELECT 1; END"
    "public"   | "pr_total"   | DatabaseType.POSTGRES   | "create procedure pr_total() language plpgsql as \$\$ begin /* noop */ end \$\$;"
    "dbo"      | "pr_compute" | DatabaseType.SQL_SERVER | "CREATE PROCEDURE dbo.pr_compute AS BEGIN SELECT 42 END"
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
}

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
    schemaName | name         | dbType                  | sql
    "public"   | "fn_total"   | DatabaseType.POSTGRES   | "create function fn_total() returns int as \$\$ select 1 \$\$;"
    "dbo"      | "fn_compute" | DatabaseType.SQL_SERVER | "CREATE FUNCTION dbo.fn_compute() RETURNS INT AS BEGIN RETURN 42 END"
    "app"      | "fn_format"  | DatabaseType.MYSQL      | "CREATE FUNCTION fn_format() RETURNS INT RETURN 7;"
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
}

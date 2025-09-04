package com.stano.schema.model

import spock.lang.Specification

class ViewSpec extends Specification {
  def "constructor should set fields and getters should return them for various database types"() {
    when:
    def view = new View(schemaName, name, sql, dbType)

    then:
    view.schemaName == schemaName
    view.name == name
    view.sql == sql
    view.databaseType == dbType

    where:
    schemaName | name        | sql                                 | dbType
    "public"  | "v_orders"  | "select * from orders"              | DatabaseType.PGSQL
    "dbo"     | "v_users"   | "SELECT * FROM dbo.users"          | DatabaseType.MSSQL
    "app"     | "v_items"   | "SELECT * FROM items"              | DatabaseType.MYSQL
  }

  def "supports null SQL and still returns correct fields"() {
    when:
    def view = new View("util", "v_empty", null, DatabaseType.H2)

    then:
    view.schemaName == "util"
    view.name == "v_empty"
    view.sql == null
    view.databaseType == DatabaseType.H2
  }

  def "Schema.getViews should return DB-specific when present, otherwise generic"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))

    and: "views where some have DB-specific overrides"
    // For name conflicts (same view name), DB-specific should override generic for that DB.
    schema.addView(new View("public", "sales", "SELECT 1 -- generic", null))
    schema.addView(new View("public", "sales", "SELECT 1 -- pg", DatabaseType.PGSQL))
    schema.addView(new View("dbo",    "sales", "SELECT 1 -- mssql", DatabaseType.MSSQL))
    schema.addView(new View("public", "inventory", "SELECT 2 -- generic", null))

    when:
    def pgViews = schema.getViews(DatabaseType.PGSQL)
    def msViews = schema.getViews(DatabaseType.MSSQL)
    def h2Views = schema.getViews(DatabaseType.H2)

    then: "PG chooses the pg-specific variant for sales and generic for inventory"
    pgViews*.name == ["sales", "inventory"]
    pgViews*.sql == ["SELECT 1 -- pg", "SELECT 2 -- generic"]

    and: "MSSQL chooses mssql-specific for sales and generic for inventory"
    msViews*.name == ["sales", "inventory"]
    msViews*.sql == ["SELECT 1 -- mssql", "SELECT 2 -- generic"]

    and: "H2 has no specific overrides, so generic ones win"
    h2Views*.name == ["sales", "inventory"]
    h2Views*.sql == ["SELECT 1 -- generic", "SELECT 2 -- generic"]
  }

  def "Schema.getViews should treat view names case-insensitively and preserve distinct order"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))

    and: "insert views in a mixed case order with duplicates by case"
    schema.addView(new View("public", "AView", "A generic", null))
    schema.addView(new View("public", "bview", "B generic", null))
    // Later DB-specific with different case for the same logical name should override per DB
    schema.addView(new View("public", "aview", "A pg", DatabaseType.PGSQL))
    // Another distinct name later
    schema.addView(new View("public", "CView", "C generic", null))

    when:
    def pgViews = schema.getViews(DatabaseType.PGSQL)
    def mysqlViews = schema.getViews(DatabaseType.MYSQL)

    then: "Distinct order is by first appearance of each logical name in the original list: AView, bview, CView"
    pgViews*.name == ["aview", "bview", "CView"]
    mysqlViews*.name == ["AView", "bview", "CView"]

    and: "For PG, AView resolves to the DB-specific SQL; for MySQL, it resolves to generic"
    pgViews*.sql == ["A pg", "B generic", "C generic"]
    mysqlViews*.sql == ["A generic", "B generic", "C generic"]
  }

  def "Schema.getViews should return an unmodifiable list"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    schema.addView(new View("public", "only", "SELECT 1", null))

    when:
    schema.getViews(DatabaseType.PGSQL) << new View("public", "x", "y", null)

    then:
    thrown(UnsupportedOperationException)
  }
}

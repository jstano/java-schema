package com.stano.schema.model

import spock.lang.Specification

class SchemaSpec extends Specification {
  def "constructor sets URL and default modes; setters update version and modes"() {
    when:
    def url = new URL("https://example.com/schema.json")
    def schema = new Schema(url)

    then:
    schema.schemaURL == url
    schema.booleanMode == BooleanMode.NATIVE // default
    schema.version == null
    schema.foreignKeyMode == null

    when:
    def ver = new Version(1, 2, 3, true)
    schema.setVersion(ver)
    schema.setForeignKeyMode(ForeignKeyMode.RELATIONS)
    schema.setBooleanMode(BooleanMode.YN)

    then:
    schema.version.is(ver)
    schema.foreignKeyMode == ForeignKeyMode.RELATIONS
    schema.booleanMode == BooleanMode.YN
  }

  def "addTable populates tables and case-insensitive map; getTables unmodifiable; getTable throws when missing"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def tUsers = new Table(schema, "public", "Users", null, LockEscalation.AUTO, false)
    def tOrders = new Table(schema, "sales", "orders", null, LockEscalation.AUTO, false)

    when:
    schema.addTable(tUsers)
    schema.addTable(tOrders)

    then: "getTables returns unmodifiable view and preserves insertion order"
    schema.tables*.name == ["Users", "orders"]

    when: "attempt to mutate returned tables list"
    schema.tables << new Table(schema, "x", "y", null, LockEscalation.AUTO, false)

    then:
    thrown(UnsupportedOperationException)

    and: "getTable resolves case-insensitively from internal map"
    schema.getTable("users").is(tUsers)
    schema.getTable("ORDERS").is(tOrders)

    and: "getOptionalTable returns empty when not present"
    !schema.getOptionalTable("missing").isPresent()

    when: "getTable with unknown name throws with message"
    schema.getTable("missing")

    then:
    def ex = thrown(IllegalStateException)
    ex.message.contains("Unable to locate a table with the name 'missing'")
  }

  def "addEnumType stores by name; getEnumType returns, getEnumTypes exposes values; throws when missing"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def etColor = new EnumType("Color")
    etColor.addValue(new EnumValue("RED", "R"))
    def etStatus = new EnumType("Status")

    when:
    schema.addEnumType(etColor)
    schema.addEnumType(etStatus)

    then:
    schema.getEnumType("Color").is(etColor)
    schema.getEnumType("Status").is(etStatus)

    and: "getEnumTypes returns the collection of values (ordering not guaranteed)"
    new HashSet(schema.getEnumTypes() as List) == new HashSet([etColor, etStatus])

    when: "requesting a missing enum throws"
    schema.getEnumType("Missing")

    then:
    def ex = thrown(IllegalStateException)
    ex.message.contains("Unable to locate an enum type with name 'Missing'")
  }

  def "sortTablesByName orders tables lexicographically by getName()"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def t3 = new Table(schema, "public", "zeta", null, LockEscalation.AUTO, false)
    def t1 = new Table(schema, "public", "alpha", null, LockEscalation.AUTO, false)
    def t2 = new Table(schema, "public", "Beta", null, LockEscalation.AUTO, false)

    when:
    schema.addTable(t3)
    schema.addTable(t1)
    schema.addTable(t2)

    then: "initial order is insertion order"
    schema.tables*.name == ["zeta", "alpha", "Beta"]

    when: "sort by name using natural String order (case-sensitive, 'B' before 'a')"
    schema.sortTablesByName()

    then:
    schema.tables*.name == ["Beta", "alpha", "zeta"]
  }

  def "getViews returns unmodifiable list and preserves distinct-name order for simple case"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    schema.addView(new View("public", "A", "ga", null))
    schema.addView(new View("public", "a", "pg", DatabaseType.POSTGRES))
    schema.addView(new View("public", "B", "gb", null))

    when:
    def pgViews = schema.getViews(DatabaseType.POSTGRES)
    def h2Views = schema.getViews(DatabaseType.H2)

    then: "order by first appearance of logical names (A,B)"
    pgViews*.name == ["a", "B"]
    h2Views*.name == ["A", "B"]

    when: "lists are unmodifiable"
    pgViews << new View("public", "C", "x", null)

    then:
    thrown(UnsupportedOperationException)
  }

  def "getViews prefers DB-specific over generic for same logical name"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    schema.addView(new View("public", "sales", "generic", null))
    schema.addView(new View("public", "sales", "pgsql", DatabaseType.POSTGRES))
    schema.addView(new View("public", "sales", "mssql", DatabaseType.SQL_SERVER))

    when:
    def pg = schema.getViews(DatabaseType.POSTGRES)
    def ms = schema.getViews(DatabaseType.SQL_SERVER)
    def h2 = schema.getViews(DatabaseType.H2)

    then:
    pg*.sql == ["pgsql"]
    ms*.sql == ["mssql"]
    h2*.sql == ["generic"]
  }

  def "buildReverseRelations adds reverse on parent with disableUsageChecking=false (in SchemaSpec)"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def parent = new Table(schema, "public", "users", null, LockEscalation.AUTO, false)
    def child = new Table(schema, "public", "orders", null, LockEscalation.AUTO, false)
    parent.columns.add(new Column("id", ColumnType.SEQUENCE, 0, true))
    child.columns.add(new Column("user_id", ColumnType.INT, 0, false))
    child.relations.add(new Relation("orders", "user_id", "users", "id", RelationType.CASCADE, true))
    schema.addTable(parent)
    schema.addTable(child)

    when:
    schema.buildReverseRelations()

    then:
    parent.reverseRelations.size() == 1
    with(parent.reverseRelations[0]) {
      fromTableName == "users"
      fromColumnName == "id"
      toTableName == "orders"
      toColumnName == "user_id"
      type == RelationType.CASCADE
      !disableUsageChecking
    }
  }

  def "validate flags SETNULL on required from-column (in SchemaSpec)"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def parent = new Table(schema, "public", "parent", null, LockEscalation.AUTO, false)
    def childReq = new Table(schema, "public", "child_req", null, LockEscalation.AUTO, false)
    parent.columns.add(new Column("id", ColumnType.SEQUENCE, 0, true))
    childReq.columns.add(new Column("parent_id", ColumnType.INT, 0, true))
    childReq.relations.add(new Relation("child_req", "parent_id", "parent", "id", RelationType.SETNULL, false))
    schema.addTable(parent)
    schema.addTable(childReq)

    when:
    def errors = schema.validate()

    then:
    errors.size() == 1
    errors[0].contains("child_req.parent_id is required")
    errors[0].contains("relation specifies setnull")
  }
}

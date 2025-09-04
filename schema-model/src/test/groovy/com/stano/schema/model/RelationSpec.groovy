package com.stano.schema.model

import spock.lang.Specification

class RelationSpec extends Specification {
  def "constructor should set fields and getters should return them for different types and flags"() {
    when:
    def rel = new Relation(fromTable, fromColumn, toTable, toColumn, type, disable)

    then:
    rel.fromTableName == fromTable
    rel.fromColumnName == fromColumn
    rel.toTableName == toTable
    rel.toColumnName == toColumn
    rel.type == type
    rel.disableUsageChecking == disable

    where:
    fromTable | fromColumn | toTable  | toColumn   | type                 | disable
    "orders" | "user_id"  | "users"  | "id"       | RelationType.CASCADE | false
    "orders" | "user_id"  | "users"  | "id"       | RelationType.ENFORCE | true
    "orders" | "user_id"  | "users"  | "id"       | RelationType.SETNULL | false
    "orders" | "user_id"  | "users"  | "id"       | RelationType.DONOTHING | true
  }

  def "Schema.buildReverseRelations should add inverse relation entries with disableUsageChecking=false"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def parent = new Table(schema, "public", "users", null, LockEscalation.AUTO, false)
    def child = new Table(schema, "public", "orders", null, LockEscalation.AUTO, false)

    and: "columns on both tables"
    parent.columns.add(new Column("id", ColumnType.SEQUENCE, 0, true))
    child.columns.add(new Column("user_id", ColumnType.INT, 0, false))

    and: "forward relation from child.user_id -> parent.id"
    child.relations.add(new Relation("orders", "user_id", "users", "id", RelationType.CASCADE, true))

    and:
    schema.addTable(parent)
    schema.addTable(child)

    when:
    schema.buildReverseRelations()

    then: "reverse relation exists on parent"
    parent.reverseRelations.size() == 1
    with(parent.reverseRelations[0]) {
      fromTableName == "users"
      fromColumnName == "id"
      toTableName == "orders"
      toColumnName == "user_id"
      type == RelationType.CASCADE
      !disableUsageChecking // always false for reverse per implementation
    }
  }

  def "Schema.validate should flag SETNULL when from-column is required and ignore otherwise"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))

    and: "parent and child tables"
    def parent = new Table(schema, "public", "parent", null, LockEscalation.AUTO, false)
    def childRequired = new Table(schema, "public", "child_req", null, LockEscalation.AUTO, false)
    def childOptional = new Table(schema, "public", "child_opt", null, LockEscalation.AUTO, false)

    and: "columns"
    parent.columns.add(new Column("id", ColumnType.SEQUENCE, 0, true))
    childRequired.columns.add(new Column("parent_id", ColumnType.INT, 0, true))
    childOptional.columns.add(new Column("parent_id", ColumnType.INT, 0, false))

    and: "relations use SETNULL"
    childRequired.relations.add(new Relation("child_req", "parent_id", "parent", "id", RelationType.SETNULL, false))
    childOptional.relations.add(new Relation("child_opt", "parent_id", "parent", "id", RelationType.SETNULL, false))

    and:
    schema.addTable(parent)
    schema.addTable(childRequired)
    schema.addTable(childOptional)

    when:
    def errors = schema.validate()

    then: "one error for the required column relation"
    errors.size() == 1
    errors[0].contains("child_req.parent_id is required")
    errors[0].contains("relation specifies setnull")

    and: "no error for optional column relation"
    !errors.any { it.contains("child_opt.parent_id") }
  }
}

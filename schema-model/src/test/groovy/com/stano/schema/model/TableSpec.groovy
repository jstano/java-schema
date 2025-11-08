package com.stano.schema.model

import spock.lang.Specification

class TableSpec extends Specification {
  def "constructor should store fields and toString returns the table name"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))

    when:
    def table = new Table(schema, "public", "users", "exported_at", LockEscalation.AUTO, true)

    then:
    table.schema.is(schema)
    table.schemaName == "public"
    table.name == "users"
    table.exportDateColumn == "exported_at"
    table.lockEscalation == LockEscalation.AUTO
    table.noExport
    table.toString() == "users"
  }

  def "getColumn should populate lazy map and be case-insensitive; hasColumn mirrors that"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "accounts", null, LockEscalation.AUTO, false)

    and:
    table.columns.addAll([
      new Column("Id", ColumnType.SEQUENCE, 0, true),
      new Column("userName", ColumnType.VARCHAR, 50, false)
    ])

    expect:
    table.hasColumn("id")
    table.hasColumn("USERNAME")
    !table.hasColumn("missing")

    and: "getColumn works regardless of input case and returns original column"
    table.getColumn("ID").name == "Id"
    table.getColumn("username").name == "userName"
  }

  def "getIdentityColumn returns first SEQUENCE or LONGSEQUENCE if present"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))

    and: "table with SEQUENCE first"
    def t1 = new Table(schema, "public", "t1", null, LockEscalation.AUTO, false)
    t1.columns.addAll([
      new Column("code", ColumnType.VARCHAR, 20, false),
      new Column("id", ColumnType.SEQUENCE, 0, true),
      new Column("id2", ColumnType.LONGSEQUENCE, 0, true)
    ])

    and: "table with only LONGSEQUENCE"
    def t2 = new Table(schema, "public", "t2", null, LockEscalation.AUTO, false)
    t2.columns.addAll([
      new Column("code", ColumnType.VARCHAR, 20, false),
      new Column("id2", ColumnType.LONGSEQUENCE, 0, true)
    ])

    and: "table with no identity columns"
    def t3 = new Table(schema, "public", "t3", null, LockEscalation.AUTO, false)
    t3.columns.add(new Column("code", ColumnType.VARCHAR, 20, false))

    expect:
    t1.getIdentityColumn().name == "id"
    t2.getIdentityColumn().name == "id2"
    t3.getIdentityColumn() == null
  }

  def "getPrimaryKey and getPrimaryKeyColumns return correct info or null when absent"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "orders", null, LockEscalation.AUTO, false)

    and:
    table.columns.addAll([
      new Column("id", ColumnType.SEQUENCE, 0, true),
      new Column("tenant_id", ColumnType.INT, 0, true),
      new Column("code", ColumnType.VARCHAR, 50, false)
    ])

    and: "add a non-primary and then the primary key"
    table.keys.add(new Key(KeyType.INDEX, [new KeyColumn("code")]))
    def pk = new Key(KeyType.PRIMARY, [new KeyColumn("id"), new KeyColumn("tenant_id")])
    table.keys.add(pk)

    expect:
    table.getPrimaryKey().is(pk)
    table.getPrimaryKeyColumns() == ["id", "tenant_id"]

    when:
    table.keys.clear()

    then:
    table.getPrimaryKey() == null
    table.getPrimaryKeyColumns() == null
  }

  def "hasOption uses identity semantics against the live options list"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "items", null, LockEscalation.AUTO, false)

    expect:
    table.options.isEmpty()
    !table.hasOption(TableOption.DATA)

    when:
    table.options.add(TableOption.DATA)

    then:
    table.hasOption(TableOption.DATA)
    !table.hasOption(TableOption.NO_EXPORT)
  }

  def "hasColumnConstraints and getColumnsWithCheckConstraints depend on BooleanMode and explicit constraints"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "orders", null, LockEscalation.AUTO, false)

    and:
    def boolCol = new Column("flag", ColumnType.BOOLEAN, 0, false) // only when mode != NATIVE
    def checked = new Column("amt", ColumnType.INT, 0, false, "amt > 0") // always
    def ranged = new Column(
      "price",
      ColumnType.DECIMAL,
      10,
      2,
      false,
      null,
      null,
      null,
      "0",
      "100",
      null,
      null,
      true,
      false
    )
    table.columns.addAll([boolCol, checked, ranged])

    expect: "with NATIVE, only explicit constraints count"
    table.hasColumnConstraints(BooleanMode.NATIVE)
    table.getColumnsWithCheckConstraints(BooleanMode.NATIVE)*.name == ["amt", "price"]

    and: "with YN, boolean joins the list"
    table.hasColumnConstraints(BooleanMode.YN)
    table.getColumnsWithCheckConstraints(BooleanMode.YN)*.name == ["flag", "amt", "price"]
  }

  def "getColumnRelation should return matching relation by from-column name case-insensitively or null"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "child", null, LockEscalation.AUTO, false)

    and:
    table.columns.addAll([
      new Column("parent_id", ColumnType.INT, 0, false),
      new Column("other", ColumnType.VARCHAR, 10, false)
    ])

    and: "add relation using a specific case for from-column"
    table.relations.add(new Relation("child", "Parent_Id", "parent", "id", RelationType.CASCADE, false))

    expect:
    table.getColumnRelation(new Column("PARENT_id", ColumnType.INT, 0, false)).type == RelationType.CASCADE

    and:
    table.getColumnRelation(new Column("missing", ColumnType.INT, 0, false)) == null
  }

  def "getIndexes should expose a live mutable list and preserve insertion order"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "idx_test", null, LockEscalation.AUTO, false)

    and: "initially, no indexes"
    assert table.indexes.isEmpty()

    when: "add two INDEX keys via the returned list"
    def k1 = new Key(KeyType.INDEX, [new KeyColumn("code")])
    def k2 = new Key(KeyType.INDEX, [new KeyColumn("tenant_id"), new KeyColumn("code")])
    table.indexes.add(k1)
    table.indexes.add(k2)

    then: "table reflects additions and preserves order"
    table.indexes[0].is(k1)
    table.indexes[1].is(k2)

    when: "remove first index"
    table.indexes.remove(0)

    then: "only the second remains"
    table.indexes.size() == 1
    table.indexes[0].is(k2)
  }

  def "getConstraints should expose a live mutable list that reflects changes"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "con_test", null, LockEscalation.AUTO, false)

    and: "initially empty"
    assert table.constraints.isEmpty()

    when: "add constraints via the returned list"
    def c1 = new Constraint("ck_positive", "amount > 0", DatabaseType.POSTGRES)
    def c2 = new Constraint("ck_not_null", "col is not null", DatabaseType.SQL_SERVER)
    table.constraints.addAll([c1, c2])

    then:
    table.constraints*.name == ["ck_positive", "ck_not_null"]
    table.constraints*.sql == ["amount > 0", "col is not null"]

    when: "remove one"
    table.constraints.remove(0)

    then:
    table.constraints*.name == ["ck_not_null"]
  }

  def "getAggregations should expose a live list and store Aggregation instances correctly"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "agg_test", null, LockEscalation.AUTO, false)

    and: "build a simple aggregation"
    def cols = [new AggregationColumn(AggregationType.SUM, "amount", "total_amount")]
    def groups = [new AggregationGroup("country", null, "country")]
    def agg = new Aggregation("agg_sales", "sale_date", "status='CONFIRMED'", "updated_at", AggregationFrequency.MONTHLY, cols, groups)

    when:
    table.aggregations.add(agg)

    then:
    table.aggregations.size() == 1
    with(table.aggregations[0]) {
      destinationTable == "agg_sales"
      dateColumn == "sale_date"
      timeStampColumn == "updated_at"
      aggregationFrequency == AggregationFrequency.MONTHLY
      aggregationColumns.size() == 1
      aggregationColumns[0].aggregationType == AggregationType.SUM
      aggregationGroups*.destination == ["country"]
    }

    when: "remove aggregation"
    table.aggregations.remove(0)

    then:
    table.aggregations.isEmpty()
  }

  def "hasColumnConstraints returns false when no columns require constraints (false path)"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def t1 = new Table(schema, "public", "no_checks_native", null, LockEscalation.AUTO, false)
    def t2 = new Table(schema, "public", "no_checks_any", null, LockEscalation.AUTO, false)

    and: "t1 has a BOOLEAN and a VARCHAR but no explicit constraints; under NATIVE, BOOLEAN should not trigger"
    t1.columns.addAll([
      new Column("flag", ColumnType.BOOLEAN, 0, false),
      new Column("code", ColumnType.VARCHAR, 50, false)
    ])

    and: "t2 has only unconstrained non-BOOLEAN columns"
    t2.columns.addAll([
      new Column("a", ColumnType.INT, 0, false),
      new Column("b", ColumnType.VARCHAR, 10, false)
    ])

    expect:
    !t1.hasColumnConstraints(BooleanMode.NATIVE)
    !t2.hasColumnConstraints(BooleanMode.NATIVE)
    !t2.hasColumnConstraints(BooleanMode.YN)
  }


}

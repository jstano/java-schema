package com.stano.schema.model

import spock.lang.Specification

class TriggerSpec extends Specification {
  def "constructor should set fields and getters should return them for various types and DBs"() {
    when:
    def trg = new Trigger(text, ttype, db)

    then:
    trg.triggerText == text
    trg.triggerType == ttype
    trg.databaseType == db

    where:
    text                     | ttype              | db
    "AFTER UPDATE SET x=1"   | TriggerType.UPDATE | DatabaseType.POSTGRES
    "BEFORE DELETE FROM t"   | TriggerType.DELETE | DatabaseType.SQL_SERVER
    "CREATE TRIGGER t_upd"   | TriggerType.UPDATE | DatabaseType.MYSQL
    "DROP TRIGGER IF EXISTS" | TriggerType.DELETE | DatabaseType.H2
  }

  def "supports null triggerText and still returns correct fields"() {
    when:
    def trg = new Trigger(null, TriggerType.UPDATE, DatabaseType.H2)

    then:
    trg.triggerText == null
    trg.triggerType == TriggerType.UPDATE
    trg.databaseType == DatabaseType.H2
  }

  def "Table.getTriggers exposes a live mutable list (current contract)"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "orders", null, LockEscalation.AUTO, false)

    and: "initially empty"
    assert table.triggers.isEmpty()

    when: "add triggers via the returned list"
    table.triggers.add(new Trigger("AFTER UPDATE ON orders", TriggerType.UPDATE, DatabaseType.POSTGRES))
    table.triggers.add(new Trigger("BEFORE DELETE ON orders", TriggerType.DELETE, DatabaseType.POSTGRES))

    then: "the table reflects those additions (live list)"
    table.triggers*.triggerType == [TriggerType.UPDATE, TriggerType.DELETE]
    table.triggers*.databaseType == [DatabaseType.POSTGRES, DatabaseType.POSTGRES]

    when: "mutate the list further"
    table.triggers.remove(0)

    then:
    table.triggers*.triggerType == [TriggerType.DELETE]
  }
}

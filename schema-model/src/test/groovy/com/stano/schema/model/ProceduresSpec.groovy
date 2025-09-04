package com.stano.schema.model

import spock.lang.Specification

class ProceduresSpec extends Specification {
  def "constructor should store databaseType and wrap procedures list as unmodifiable copy"() {
    given:
    def input = [
      new Procedure("public", "pr_a", DatabaseType.PGSQL, "sql a"),
      new Procedure("dbo", "pr_b", DatabaseType.PGSQL, "sql b")
    ]

    when:
    def procs = new Procedures(DatabaseType.PGSQL, input)

    then:
    procs.databaseType == DatabaseType.PGSQL
    procs.procedures*.name == ["pr_a", "pr_b"]

    when: "mutate the input list after construction"
    input.clear()

    then: "internal list remains intact (copy-on-construct)"
    procs.procedures*.name == ["pr_a", "pr_b"]

    when: "attempt to mutate the returned list"
    procs.procedures << new Procedure("x", "pr_c", DatabaseType.PGSQL, "sql c")

    then:
    thrown(UnsupportedOperationException)
  }

  def "works with an empty list and still exposes unmodifiable list"() {
    when:
    def procs = new Procedures(DatabaseType.MSSQL, [])

    then:
    procs.databaseType == DatabaseType.MSSQL
    procs.procedures.isEmpty()

    when:
    procs.procedures << new Procedure("dbo", "pr_x", DatabaseType.MSSQL, "sql")

    then:
    thrown(UnsupportedOperationException)
  }
}

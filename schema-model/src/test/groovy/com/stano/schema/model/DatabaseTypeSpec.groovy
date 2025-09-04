package com.stano.schema.model

import spock.lang.Specification

class DatabaseTypeSpec extends Specification {
  def "if the values in the TargetData enum changes, we need to adjust these tests"() {
    expect:
    DatabaseType.values().size() == 5
  }

  def "getDatabaseTypes should return the correct results"() {
    expect:
    DatabaseType.getDatabaseTypes(targetDatabasesStr) == targetDatabases

    where:
    targetDatabasesStr           | targetDatabases
    null                         | [] as Set
    ""                           | [] as Set
    "mssql"                      | [DatabaseType.MSSQL] as Set
    "mssql,PGSQL"                | [DatabaseType.MSSQL, DatabaseType.PGSQL] as Set
  }

  def "getMaxKeyNameLength should return the correct value for each type"() {
    expect:
    targetDatabase.getMaxKeyNameLength() == maxKeyNameLength

    where:
    targetDatabase     | maxKeyNameLength
    DatabaseType.H2    | 64
    DatabaseType.HSQL  | 64
    DatabaseType.MSSQL | 32
    DatabaseType.MYSQL | 64
    DatabaseType.PGSQL | 63
  }

  def "statement separator should be correct for each DB"() {
    expect:
    db.getStatementSeparator() == sep

    where:
    db               | sep
    DatabaseType.H2  | ";"
    DatabaseType.HSQL| ";"
    DatabaseType.MSSQL | "\nGO"
    DatabaseType.MYSQL | ";"
    DatabaseType.PGSQL | ";"
  }

  def "supportsTriggers flag should be correct for each DB"() {
    expect:
    db.isSupportsTriggers() == supports

    where:
    db                 | supports
    DatabaseType.H2    | false
    DatabaseType.HSQL  | false
    DatabaseType.MSSQL | true
    DatabaseType.MYSQL | true
    DatabaseType.PGSQL | true
  }

  def "valueOf should resolve names case-sensitively (upper) and name() round-trips"() {
    expect:
    DatabaseType.valueOf(name).name() == name

    where:
    name << DatabaseType.values()*.name()
  }
}

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
    targetDatabasesStr   | targetDatabases
    null                 | [] as Set
    ""                   | [] as Set
    "sqlserver"          | [DatabaseType.SQL_SERVER] as Set
    "sqlserver,POSTGRES" | [DatabaseType.SQL_SERVER, DatabaseType.POSTGRES] as Set
  }

  def "getMaxKeyNameLength should return the correct value for each type"() {
    expect:
    targetDatabase.getMaxKeyNameLength() == maxKeyNameLength

    where:
    targetDatabase          | maxKeyNameLength
    DatabaseType.H2         | 64
    DatabaseType.MYSQL      | 64
    DatabaseType.POSTGRES   | 63
    DatabaseType.SQLITE     | 63
    DatabaseType.SQL_SERVER | 32
  }

  def "statement separator should be correct for each DB"() {
    expect:
    db.getStatementSeparator() == sep

    where:
    db                      | sep
    DatabaseType.H2         | ";"
    DatabaseType.MYSQL      | ";"
    DatabaseType.POSTGRES   | ";"
    DatabaseType.SQLITE     | ";"
    DatabaseType.SQL_SERVER | "\nGO"
  }

  def "supportsTriggers flag should be correct for each DB"() {
    expect:
    db.isSupportsTriggers() == supports

    where:
    db                      | supports
    DatabaseType.H2         | false
    DatabaseType.MYSQL      | true
    DatabaseType.POSTGRES   | true
    DatabaseType.SQLITE     | true
    DatabaseType.SQL_SERVER | true
  }

  def "valueOf should resolve names case-sensitively (upper) and name() round-trips"() {
    expect:
    DatabaseType.valueOf(name).name() == name

    where:
    name << DatabaseType.values()*.name()
  }
}

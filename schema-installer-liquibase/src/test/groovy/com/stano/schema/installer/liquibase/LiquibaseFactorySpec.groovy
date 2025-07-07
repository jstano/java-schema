package com.stano.schema.installer.liquibase

import liquibase.database.DatabaseFactory
import liquibase.database.ObjectQuotingStrategy
import liquibase.database.core.PostgresDatabase
import liquibase.structure.core.Table
import spock.lang.Specification

class LiquibaseFactorySpec extends Specification {
  def "test postgres quoting override"() {
    def postgresDatabase = new PostgresDatabase()
    postgresDatabase.setObjectQuotingStrategy(quotingStrategy)

    expect:
    postgresDatabase.escapeObjectName("TestTable", Table.class) == expectedResult

    where:
    quotingStrategy                                 | expectedResult
    ObjectQuotingStrategy.QUOTE_ONLY_RESERVED_WORDS | "TestTable"
    ObjectQuotingStrategy.QUOTE_ALL_OBJECTS         | "\"TestTable\""
    ObjectQuotingStrategy.LEGACY                    | "\"TestTable\""
  }

  def "test registering the CustomPostgresDatabase"() {
    LiquibaseFactory.registerDatabases()

    expect:
    DatabaseFactory.getInstance().implementedDatabases.find { it -> CustomPostgresDatabase.class.isAssignableFrom(it.getClass()) }
  }
}

package com.stano.schema.installer.liquibase

import liquibase.database.ObjectQuotingStrategy
import spock.lang.Specification

class CustomPostgresDatabaseSpec extends Specification {
  def "getObjectQuotingStrategy should always return QUOTE_ONLY_RESERVED_WORDS"() {
    def postgresDatabase = new CustomPostgresDatabase()
    postgresDatabase.setObjectQuotingStrategy(ObjectQuotingStrategy.LEGACY)

    expect:
    postgresDatabase.objectQuotingStrategy == ObjectQuotingStrategy.QUOTE_ONLY_RESERVED_WORDS
  }
}

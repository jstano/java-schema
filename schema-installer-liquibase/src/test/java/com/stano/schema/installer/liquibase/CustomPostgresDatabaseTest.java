package com.stano.schema.installer.liquibase;

import liquibase.database.ObjectQuotingStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CustomPostgresDatabase")
class CustomPostgresDatabaseTest {

  @Test
  @DisplayName("getObjectQuotingStrategy should always return QUOTE_ONLY_RESERVED_WORDS")
  void getObjectQuotingStrategyShouldAlwaysReturnQUOTE_ONLY_RESERVED_WORDS() {
    CustomPostgresDatabase postgresDatabase = new CustomPostgresDatabase();
    postgresDatabase.setObjectQuotingStrategy(ObjectQuotingStrategy.LEGACY);

    assertEquals(postgresDatabase.getObjectQuotingStrategy(), ObjectQuotingStrategy.QUOTE_ONLY_RESERVED_WORDS);
  }
}

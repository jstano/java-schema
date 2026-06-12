package com.stano.schema.installer.liquibase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import liquibase.database.DatabaseFactory;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.database.core.PostgresDatabase;
import liquibase.structure.core.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("LiquibaseFactory")
class LiquibaseFactoryTest {

  @ParameterizedTest
  @MethodSource("providePostgresQuotingTestCases")
  @DisplayName("test postgres quoting override")
  void testPostgresQuotingOverride(ObjectQuotingStrategy quotingStrategy, String expectedResult) {
    PostgresDatabase postgresDatabase = new PostgresDatabase();
    postgresDatabase.setObjectQuotingStrategy(quotingStrategy);

    assertEquals(postgresDatabase.escapeObjectName("TestTable", Table.class), expectedResult);
  }

  private static Stream<Object[]> providePostgresQuotingTestCases() {
    return Stream.of(
        new Object[] {ObjectQuotingStrategy.QUOTE_ONLY_RESERVED_WORDS, "TestTable"},
        new Object[] {ObjectQuotingStrategy.QUOTE_ALL_OBJECTS, "\"TestTable\""},
        new Object[] {ObjectQuotingStrategy.LEGACY, "\"TestTable\""});
  }

  @Test
  @DisplayName("test registering the CustomPostgresDatabase")
  void testRegisteringTheCustomPostgresDatabase() {
    LiquibaseFactory.registerDatabases();

    assertTrue(
        DatabaseFactory.getInstance().getImplementedDatabases().stream()
            .anyMatch(db -> CustomPostgresDatabase.class.isAssignableFrom(db.getClass())));
  }
}

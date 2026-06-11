package com.stano.schema.installer.liquibase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

@DisplayName("LiquibaseServices")
class LiquibaseServicesTest {

  @Test
  @DisplayName("truncateTransactionLog is a no-op for non-SQL-Server connections")
  void truncateTransactionLogIsNoOpForNonSqlServerConnections() throws Exception {
    try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:test_" + System.nanoTime())) {
      new LiquibaseServices().truncateTransactionLog(conn);
    }
  }
}

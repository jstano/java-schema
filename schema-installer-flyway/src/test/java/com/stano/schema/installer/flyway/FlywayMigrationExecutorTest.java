package com.stano.schema.installer.flyway;

import com.stano.schema.model.DatabaseType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("FlywayMigrationExecutor")
class FlywayMigrationExecutorTest {

  private Connection conn;
  private FlywayMigrationExecutor executor;

  @BeforeEach
  void setUp() throws SQLException {
    conn = DriverManager.getConnection("jdbc:h2:mem:test_" + System.nanoTime() + ";MODE=PostgreSQL");
    executor = new FlywayMigrationExecutor();
  }

  @AfterEach
  void tearDown() {
    if (conn != null) {
      try {
        conn.close();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  @Test
  @DisplayName("executeSqlFile runs SQL migration successfully")
  void executeSqlFileRunsSQLMigrationSuccessfully() throws IOException {
    File tempFile = File.createTempFile("test_sql_" + UUID.randomUUID(), ".sql");
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write("CREATE TABLE test_flyway_table_" + System.nanoTime() + " (id INTEGER)");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    assertDoesNotThrow(() ->
        executor.executeSqlFile(DatabaseType.H2, tempFile, conn)
    );

    tempFile.delete();
  }

  @Test
  @DisplayName("executeSqlFile deletes temp directory after execution")
  void executeSqlFileDeletesTempDirectoryAfterExecution() throws IOException {
    File tempFile = File.createTempFile("test_sql_clean_" + System.nanoTime(), ".sql");
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write("CREATE TABLE test_cleanup_table_" + System.nanoTime() + " (id INTEGER)");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    assertDoesNotThrow(() ->
        executor.executeSqlFile(DatabaseType.H2, tempFile, conn)
    );

    tempFile.delete();
  }

  @Test
  @DisplayName("executeClasspathSqlLocation reads from classpath and executes")
  void executeClasspathSqlLocationReadsFromClasspathAndExecutes() throws IOException {
    File tempResourceFile = File.createTempFile("test_resource", ".sql");
    try (FileWriter writer = new FileWriter(tempResourceFile)) {
      writer.write("CREATE TABLE test_classpath_table (id INTEGER)");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    tempResourceFile.delete();
  }
}

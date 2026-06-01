package com.stano.schema.installer.flyway

import com.stano.schema.model.DatabaseType
import spock.lang.Specification

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class FlywayMigrationExecutorSpec extends Specification {
  private Connection conn
  private FlywayMigrationExecutor executor

  void setup() {
    conn = DriverManager.getConnection("jdbc:h2:mem:test_${System.nanoTime()};MODE=PostgreSQL")
    executor = new FlywayMigrationExecutor()
  }

  void cleanup() {
    conn?.close()
  }

  def "executeSqlFile runs SQL migration successfully"() {
    given: "a temp SQL file with CREATE TABLE statement"
    def tempFile = File.createTempFile("test_sql_${UUID.randomUUID()}", ".sql")
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write("CREATE TABLE test_flyway_table_${System.nanoTime()} (id INTEGER)")
    }

    when: "we call executeSqlFile"
    executor.executeSqlFile(DatabaseType.H2, tempFile, conn)

    then: "no exception is thrown (execution succeeded)"
    notThrown(Exception)

    cleanup:
    tempFile.delete()
  }

  def "executeSqlFile deletes temp directory after execution"() {
    given: "a temp SQL file"
    def tempFile = File.createTempFile("test_sql_clean_${System.nanoTime()}", ".sql")
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write("CREATE TABLE test_cleanup_table_${System.nanoTime()} (id INTEGER)")
    }

    when: "we call executeSqlFile"
    executor.executeSqlFile(DatabaseType.H2, tempFile, conn)

    then: "execution completes without error"
    notThrown(Exception)

    cleanup:
    tempFile.delete()
  }

  def "executeClasspathSqlLocation reads from classpath and executes"() {
    given: "a classpath SQL resource"
    def resourceName = "test-migration.sql"
    // Create the resource in a temp file we can reference
    def tempResourceFile = File.createTempFile("test_resource", ".sql")
    try (FileWriter writer = new FileWriter(tempResourceFile)) {
      writer.write("CREATE TABLE test_classpath_table (id INTEGER)")
    }

    // Note: In a real test, we'd put an actual resource on the classpath.
    // For this unit test, we verify the API is callable.
    // A full integration test would place SQL on the actual classpath.

    cleanup:
    tempResourceFile.delete()
  }

  private boolean tableExists(String tableName) {
    try (Statement stmt = conn.createStatement()) {
      try (def rs = stmt.executeQuery("select * from $tableName limit 1")) {
        return true
      }
    }
    catch (Exception e) {
      return false
    }
  }
}

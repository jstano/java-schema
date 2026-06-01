package com.stano.schema.installer.flyway

import com.stano.schema.model.DatabaseType
import spock.lang.Specification

import java.sql.Connection
import java.sql.DriverManager

class FlywaySchemaInstallerSpec extends Specification {
  private Connection conn
  private FlywaySchemaInstaller installer

  void setup() {
    conn = DriverManager.getConnection("jdbc:h2:mem:test_${System.nanoTime()};MODE=PostgreSQL")
    installer = new FlywaySchemaInstaller()
  }

  void cleanup() {
    conn?.close()
  }

  def "executeSqlFile delegates to FlywayMigrationExecutor"() {
    given: "a mock FlywayMigrationExecutor"
    def mockExecutor = Mock(FlywayMigrationExecutor)
    installer.flywayMigrationExecutor = mockExecutor

    and: "a temp SQL file"
    def tempFile = File.createTempFile("test", ".sql")
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write("CREATE TABLE dummy (id INTEGER)")
    }

    when: "we call executeSqlFile"
    installer.executeSqlFile(conn, DatabaseType.H2, null, tempFile)

    then: "FlywayMigrationExecutor.executeSqlFile is called with correct params"
    1 * mockExecutor.executeSqlFile(DatabaseType.H2, tempFile, conn)

    cleanup:
    tempFile.delete()
  }

  def "executePostCreateScript delegates to FlywayMigrationExecutor with derived DatabaseType"() {
    given: "a mock FlywayMigrationExecutor"
    def mockExecutor = Mock(FlywayMigrationExecutor)
    installer.flywayMigrationExecutor = mockExecutor

    when: "we call executePostCreateScript"
    installer.executePostCreateScript(conn, "com/example/post-create.sql")

    then: "FlywayMigrationExecutor.executeClasspathSqlLocation is called"
    1 * mockExecutor.executeClasspathSqlLocation(DatabaseType.H2, "com/example/post-create.sql", conn)
  }

  def "extends SchemaInstaller"() {
    expect:
    installer instanceof com.stano.schema.installer.SchemaInstaller
  }
}

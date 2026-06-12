package com.stano.schema.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MigrationServices {
  public boolean indexExists(Connection connection, String indexName) {
    return executeWithStatement(connection, new IndexExistsMigration(indexName));
  }

  public void dropIndex(Connection connection, String tableName, String indexName) {
    executeWithStatement(connection, new DropIndexMigration(tableName, indexName));
  }

  public boolean tableExists(Connection connection, String name) {
    return new TableExistsMigration(connection, name).tableExists();
  }

  public void dropColumnCheckConstraint(
      Connection connection, String tableName, String columnName) {
    executeWithStatement(connection, new DropColumnCheckConstraintMigration(tableName, columnName));
  }

  public void dropColumnConstraints(Connection connection, String tableName, String columnName) {
    executeWithStatement(connection, new DropColumnConstraintsMigration(tableName, columnName));
  }

  public void dropTableConstraint(Connection connection, String tableName, String constraintName) {
    executeWithStatement(connection, new DropTableConstraintMigration(tableName, constraintName));
  }

  public boolean constraintExists(Connection connection, String constraintName) {
    return executeWithStatement(connection, new ConstraintExistsMigration(constraintName));
  }

  public boolean columnExists(Connection connection, String tableName, String columnName) {
    return new ColumnExistsMigration(connection, tableName, columnName).columnExists();
  }

  public boolean itemExists(Connection connection, String name, String type) {
    return executeWithStatement(connection, new ItemExistsMigration(name, type));
  }

  public void executeSQL(Connection connection, String sql) {
    executeWithStatement(connection, new ExecuteSQLMigration(sql));
  }

  public void dropAllTriggers(Connection connection) {
    executeWithStatement(connection, new DropAllTriggersMigration());
  }

  private <T> T executeWithStatement(Connection connection, StatementAction<T> action) {
    try (Statement statement = connection.createStatement()) {
      return action.execute(statement);
    } catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.StatementWrapper;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationServices {
  public boolean indexExists(Connection connection, String indexName) {
    try {
      return getStatementWrapper(connection).executeWithStatement(new IndexExistsMigration(indexName));
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  public void dropIndex(Connection connection, String tableName, String indexName) {
    try {
      getStatementWrapper(connection).executeWithStatement(new DropIndexMigration(tableName, indexName));
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  public boolean tableExists(Connection connection, String name) {
    return new TableExistsMigration(connection, name).tableExists();
  }

  public void dropColumnCheckConstraint(Connection connection, String tableName, String columnName) {
    try {
      getStatementWrapper(connection).executeWithStatement(new DropColumnCheckConstraintMigration(tableName, columnName));
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  public void dropColumnConstraints(Connection connection, String tableName, String columnName) {
    try {
      getStatementWrapper(connection).executeWithStatement(new DropColumnConstraintsMigration(tableName, columnName));
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  public void dropTableConstraint(Connection connection, String tableName, String constraintName) {
    try {
      getStatementWrapper(connection).executeWithStatement(new DropTableConstraintMigration(tableName, constraintName));
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  public boolean constraintExists(Connection connection, String constraintName) {
    try {
      return getStatementWrapper(connection).executeWithStatement(new ConstraintExistsMigration(constraintName));
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  public boolean columnExists(Connection connection, String tableName, String columnName) {
    return new ColumnExistsMigration(connection, tableName, columnName).columnExists();
  }

  public boolean itemExists(Connection connection, String name, String type) {
    try {
      return getStatementWrapper(connection).executeWithStatement(new ItemExistsMigration(name, type));
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  public void executeSQL(Connection connection, String sql) {
    try {
      getStatementWrapper(connection).executeWithStatement(new ExecuteSQLMigration(sql));
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  public void dropAllTriggers(Connection connection) {
    try {
      getStatementWrapper(connection).executeWithStatement(new DropAllTriggersMigration());
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  private StatementWrapper getStatementWrapper(Connection connection) {
    return new StatementWrapper(connection);
  }
}

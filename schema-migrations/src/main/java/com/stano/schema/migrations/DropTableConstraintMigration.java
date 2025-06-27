package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.Statement;

public class DropTableConstraintMigration implements ExecuteWithStatement<Void> {
  private final String tableName;
  private final String constraintName;

  public DropTableConstraintMigration(String tableName, String constraintName) {
    this.tableName = tableName;
    this.constraintName = constraintName;
  }

  @Override
  public Void executeWithStatement(Statement statement) {
    if (constraintExists(statement, constraintName)) {
      dropConstraint(statement, tableName, constraintName);
    }

    return null;
  }

  private boolean constraintExists(Statement statement, String constraintName) {
    return new ConstraintExistsMigration(constraintName).executeWithStatement(statement);
  }

  private void dropConstraint(Statement statement, String tableName, String constraintName) {
    new ExecuteSQLMigration(String.format("alter table %s drop constraint %s", tableName, constraintName)).executeWithStatement(statement);
  }
}

package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteSQLMigration implements ExecuteWithStatement<Void> {
  private final String sql;

  public ExecuteSQLMigration(String sql) {
    this.sql = sql;
  }

  @Override
  public Void executeWithStatement(Statement statement) {
    try {
      statement.executeUpdate(sql);

      return null;
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

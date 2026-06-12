package com.stano.schema.migrations;

import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteSQLMigration implements StatementAction<Void> {
  private final String sql;

  public ExecuteSQLMigration(String sql) {
    this.sql = sql;
  }

  @Override
  public Void execute(Statement statement) {
    try {
      statement.executeUpdate(sql);

      return null;
    } catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

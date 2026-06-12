package com.stano.schema.migrations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemExistsMigration implements StatementAction<Boolean> {
  private final String name;
  private final String type;

  public ItemExistsMigration(String name, String type) {
    this.name = name;
    this.type = type;
  }

  @Override
  public Boolean execute(Statement statement) {
    try (ResultSet rs =
        statement.executeQuery(
            String.format(
                "select name from dbo.sysobjects where name = '%s' and type = '%s'", name, type))) {
      return rs.next();
    } catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

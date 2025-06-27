package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemExistsMigration implements ExecuteWithStatement<Boolean> {
  private final String name;
  private final String type;

  public ItemExistsMigration(String name, String type) {
    this.name = name;
    this.type = type;
  }

  @Override
  public Boolean executeWithStatement(Statement statement) {
    try (ResultSet rs = statement.executeQuery(String.format("select name from dbo.sysobjects where name = '%s' and type = '%s'", name, type))) {
      return rs.next();
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

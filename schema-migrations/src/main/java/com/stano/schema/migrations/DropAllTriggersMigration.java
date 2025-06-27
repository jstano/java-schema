package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DropAllTriggersMigration implements ExecuteWithStatement<Void> {
  @Override
  public Void executeWithStatement(Statement statement) {
    try {
      for (String triggerName : loadTriggerNames(statement)) {
        statement.executeUpdate(String.format("drop trigger %s", triggerName));
      }

      return null;
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  private List<String> loadTriggerNames(Statement statement) throws SQLException {
    List<String> triggerNames = new ArrayList<>();

    try (ResultSet rs = statement.executeQuery("select name from dbo.sysobjects where type = 'TR'")) {
      while (rs.next()) {
        triggerNames.add(rs.getString("name"));
      }
    }

    return triggerNames;
  }
}

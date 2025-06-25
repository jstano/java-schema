package com.stano.schema.migrations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableExistsMigration {
  private final Connection connection;
  private final String tableName;

  public TableExistsMigration(Connection connection, String tableName) {
    this.connection = connection;
    this.tableName = tableName;
  }

  public boolean tableExists() {
    try (ResultSet rs = connection.getMetaData().getTables(null, null, getAdjustedTableName(), new String[] {"TABLE"})) {
      return rs.next();
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  private String getAdjustedTableName() {
    return MigrationUtil.normalizeIdentifierCase(connection, tableName);
  }
}

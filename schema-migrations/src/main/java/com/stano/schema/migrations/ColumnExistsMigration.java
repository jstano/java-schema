package com.stano.schema.migrations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnExistsMigration {
  private final Connection connection;
  private final String tableName;
  private final String columnName;

  public ColumnExistsMigration(Connection connection, String tableName, String columnName) {
    this.connection = connection;
    this.tableName = tableName;
    this.columnName = columnName;
  }

  public boolean columnExists() {
    try {
      try (ResultSet rs = connection.getMetaData().getColumns(null, null, getAdjustedName(tableName), getAdjustedName(columnName))) {
        return rs.next();
      }
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  private String getAdjustedName(String name) {
    return MigrationUtil.normalizeIdentifierCase(connection, name);
  }
}

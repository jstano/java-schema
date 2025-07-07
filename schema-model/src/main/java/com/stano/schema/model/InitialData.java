package com.stano.schema.model;

public class InitialData {
  private final String sql;
  private final DatabaseType databaseType;

  public InitialData(String sql, DatabaseType databaseType) {
    this.sql = sql;
    this.databaseType = databaseType;
  }

  public String getSql() {
    return sql;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }
}

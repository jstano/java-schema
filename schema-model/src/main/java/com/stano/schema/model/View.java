package com.stano.schema.model;

public class View {
  private final String schemaName;
  private final String name;
  private final String sql;
  private final DatabaseType databaseType;

  public View(String schemaName, String name, String sql, DatabaseType databaseType) {
    this.schemaName = schemaName;
    this.name = name;
    this.sql = sql;
    this.databaseType = databaseType;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public String getName() {
    return name;
  }

  public String getSql() {
    return sql;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }
}

package com.stano.schema.model;

public class Function {
  private final String schemaName;
  private final String name;
  private final DatabaseType databaseType;
  private final String sql;

  public Function(String schemaName, String name, DatabaseType databaseType, String sql) {
    this.schemaName = schemaName;
    this.name = name;
    this.databaseType = databaseType;
    this.sql = sql;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public String getName() {
    return name;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }

  public String getSql() {
    return sql;
  }
}

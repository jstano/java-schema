package com.stano.schema.model;

public class Constraint {
  private final String name;
  private final String sql;
  private final DatabaseType databaseType;

  public Constraint(String name, String sql, DatabaseType databaseType) {
    this.name = name;
    this.sql = sql;
    this.databaseType = databaseType;
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

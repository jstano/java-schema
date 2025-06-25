package com.stano.schema.model;

public class OtherSql {
  private final DatabaseType databaseType;
  private final OtherSqlOrder order;
  private final String sql;

  public OtherSql(DatabaseType databaseType, OtherSqlOrder order, String sql) {
    this.databaseType = databaseType;
    this.order = order;
    this.sql = sql;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }

  public OtherSqlOrder getOrder() {
    return order;
  }

  public String getSql() {
    return sql;
  }
}

package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.DatabaseType;

class VendorSql {
  private final DatabaseType databaseType;
  private final String sql;

  VendorSql(DatabaseType databaseType, String sql) {
    this.databaseType = databaseType;
    this.sql = sql;
  }

  DatabaseType getDatabaseType() {
    return databaseType;
  }

  String getSql() {
    return sql;
  }
}

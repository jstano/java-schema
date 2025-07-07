package com.stano.schema.model;

import java.util.HashSet;
import java.util.Set;

public enum DatabaseType {
  H2(";", 64, false),
  HSQL(";", 64, false),
  MSSQL("\nGO", 32, true),
  MYSQL(";", 64, true),
  PGSQL(";", 63, true);

  public static Set<DatabaseType> getDatabaseTypes(String targetDatabasesStr) {
    Set<DatabaseType> databaseTypes = new HashSet<>();

    if (targetDatabasesStr != null) {
      for (String targetDatabase : targetDatabasesStr.split(",")) {
        if (!targetDatabase.trim().isEmpty()) {
          databaseTypes.add(valueOf(targetDatabase.toUpperCase()));
        }
      }
    }

    return databaseTypes;
  }

  private final String statementSeparator;
  private final int maxKeyNameLength;
  private final boolean supportsTriggers;

  public String getStatementSeparator() {
    return statementSeparator;
  }

  public int getMaxKeyNameLength() {
    return maxKeyNameLength;
  }

  public boolean isSupportsTriggers() {
    return supportsTriggers;
  }

  DatabaseType(String statementSeparator, int maxKeyNameLength, boolean supportsTriggers) {
    this.statementSeparator = statementSeparator;
    this.maxKeyNameLength = maxKeyNameLength;
    this.supportsTriggers = supportsTriggers;
  }
}

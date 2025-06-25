package com.stano.schema.migrations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public final class MigrationUtil {
  public static String normalizeIdentifierCase(Connection connection, String identifier) {
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();

      if (databaseMetaData.storesLowerCaseIdentifiers()) {
        return identifier.toLowerCase();
      }

      if (databaseMetaData.storesUpperCaseIdentifiers()) {
        return identifier.toUpperCase();
      }

      return identifier;
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }

  private MigrationUtil() {
  }
}

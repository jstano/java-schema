package com.stano.schema.installer.liquibase;

import java.sql.SQLException;
import java.sql.Statement;

public class LiquibaseServices {
  private static final String JDBC_SQL_SERVER_PREFIX = "jdbc:sqlserver:";

  public void truncateTransactionLog(java.sql.Connection connection) throws SQLException {
    if (connection.getMetaData().getURL().startsWith(JDBC_SQL_SERVER_PREFIX)) {
      try (Statement statement = connection.createStatement()) {
        statement.execute("declare @fileName varchar(20)\nselect @fileName = FILE_NAME(2)\nDBCC Shrinkfile(@filename,100)"); //NON-NLS
      }
    }
  }
}

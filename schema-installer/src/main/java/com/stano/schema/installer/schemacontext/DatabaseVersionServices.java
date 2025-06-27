package com.stano.schema.installer.schemacontext;

import com.stano.jdbcutils.datasource.DriverType;
import com.stano.jdbcutils.utils.TransactionalExecutor;
import com.stano.schema.migrations.TableExistsMigration;
import com.stano.schema.model.Version;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseVersionServices {
  public Version getVersion(Connection connection) {

    return TransactionalExecutor.withConnection(connection).execute(() -> {
      ensureDatabaseVersionTableExists(connection);

      try (Statement statement = connection.createStatement()) {
        try (ResultSet rs = statement.executeQuery("select version from databaseversion")) {
          if (rs.next()) {
            return new Version(rs.getString("version"));
          }

          throw new IllegalStateException("Unable to read the database version");
        }
      }
    });
  }

  public void setVersion(Connection connection, Version version) {
    TransactionalExecutor.withConnection(connection).execute(() -> {
      ensureDatabaseVersionTableExists(connection);

      try (Statement statement = connection.createStatement()) {
        statement.execute(String.format("update databaseversion set version = '%s'", version.toString()));
      }
    });
  }

  private void ensureDatabaseVersionTableExists(Connection connection) throws SQLException {
    if (new TableExistsMigration(connection, "databaseversion").tableExists()) {
      return;
    }

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(getDatabaseVersionCreateTableSql(DriverType.fromConnection(connection)));
      statement.executeUpdate("insert into databaseversion values ('01.00')");
    }
  }

  private String getDatabaseVersionCreateTableSql(DriverType driverType) {
    if (driverType == DriverType.MSSQL) {
      return "create table dbo.databaseversion (version varchar(10) not null,constraint pk_databaseversion primary key (version))";
    }
    else {
      return "create table databaseversion (version varchar(10) not null,constraint pk_databaseversion primary key (version))";
    }
  }
}

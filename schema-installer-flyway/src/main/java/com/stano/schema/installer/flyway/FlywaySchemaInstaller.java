package com.stano.schema.installer.flyway;

import com.stano.jdbcutils.datasource.DriverType;
import com.stano.schema.installer.SchemaInstaller;
import com.stano.schema.installer.schemacontext.SchemaContext;
import com.stano.schema.model.DatabaseType;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

public class FlywaySchemaInstaller extends SchemaInstaller {
  private FlywayMigrationExecutor flywayMigrationExecutor = new FlywayMigrationExecutor();

  public void setFlywayMigrationExecutor(FlywayMigrationExecutor flywayMigrationExecutor) {
    this.flywayMigrationExecutor = flywayMigrationExecutor;
  }

  @Override
  protected void executeSqlFile(Connection connection, DatabaseType databaseType, SchemaContext schemaContext, File sqlFile) throws IOException {
    flywayMigrationExecutor.executeSqlFile(databaseType, sqlFile, connection);
  }

  @Override
  protected void executePostCreateScript(Connection connection, String postCreateResourceName) {
    DatabaseType databaseType = DatabaseType.valueOf(DriverType.fromConnection(connection).name());
    flywayMigrationExecutor.executeClasspathSqlLocation(databaseType, postCreateResourceName, connection);
  }
}

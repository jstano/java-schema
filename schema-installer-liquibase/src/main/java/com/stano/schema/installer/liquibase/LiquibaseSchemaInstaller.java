package com.stano.schema.installer.liquibase;

import com.stano.schema.installer.SchemaInstaller;
import com.stano.schema.installer.schemacontext.SchemaContext;
import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Version;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

public class LiquibaseSchemaInstaller extends SchemaInstaller {
  private LiquibaseChangeLogCreator liquibaseChangeLogCreator = new LiquibaseChangeLogCreator();
  private LiquibaseChangeLogExecutor liquibaseChangeLogExecutor = new LiquibaseChangeLogExecutor();

  @Override
  protected void executeSqlFile(Connection connection, DatabaseType databaseType, SchemaContext schemaContext, File sqlFile) throws IOException {
    File tempChangeLogFile = createTempChangeLogFile(databaseType, sqlFile, schemaContext.getSchemaVersion(), schemaContext.getEndDelimiter());
    executeTempChangeLog(connection, tempChangeLogFile);
  }

  @Override
  protected void executePostCreateScript(Connection connection, String postCreateResourceName) {
    new LiquibaseChangeLogExecutor().executeChangeLog(postCreateResourceName, connection);
  }

  private File createTempChangeLogFile(DatabaseType databaseType,
                                       File tempSqlFile,
                                       Version schemaVersion,
                                       String endDelimiter) throws IOException {
    return liquibaseChangeLogCreator.createTempChangeLogFile(databaseType,
                                                             tempSqlFile,
                                                             schemaVersion,
                                                             endDelimiter);
  }

  private void executeTempChangeLog(Connection connection, File tempChangeLogFile) {
    try {
      liquibaseChangeLogExecutor.executeChangeLog(tempChangeLogFile, connection);
    }
    finally {
      tempChangeLogFile.delete();
    }
  }
}

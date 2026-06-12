package com.stano.schema.installer.liquibase;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.exception.ValidationFailedException;

public class LiquibaseChangeLogExecutor {
  private LiquibaseFactory liquibaseFactory = new LiquibaseFactory();
  private DatabaseUpgradeLog databaseUpgradeLog = new DatabaseUpgradeLog();
  private LiquibaseServices liquibaseServices = new LiquibaseServices();

  public void executeChangeLog(String changeLogResource, Connection connection) {
    try {
      executeChangeLog(
          liquibaseFactory.createLiquibase(changeLogResource, connection),
          connection,
          changeLogResource);
    } catch (LiquibaseException x) {
      throw new LiquibaseRuntimeException(x);
    }
  }

  public void executeChangeLog(File changeLogFile, Connection connection) {
    try {
      executeChangeLog(
          liquibaseFactory.createLiquibase(changeLogFile, connection),
          connection,
          changeLogFile.getAbsolutePath());
    } catch (LiquibaseException x) {
      throw new LiquibaseRuntimeException(x);
    }
  }

  public void executeChangeLog(
      Liquibase liquibase, Connection connection, String changeLogResource) {
    Database database = liquibase.getDatabase();
    int databaseChangeLogId = databaseUpgradeLog.start(database, connection, changeLogResource);

    try {
      runWithChecksumRetry(liquibase, connection);
      databaseUpgradeLog.finish(database, connection, databaseChangeLogId, null);
    } catch (LiquibaseException x) {
      databaseUpgradeLog.finish(database, connection, databaseChangeLogId, getStackTrace(x));
      throw new LiquibaseRuntimeException(x);
    } finally {
      liquibaseFactory.getExecutorService().clearExecutor("jdbc", database);
    }
  }

  private static String getStackTrace(Throwable x) {
    StringWriter sw = new StringWriter();
    x.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  private void runWithChecksumRetry(Liquibase liquibase, Connection connection)
      throws LiquibaseException {
    try {
      liquibase.update(new Contexts());
    } catch (ValidationFailedException x) {
      liquibase.clearCheckSums();
      liquibase.update(new Contexts());
      truncateTransactionLog(connection);
    }
  }

  private void truncateTransactionLog(Connection connection) throws LiquibaseException {
    try {
      liquibaseServices.truncateTransactionLog(connection);
    } catch (SQLException x) {
      throw new LiquibaseException(x);
    }
  }
}

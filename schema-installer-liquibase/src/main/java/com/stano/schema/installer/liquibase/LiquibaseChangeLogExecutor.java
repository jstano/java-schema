package com.stano.schema.installer.liquibase;

import com.stano.exceptions.ExceptionUtils;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.exception.ValidationFailedException;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class LiquibaseChangeLogExecutor {
  private LiquibaseFactory liquibaseFactory = new LiquibaseFactory();
  private DatabaseUpgradeLog databaseUpgradeLog = new DatabaseUpgradeLog();
  private LiquibaseServices liquibaseServices = new LiquibaseServices();

  public void executeChangeLog(String changeLogResource, Connection connection) {
    try {
      executeChangeLog(liquibaseFactory.createLiquibase(changeLogResource, connection),
                       connection,
                       changeLogResource);
    }
    catch (LiquibaseException x) {
      throw new LiquibaseRuntimeException(x);
    }
  }

  public void executeChangeLog(File changeLogFile, Connection connection) {
    try {
      executeChangeLog(liquibaseFactory.createLiquibase(changeLogFile, connection),
                       connection,
                       changeLogFile.getAbsolutePath());
    }
    catch (LiquibaseException x) {
      throw new LiquibaseRuntimeException(x);
    }
  }

  public void executeChangeLog(Liquibase liquibase, Connection connection, String changeLogResource) {
    Database database = liquibase.getDatabase();

    int databaseChangeLogId = databaseUpgradeLog.start(database, connection, changeLogResource);

    try {
      try {
        try {
          liquibase.update(new Contexts());
        }
        catch (ValidationFailedException x) {
          liquibase.clearCheckSums();

          liquibase.update(new Contexts());

          truncateTransactionLog(connection);
        }

        databaseUpgradeLog.finish(database, connection, databaseChangeLogId, null);
      }
      finally {
        liquibaseFactory.getExecutorService().clearExecutor("jdbc", database);
      }
    }
    catch (LiquibaseException x) {
      databaseUpgradeLog.finish(database, connection, databaseChangeLogId, ExceptionUtils.getStackTrace(x));

      throw new LiquibaseRuntimeException(x);
    }
  }

  private void truncateTransactionLog(Connection connection) throws LiquibaseException {
    try {
      liquibaseServices.truncateTransactionLog(connection);
    }
    catch (SQLException x) {
      throw new LiquibaseException(x);
    }
  }
}

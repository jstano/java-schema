package com.stano.schema.installer.liquibase;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.executor.ExecutorService;

import java.sql.Connection;

public class LiquibaseDatabaseUpdateChecker {
  private LiquibaseFactory liquibaseFactory = new LiquibaseFactory();

  public boolean databaseNeedsUpdating(String changeLogResource, Connection connection) {
    try {
      return databaseNeedsUpdating(liquibaseFactory.createLiquibase(changeLogResource, connection),
                                   liquibaseFactory.getExecutorService());
    }
    catch (Exception x) {
      throw new LiquibaseRuntimeException(x);
    }
  }

  public boolean databaseNeedsUpdating(Liquibase liquibase, ExecutorService executorService) {
    try {
      try {
        try {
          return !liquibase.listUnrunChangeSets(new Contexts()).isEmpty();
        }
        catch (Exception x) {
          liquibase.clearCheckSums();

          return !liquibase.listUnrunChangeSets(new Contexts()).isEmpty();
        }
      }
      finally {
        executorService.clearExecutor(liquibase.getDatabase());
      }
    }
    catch (Exception x) {
      throw new LiquibaseRuntimeException(x);
    }
  }
}

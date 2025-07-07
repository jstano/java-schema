package com.stano.schema.installer.liquibase;

import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.executor.ExecutorService;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.List;

public class LiquibaseFactory {
  public Liquibase createLiquibase(String changeLogResource, Connection connection) throws LiquibaseException {
    return createLiquibase(changeLogResource,
                           connection,
                           new CustomResourceAccessor(getClass().getClassLoader()));
  }

  public Liquibase createLiquibase(File changeLogFile, Connection connection) throws LiquibaseException {
    return createLiquibase(changeLogFile.getName(),
                           connection,
                           getResourceAccessor(changeLogFile));
  }

  private Liquibase createLiquibase(String changeLogResource,
                                    Connection connection,
                                    ResourceAccessor resourceAccessor) throws LiquibaseRuntimeException {
    try {
//         LogFactory.setInstance(new Slf4jLogFactory());

      registerDatabases();

      Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

      return new Liquibase(changeLogResource,
                           resourceAccessor,
                           database);
    }
    catch (LiquibaseException x) {
      throw new LiquibaseRuntimeException(x);
    }
  }

  public ExecutorService getExecutorService() {
    return Scope.getCurrentScope().getSingleton(ExecutorService.class);
  }

  private ResourceAccessor getResourceAccessor(File changeLogFile) {
    try {
      return new DirectoryResourceAccessor(changeLogFile.getParentFile());
    }
    catch (FileNotFoundException x) {
      throw new IllegalStateException(x);
    }
  }

  private static void registerDatabases() {
    DatabaseFactory databaseFactory = DatabaseFactory.getInstance();

    if (databaseFactory.getImplementedDatabases().stream().noneMatch(it -> CustomPostgresDatabase.class.isAssignableFrom(it.getClass()))) {
      databaseFactory.clearRegistry();
      databaseFactory.register(new CustomPostgresDatabase());

      List<Database> databases = Scope.getCurrentScope().getServiceLocator().findInstances(Database.class);
      databases.forEach(databaseFactory::register);
    }
  }
}

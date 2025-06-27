package com.stano.schema.installer;

import com.stano.files.FileServices;
import com.stano.jdbcutils.datasource.DriverType;
import com.stano.resourcelocator.ResourceLocator;
import com.stano.resourcelocator.ResourceLocatorService;
import com.stano.schema.gensql.GenSQL;
import com.stano.schema.gensql.impl.common.OutputMode;
import com.stano.schema.installer.schemacontext.DatabaseVersionServices;
import com.stano.schema.installer.schemacontext.SchemaContext;
import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.ForeignKeyMode;
import com.stano.schema.model.Schema;
import com.stano.schema.parser.SchemaParser;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class SchemaInstaller {
  private static final String TEMP_SQL_FILE_PREFIX = "_temp_sql_";
  private static final String TEMP_SQL_FILE_EXTENSION = ".sql";

  private GenSQL genSQL = new GenSQL();
  private SchemaParser schemaParser = new SchemaParser();
  private FileServices fileServices = new FileServices();
  private DatabaseVersionServices databaseVersionServices = new DatabaseVersionServices();

  public void installSchema(DataSource dataSource, SchemaContext schemaContext) {
    try (Connection connection = dataSource.getConnection()) {
      installSchema(connection, schemaContext);
    }
    catch (SQLException x) {
      throw new SchemaMigrationException(x);
    }
  }

  public void installSchema(Connection connection, SchemaContext schemaContext) {
    installSchema(connection, schemaContext, true);
  }

  public void installSchemaNoVersionUpdate(Connection connection, SchemaContext schemaContext) {
    installSchema(connection, schemaContext, false);
  }

  public void installSchema(Connection connection, SchemaContext schemaContext, boolean updateVersion) {
    try {
      if (schemaContext.schemaIsInstalled(connection)) {
        return;
      }

      DatabaseType databaseType = DatabaseType.valueOf(DriverType.fromConnection(connection).name());
      Schema schema = schemaParser.parseSchema(schemaContext.getSchemaUrl());

      File tempSqlFile = createTempSqlFile(databaseType);
      generateSqlToTempSqlFile(databaseType, schemaContext, schema, tempSqlFile);

      executeSqlFile(connection, databaseType, schemaContext, tempSqlFile);

      runPostCreateScript(schemaContext, connection);

      if (updateVersion) {
        databaseVersionServices.setVersion(connection, schemaContext.getSchemaVersion());
      }

      schemaContext.schemaInstalled(connection);
    }
    catch (IOException | SQLException x) {
      throw new SchemaMigrationException(x);
    }
  }

  public void installSql(DataSource dataSource, SchemaContext schemaContext) {
    try {
      try (Connection connection = dataSource.getConnection()) {
        if (schemaContext.schemaIsInstalled(connection)) {
          return;
        }

        DatabaseType databaseType = DatabaseType.valueOf(DriverType.fromConnection(connection).name());

        File tempSqlFile = createTempSqlFile(databaseType);
        generateSqlToTempSqlFile(schemaContext.getSchemaUrl().openStream(), tempSqlFile);

        executeSqlFile(connection, databaseType, schemaContext, tempSqlFile);

        schemaContext.schemaInstalled(connection);
      }
    }
    catch (IOException | SQLException x) {
      throw new SchemaMigrationException(x);
    }
  }

  protected abstract void executeSqlFile(Connection connection, DatabaseType databaseType, SchemaContext schemaContext, File sqlFile) throws IOException;

  protected abstract void executePostCreateScript(Connection connection, String postCreateResourceName);

  private File createTempSqlFile(DatabaseType databaseType) throws IOException {
    return fileServices.createTempFile(TEMP_SQL_FILE_PREFIX, databaseType.name().toLowerCase() + TEMP_SQL_FILE_EXTENSION);
  }

  private void generateSqlToTempSqlFile(DatabaseType databaseType,
                                        SchemaContext schemaContext,
                                        Schema schema,
                                        File tempSqlFile) throws IOException {
    ForeignKeyMode foreignKeyMode = schema.getForeignKeyMode();
    BooleanMode booleanMode = schema.getBooleanMode();

    if (foreignKeyMode == null) {
      foreignKeyMode = schemaContext.getForeignKeyMode();
    }

    if (booleanMode == null) {
      booleanMode = schemaContext.getBooleanMode();
    }

    genSQL.generateSQL(databaseType,
                       schema,
                       new PrintWriter(fileServices.createFileWriter(tempSqlFile)),
                       foreignKeyMode,
                       booleanMode,
                       OutputMode.ALL,
                       "\nGO");
  }

  private void generateSqlToTempSqlFile(InputStream inputStream,
                                        File tempSqlFile) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
         PrintWriter writer = new PrintWriter(new FileWriter(tempSqlFile))) {
      String line;

      while ((line = reader.readLine()) != null) {
        writer.println(line);
      }
    }
  }

  private void runPostCreateScript(SchemaContext schemaContext, Connection connection) {
    ResourceLocator postCreateScriptLocator = schemaContext.getPostCreateScriptLocator(connection);

    if (postCreateScriptLocator == null) {
      return;
    }

    new ResourceLocatorService().getResourceNames(postCreateScriptLocator)
                                .stream()
                                .findFirst()
                                .ifPresent(postCreateResourceName -> executePostCreateScript(connection, postCreateResourceName));
  }
}

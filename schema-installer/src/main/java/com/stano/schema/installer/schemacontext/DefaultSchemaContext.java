package com.stano.schema.installer.schemacontext;

import com.stano.resourcelocator.ResourceLocator;
import com.stano.schema.migrations.MigrationServices;
import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.ForeignKeyMode;
import com.stano.schema.model.Version;
import com.stano.schema.parser.SchemaParser;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DefaultSchemaContext implements SchemaContext {
  private DatabaseVersionServices databaseVersionServices = new DatabaseVersionServices();
  private SchemaParser schemaParser = new SchemaParser();

  protected MigrationServices migrationServices = new MigrationServices();

  @Override
  public ResourceLocator getPostCreateScriptLocator(Connection connection) {
    return null;
  }

  @Override
  public boolean isVersionBased() {
    return true;
  }

  @Override
  public Version getSchemaVersion() {
    Version version = schemaParser.parseSchema(getSchemaUrl()).getVersion();

    if (version != null) {
      return version;
    }

    return new Version(1, 0);
  }

  @Override
  public Version getDatabaseVersion(Connection connection) {
    if (isVersionBased()) {
      return databaseVersionServices.getVersion(connection);
    }

    return null;
  }

  @Override
  public BooleanMode getBooleanMode() {
    return BooleanMode.NATIVE;
  }

  @Override
  public ForeignKeyMode getForeignKeyMode() {
    return ForeignKeyMode.RELATIONS;
  }

  @Override
  public boolean schemaIsInstalled(Connection connection) throws SQLException {
    return migrationServices.tableExists(connection, "databaseupgradelog");
  }

  @Override
  public void schemaInstalled(Connection connection) throws SQLException {
  }
}

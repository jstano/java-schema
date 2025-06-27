package com.stano.schema.installer.schemacontext;

import com.stano.jdbcutils.datasource.DataSourceProperties;
import com.stano.resourcelocator.ResourceLocator;
import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.ForeignKeyMode;
import com.stano.schema.model.Version;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public interface SchemaContext {
  URL getSchemaUrl();

  ResourceLocator getMigrationScriptLocator(Connection connection);

  ResourceLocator getPostCreateScriptLocator(Connection connection);

  boolean isVersionBased();

  Version getSchemaVersion();

  Version getDatabaseVersion(Connection connection);

  BooleanMode getBooleanMode();

  ForeignKeyMode getForeignKeyMode();

  boolean schemaIsInstalled(Connection connection) throws SQLException;

  void schemaInstalled(Connection connection) throws SQLException;

  default String getEndDelimiter() {

    return "GO";
  }

  default String getMigrateParams(DataSourceProperties dataSourceProperties) {

    return String.format("--migrate=%s,%s,%s,%s",
                         dataSourceProperties.getUrl(),
                         dataSourceProperties.getUsername(),
                         "xxxxxx",
                         dataSourceProperties.getDriverType().toString().toLowerCase());

  }
}

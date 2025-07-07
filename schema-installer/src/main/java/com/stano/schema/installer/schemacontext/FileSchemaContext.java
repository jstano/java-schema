package com.stano.schema.installer.schemacontext;

import com.stano.resourcelocator.ResourceLocator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public class FileSchemaContext extends DefaultSchemaContext {
  private final File schemaFile;

  public FileSchemaContext(File schemaFile) {
    this.schemaFile = schemaFile;
  }

  @Override
  public URL getSchemaUrl() {
    try {
      return schemaFile.toURI().toURL();
    }
    catch (MalformedURLException x) {
      throw new IllegalArgumentException(x);
    }
  }

  @Override
  public ResourceLocator getMigrationScriptLocator(Connection connection) {
    return null;
  }

  @Override
  public boolean schemaIsInstalled(Connection connection) throws SQLException {
    return false;
  }
}

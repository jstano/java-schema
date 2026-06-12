package com.stano.schema.installer.schemacontext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public class FileSchemaContext extends DefaultSchemaContext {
  public FileSchemaContext(File schemaFile) {
    super(toUrl(schemaFile), null);
  }

  @Override
  public boolean schemaIsInstalled(Connection connection) throws SQLException {
    return false;
  }

  private static URL toUrl(File schemaFile) {
    try {
      return schemaFile.toURI().toURL();
    } catch (MalformedURLException x) {
      throw new IllegalArgumentException(x);
    }
  }
}

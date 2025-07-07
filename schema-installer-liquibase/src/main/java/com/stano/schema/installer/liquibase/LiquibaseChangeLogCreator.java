package com.stano.schema.installer.liquibase;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Version;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class LiquibaseChangeLogCreator {
  private static final String TEMP_CHANGE_LOG_PREFIX = "_TEMP_CHANGE_LOG_";
  private static final String TEMP_CHANGE_LOG_EXTENSION = ".xml";

  private FileServices fileServices = new FileServices();

  public File createTempChangeLogFile(DatabaseType databaseType, File tempSqlFile, Version version, String endDelimiter) throws IOException {
    File tempChangeLogFile = fileServices.createTempFile(TEMP_CHANGE_LOG_PREFIX,
                                                         "_" + databaseType.name().toLowerCase()
                                                         + TEMP_CHANGE_LOG_EXTENSION);

    try (PrintWriter out = new PrintWriter(fileServices.createFileWriter(tempChangeLogFile))) {
      out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      out.println("<databaseChangeLog");
      out.println("   xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"");
      out.println("   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
      out.println(
        "   xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog https://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd\">");
      out.println(String.format("<changeSet id=\"%s\" author=\"InstallDatabase\">", version == null ? "1.0" : version));
      out.println(String.format("<sqlFile path=\"%s\" relativeToChangelogFile=\"true\" endDelimiter=\"%s\"/>",
                                tempSqlFile.getName(),
                                endDelimiter));
      out.println("</changeSet>");
      out.println("</databaseChangeLog>");
    }

    return tempChangeLogFile;
  }
}

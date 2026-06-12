package com.stano.schema.installer.liquibase;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Version;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("LiquibaseChangeLogCreator")
class LiquibaseChangeLogCreatorTest {

  @TempDir Path tempDir;

  private LiquibaseChangeLogCreator creator;
  private File sqlFile;

  @BeforeEach
  void setUp() throws Exception {
    creator = new LiquibaseChangeLogCreator();
    sqlFile = tempDir.resolve("V1__install.sql").toFile();
    sqlFile.createNewFile();
  }

  @Test
  @DisplayName("createTempChangeLogFile produces valid Liquibase XML with correct changeSet id")
  void createTempChangeLogFileProducesValidXmlWithCorrectChangeSetId() throws Exception {
    File changeLogFile =
        creator.createTempChangeLogFile(DatabaseType.H2, sqlFile, new Version(1, 0), ";");

    String content = Files.readString(changeLogFile.toPath());
    assertTrue(content.contains("<?xml version=\"1.0\""), "should start with XML declaration");
    assertTrue(content.contains("databaseChangeLog"), "should contain databaseChangeLog element");
    assertTrue(content.contains("changeSet"), "should contain changeSet element");
    assertTrue(content.contains("01.00"), "changeSet id should be the version string");
  }

  @Test
  @DisplayName(
      "createTempChangeLogFile references the sql file by name with relativeToChangelogFile=true")
  void createTempChangeLogFileReferencesSqlFileByName() throws Exception {
    File changeLogFile =
        creator.createTempChangeLogFile(DatabaseType.H2, sqlFile, new Version(1, 0), ";");

    String content = Files.readString(changeLogFile.toPath());
    assertTrue(content.contains(sqlFile.getName()), "should reference sql file by name");
    assertTrue(
        content.contains("relativeToChangelogFile=\"true\""),
        "should set relativeToChangelogFile=true");
  }

  @Test
  @DisplayName("createTempChangeLogFile includes the specified endDelimiter")
  void createTempChangeLogFileIncludesEndDelimiter() throws Exception {
    File changeLogFile =
        creator.createTempChangeLogFile(DatabaseType.H2, sqlFile, new Version(1, 0), "GO");

    String content = Files.readString(changeLogFile.toPath());
    assertTrue(content.contains("endDelimiter=\"GO\""), "should include the end delimiter");
  }

  @Test
  @DisplayName("createTempChangeLogFile uses '1.0' as changeSet id when version is null")
  void createTempChangeLogFileUses10AsIdWhenVersionIsNull() throws Exception {
    File changeLogFile = creator.createTempChangeLogFile(DatabaseType.H2, sqlFile, null, ";");

    String content = Files.readString(changeLogFile.toPath());
    assertTrue(content.contains("id=\"1.0\""), "null version should use id=1.0");
  }

  @Test
  @DisplayName("createTempChangeLogFile XML-escapes special characters in endDelimiter")
  void createTempChangeLogFileEscapesSpecialCharactersInEndDelimiter() throws Exception {
    File changeLogFile =
        creator.createTempChangeLogFile(DatabaseType.H2, sqlFile, new Version(1, 0), "&<>\"");

    String content = Files.readString(changeLogFile.toPath());
    assertTrue(content.contains("&amp;"), "& should be escaped to &amp;");
    assertTrue(content.contains("&lt;"), "< should be escaped to &lt;");
    assertTrue(content.contains("&gt;"), "> should be escaped to &gt;");
    assertTrue(content.contains("&quot;"), "\" should be escaped to &quot;");
    assertFalse(
        content.contains("endDelimiter=\"&<>\"\""),
        "raw special chars should not appear unescaped");
  }
}

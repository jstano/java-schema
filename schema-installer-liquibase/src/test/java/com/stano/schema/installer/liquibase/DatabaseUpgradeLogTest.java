package com.stano.schema.installer.liquibase;

import liquibase.database.core.H2Database;
import liquibase.database.jvm.JdbcConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DatabaseUpgradeLog")
class DatabaseUpgradeLogTest {

  private Connection conn;
  private H2Database h2db;
  private DatabaseUpgradeLog log;

  @BeforeEach
  void setUp() throws Exception {
    conn = DriverManager.getConnection("jdbc:h2:mem:test_" + System.nanoTime() + ";MODE=PostgreSQL");
    h2db = new H2Database();
    h2db.setConnection(new JdbcConnection(conn));
    log = new DatabaseUpgradeLog();
  }

  @AfterEach
  void tearDown() throws Exception {
    if (h2db != null) {
      h2db.close();
    }
    if (conn != null && !conn.isClosed()) {
      conn.close();
    }
  }

  @Test
  @DisplayName("start creates databaseupgradelog table when absent")
  void startCreatesDatabaseupgradelogTableWhenAbsent() {
    assertFalse(tableExists("databaseupgradelog"));

    int id = log.start(h2db, conn, "V1__install.sql");

    assertTrue(tableExists("databaseupgradelog"));
    assertTrue(id > 0);
  }

  @Test
  @DisplayName("start inserts a row with StartDateTime and ChangeLogName")
  void startInsertsRowWithStartDateTimeAndChangeLogName() {
    int id = log.start(h2db, conn, "some/path/to/V1__test.sql");

    Map<String, Object> row = getLogRow(id);
    assertNotNull(row);
    assertEquals("V1__test.sql", row.get("ChangeLogName"));
    assertNotNull(row.get("StartDateTime"));
    assertNull(row.get("EndDateTime"));
    assertNull(row.get("Error"));
  }

  @Test
  @DisplayName("finish updates EndDateTime when no error")
  void finishUpdatesEndDateTimeWhenNoError() {
    int id = log.start(h2db, conn, "V1__test.sql");

    log.finish(h2db, conn, id, null);

    Map<String, Object> row = getLogRow(id);
    assertNotNull(row);
    assertNotNull(row.get("EndDateTime"));
    assertNull(row.get("Error"));
  }

  @Test
  @DisplayName("finish updates EndDateTime and Error when error provided")
  void finishUpdatesEndDateTimeAndErrorWhenErrorProvided() {
    int id = log.start(h2db, conn, "V1__test.sql");

    log.finish(h2db, conn, id, "Something went wrong");

    Map<String, Object> row = getLogRow(id);
    assertNotNull(row);
    assertNotNull(row.get("EndDateTime"));
    assertEquals("Something went wrong", row.get("Error"));
  }

  @Test
  @DisplayName("start is idempotent when table already exists")
  void startIsIdempotentWhenTableAlreadyExists() {
    log.start(h2db, conn, "V1__first.sql");
    int id2 = log.start(h2db, conn, "V2__second.sql");

    assertTrue(id2 > 0);
  }

  private boolean tableExists(String tableName) {
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("select * from " + tableName + " limit 1")) {
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  private Map<String, Object> getLogRow(int id) {
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("select * from databaseupgradelog where ID = " + id)) {
      if (rs.next()) {
        Map<String, Object> row = new HashMap<>();
        row.put("ChangeLogName", rs.getString("ChangeLogName"));
        row.put("StartDateTime", rs.getTimestamp("StartDateTime"));
        row.put("EndDateTime", rs.getTimestamp("EndDateTime"));
        row.put("Error", rs.getString("Error"));
        return row;
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}

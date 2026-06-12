package com.stano.schema.installer.schemacontext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.stano.schema.model.Version;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DatabaseVersionServices")
class DatabaseVersionServicesTest {

  private Connection conn;
  private DatabaseVersionServices services;

  @BeforeEach
  void setUp() throws Exception {
    conn =
        DriverManager.getConnection("jdbc:h2:mem:test_" + System.nanoTime() + ";MODE=PostgreSQL");
    services = new DatabaseVersionServices();
  }

  @AfterEach
  void tearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }

  @Test
  @DisplayName("getVersion creates databaseversion table when absent and returns initial version")
  void getVersionCreatesTableWhenAbsentAndReturnsInitialVersion() {
    Version version = services.getVersion(conn);
    assertNotNull(version);
    assertEquals("01.00", version.toString());
  }

  @Test
  @DisplayName("setVersion updates the stored version and getVersion returns the new value")
  void setVersionUpdatesVersionAndGetVersionReturnsNewValue() {
    services.getVersion(conn);
    services.setVersion(conn, new Version(2, 0));
    assertEquals("02.00", services.getVersion(conn).toString());
  }

  @Test
  @DisplayName("getVersion is idempotent when databaseversion table already exists")
  void getVersionIsIdempotentWhenTableAlreadyExists() {
    services.getVersion(conn);
    Version second = services.getVersion(conn);
    assertEquals("01.00", second.toString());
  }

  @Test
  @DisplayName("databaseversion table is created with correct schema")
  void databaseversionTableIsCreatedWithCorrectSchema() throws Exception {
    services.getVersion(conn);

    try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select version from databaseversion")) {
      assertNotNull(rs);
    }
  }
}

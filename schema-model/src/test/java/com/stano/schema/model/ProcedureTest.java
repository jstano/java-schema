package com.stano.schema.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProcedureTest {
  @ParameterizedTest
  @MethodSource("constructorProvider")
  @DisplayName("constructor should set fields and getters should return them for various database types")
  void testConstructor(String schemaName, String name, DatabaseType dbType, String sql) {
    Procedure proc = new Procedure(schemaName, name, dbType, sql);

    assertEquals(schemaName, proc.getSchemaName());
    assertEquals(name, proc.getName());
    assertEquals(dbType, proc.getDatabaseType());
    assertEquals(sql, proc.getSql());
  }

  static Stream<Arguments> constructorProvider() {
    return Stream.of(
      Arguments.of("app", "pr_cleanup", DatabaseType.MYSQL, "CREATE PROCEDURE pr_cleanup() BEGIN SELECT 1; END"),
      Arguments.of("public", "pr_total", DatabaseType.POSTGRES, "create procedure pr_total() language plpgsql as $$ begin /* noop */ end $$;"),
      Arguments.of("dbo", "pr_compute", DatabaseType.SQL_SERVER, "CREATE PROCEDURE dbo.pr_compute AS BEGIN SELECT 42 END")
    );
  }

  @Test
  @DisplayName("supports null SQL and still returns correct fields")
  void testConstructorWithNullSql() {
    Procedure proc = new Procedure("util", "pr_empty", DatabaseType.H2, null);

    assertEquals("util", proc.getSchemaName());
    assertEquals("pr_empty", proc.getName());
    assertEquals(DatabaseType.H2, proc.getDatabaseType());
    assertNull(proc.getSql());
  }
}

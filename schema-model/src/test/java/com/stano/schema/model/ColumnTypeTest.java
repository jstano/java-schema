package com.stano.schema.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ColumnTypeTest {
  @Test
  @DisplayName("getColumnType should return the correct ColumnType for all supported types")
  void testGetColumnType() {
    assertEquals(25, ColumnType.values().length);
    assertEquals(ColumnType.SEQUENCE, ColumnType.getColumnType("sequence"));
    assertEquals(ColumnType.LONGSEQUENCE, ColumnType.getColumnType("longsequence"));
    assertEquals(ColumnType.BYTE, ColumnType.getColumnType("byte"));
    assertEquals(ColumnType.SHORT, ColumnType.getColumnType("short"));
    assertEquals(ColumnType.INT, ColumnType.getColumnType("int"));
    assertEquals(ColumnType.LONG, ColumnType.getColumnType("long"));
    assertEquals(ColumnType.FLOAT, ColumnType.getColumnType("float"));
    assertEquals(ColumnType.DOUBLE, ColumnType.getColumnType("double"));
    assertEquals(ColumnType.DECIMAL, ColumnType.getColumnType("decimal"));
    assertEquals(ColumnType.BOOLEAN, ColumnType.getColumnType("boolean"));
    assertEquals(ColumnType.DATE, ColumnType.getColumnType("date"));
    assertEquals(ColumnType.DATETIME, ColumnType.getColumnType("datetime"));
    assertEquals(ColumnType.TIME, ColumnType.getColumnType("time"));
    assertEquals(ColumnType.TIMESTAMPTZ, ColumnType.getColumnType("timestamptz"));
    assertEquals(ColumnType.TIMESTAMP, ColumnType.getColumnType("timestamp"));
    assertEquals(ColumnType.CHAR, ColumnType.getColumnType("char"));
    assertEquals(ColumnType.VARCHAR, ColumnType.getColumnType("varchar"));
    assertEquals(ColumnType.ENUM, ColumnType.getColumnType("enum"));
    assertEquals(ColumnType.TEXT, ColumnType.getColumnType("text"));
    assertEquals(ColumnType.CITEXT, ColumnType.getColumnType("citext"));
    assertEquals(ColumnType.CSTEXT, ColumnType.getColumnType("cstext"));
    assertEquals(ColumnType.BINARY, ColumnType.getColumnType("binary"));
    assertEquals(ColumnType.UUID, ColumnType.getColumnType("uuid"));
    assertEquals(ColumnType.JSON, ColumnType.getColumnType("json"));
  }

  @Test
  @DisplayName(
      "getColumnType should throw an IllegalArgumentException if the type name is not valid")
  void testGetColumnTypeInvalid() {
    assertThrows(IllegalArgumentException.class, () -> ColumnType.getColumnType("invalid"));
  }

  @Test
  @DisplayName("isText should return true for all text types")
  void testIsText() {
    assertFalse(ColumnType.SEQUENCE.isText());
    assertFalse(ColumnType.LONGSEQUENCE.isText());
    assertFalse(ColumnType.BYTE.isText());
    assertFalse(ColumnType.SHORT.isText());
    assertFalse(ColumnType.INT.isText());
    assertFalse(ColumnType.LONG.isText());
    assertFalse(ColumnType.FLOAT.isText());
    assertFalse(ColumnType.DOUBLE.isText());
    assertFalse(ColumnType.DECIMAL.isText());
    assertFalse(ColumnType.BOOLEAN.isText());
    assertFalse(ColumnType.DATE.isText());
    assertFalse(ColumnType.DATETIME.isText());
    assertFalse(ColumnType.TIME.isText());
    assertFalse(ColumnType.TIMESTAMPTZ.isText());
    assertFalse(ColumnType.TIMESTAMP.isText());
    assertTrue(ColumnType.CHAR.isText());
    assertTrue(ColumnType.VARCHAR.isText());
    assertTrue(ColumnType.ENUM.isText());
    assertTrue(ColumnType.TEXT.isText());
    assertTrue(ColumnType.CITEXT.isText());
    assertTrue(ColumnType.CSTEXT.isText());
    assertFalse(ColumnType.BINARY.isText());
    assertTrue(ColumnType.UUID.isText());
    assertTrue(ColumnType.JSON.isText());
  }

  @Test
  @DisplayName("isNumeric should return true for all numeric types")
  void testIsNumeric() {
    assertTrue(ColumnType.SEQUENCE.isNumeric());
    assertTrue(ColumnType.LONGSEQUENCE.isNumeric());
    assertTrue(ColumnType.BYTE.isNumeric());
    assertTrue(ColumnType.SHORT.isNumeric());
    assertTrue(ColumnType.INT.isNumeric());
    assertTrue(ColumnType.LONG.isNumeric());
    assertTrue(ColumnType.FLOAT.isNumeric());
    assertTrue(ColumnType.DOUBLE.isNumeric());
    assertTrue(ColumnType.DECIMAL.isNumeric());
    assertFalse(ColumnType.BOOLEAN.isNumeric());
    assertFalse(ColumnType.DATE.isNumeric());
    assertFalse(ColumnType.DATETIME.isNumeric());
    assertFalse(ColumnType.TIME.isNumeric());
    assertFalse(ColumnType.TIMESTAMPTZ.isNumeric());
    assertFalse(ColumnType.TIMESTAMP.isNumeric());
    assertFalse(ColumnType.CHAR.isNumeric());
    assertFalse(ColumnType.VARCHAR.isNumeric());
    assertFalse(ColumnType.ENUM.isNumeric());
    assertFalse(ColumnType.TEXT.isNumeric());
    assertFalse(ColumnType.CITEXT.isNumeric());
    assertFalse(ColumnType.CSTEXT.isNumeric());
    assertFalse(ColumnType.BINARY.isNumeric());
    assertFalse(ColumnType.UUID.isNumeric());
    assertFalse(ColumnType.JSON.isNumeric());
  }
}

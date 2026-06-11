package com.stano.schema.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BooleanModeTest {
  @Test
  @DisplayName("enum should contain exactly the expected values in order")
  void testEnumValues() {
    String[] names = new String[BooleanMode.values().length];
    for (int i = 0; i < BooleanMode.values().length; i++) {
      names[i] = BooleanMode.values()[i].name();
    }
    assertArrayEquals(new String[]{"NATIVE", "YES_NO", "YN"}, names);
  }

  @ParameterizedTest
  @MethodSource("valueOfProvider")
  @DisplayName("valueOf should return the correct enum constant for each name")
  void testValueOf(String name, BooleanMode constant) {
    assertEquals(constant, BooleanMode.valueOf(name));
    assertEquals(name, constant.name());
  }

  static Stream<Arguments> valueOfProvider() {
    return Stream.of(
      Arguments.of("NATIVE", BooleanMode.NATIVE),
      Arguments.of("YES_NO", BooleanMode.YES_NO),
      Arguments.of("YN", BooleanMode.YN)
    );
  }

  @Test
  @DisplayName("all enum values should be unique")
  void testEnumValuesUnique() {
    BooleanMode[] values = BooleanMode.values();
    assertEquals(new HashSet<>(java.util.Arrays.asList(values)).size(), values.length);
  }

  @Test
  @DisplayName("Column.needsCheckConstraints should depend on BooleanMode for BOOLEAN type")
  void testColumnNeedsCheckConstraints() {
    Column col = new Column("flag", ColumnType.BOOLEAN, 0, false);

    assertFalse(col.needsCheckConstraints(BooleanMode.NATIVE));
    assertTrue(col.needsCheckConstraints(BooleanMode.YES_NO));
    assertTrue(col.needsCheckConstraints(BooleanMode.YN));
  }

  @Test
  @DisplayName("Column.needsCheckConstraints true when explicit constraints regardless of BooleanMode")
  void testColumnNeedsCheckConstraintsWithExplicitConstraints() {
    Column colWithCheck = new Column("x", ColumnType.INT, 0, false, "x > 0");

    Column colWithAll = new Column(
      "y",
      ColumnType.DECIMAL,
      10,
      2,
      false,
      null,
      null,
      null,
      "0",
      "100",
      null,
      null
    );

    assertTrue(colWithCheck.needsCheckConstraints(BooleanMode.NATIVE));
    assertTrue(colWithCheck.needsCheckConstraints(BooleanMode.YES_NO));
    assertTrue(colWithAll.needsCheckConstraints(BooleanMode.NATIVE));
    assertTrue(colWithAll.needsCheckConstraints(BooleanMode.YN));
  }
}

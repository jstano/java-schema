package com.stano.schema.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VerifyTypeTest {
  @Test
  @DisplayName("enum should contain exactly the expected values in order")
  void testEnumValues() {
    VerifyType[] values = VerifyType.values();
    assertEquals(3, values.length);
    assertEquals("DATE", values[0].name());
    assertEquals("SUM", values[1].name());
    assertEquals("GROUP_BY", values[2].name());
  }

  @ParameterizedTest
  @MethodSource("valueOfProvider")
  @DisplayName("valueOf should return the correct enum constant for each name")
  void testValueOf(String name, VerifyType constant) {
    assertEquals(constant, VerifyType.valueOf(name));
    assertEquals(name, constant.name());
  }

  static Stream<Arguments> valueOfProvider() {
    return Stream.of(
        Arguments.of("DATE", VerifyType.DATE),
        Arguments.of("SUM", VerifyType.SUM),
        Arguments.of("GROUP_BY", VerifyType.GROUP_BY));
  }

  @Test
  @DisplayName("all enum values should be unique")
  void testEnumValuesUnique() {
    VerifyType[] values = VerifyType.values();
    assertEquals(new HashSet<>(java.util.Arrays.asList(values)).size(), values.length);
  }
}

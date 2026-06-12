package com.stano.schema.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AggregationFrequencyTest {
  @Test
  @DisplayName("enum should contain exactly the expected values in order")
  void testEnumValues() {
    AggregationFrequency[] values = AggregationFrequency.values();
    assertEquals(4, values.length);
    assertEquals("DAILY", values[0].name());
    assertEquals("WEEKLY", values[1].name());
    assertEquals("MONTHLY", values[2].name());
    assertEquals("YEARLY", values[3].name());
  }

  @ParameterizedTest
  @MethodSource("valueOfProvider")
  @DisplayName("valueOf should return the correct enum constant for each name")
  void testValueOf(String name, AggregationFrequency constant) {
    assertEquals(constant, AggregationFrequency.valueOf(name));
    assertEquals(name, constant.name());
  }

  static Stream<Arguments> valueOfProvider() {
    return Stream.of(
        Arguments.of("DAILY", AggregationFrequency.DAILY),
        Arguments.of("WEEKLY", AggregationFrequency.WEEKLY),
        Arguments.of("MONTHLY", AggregationFrequency.MONTHLY),
        Arguments.of("YEARLY", AggregationFrequency.YEARLY));
  }

  @Test
  @DisplayName("all enum values should be unique")
  void testEnumValuesUnique() {
    AggregationFrequency[] values = AggregationFrequency.values();
    assertEquals(new HashSet<>(java.util.Arrays.asList(values)).size(), values.length);
  }
}

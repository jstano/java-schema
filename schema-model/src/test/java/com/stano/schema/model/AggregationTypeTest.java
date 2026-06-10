package com.stano.schema.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AggregationTypeTest {
  @Test
  @DisplayName("enum should contain exactly the expected values in order")
  void testEnumValues() {
    AggregationType[] values = AggregationType.values();
    assertEquals(2, values.length);
    assertEquals("SUM", values[0].name());
    assertEquals("COUNT", values[1].name());
  }

  @ParameterizedTest
  @MethodSource("valueOfProvider")
  @DisplayName("valueOf should return the correct enum constant for each name")
  void testValueOf(String name, AggregationType constant) {
    assertEquals(constant, AggregationType.valueOf(name));
    assertEquals(name, constant.name());
  }

  static Stream<Arguments> valueOfProvider() {
    return Stream.of(
      Arguments.of("SUM", AggregationType.SUM),
      Arguments.of("COUNT", AggregationType.COUNT)
    );
  }

  @Test
  @DisplayName("all enum values should be unique")
  void testEnumValuesUnique() {
    AggregationType[] values = AggregationType.values();
    assertEquals(new HashSet<>(java.util.Arrays.asList(values)).size(), values.length);
  }

  @Test
  @DisplayName("can be used in AggregationColumn without errors")
  void testAggregationColumnIntegration() {
    AggregationColumn colSum = new AggregationColumn(AggregationType.SUM, "amount", "total_amount");
    AggregationColumn colCount = new AggregationColumn(AggregationType.COUNT, "id", "cnt");

    assertEquals(AggregationType.SUM, colSum.getAggregationType());
    assertEquals(AggregationType.COUNT, colCount.getAggregationType());
  }
}

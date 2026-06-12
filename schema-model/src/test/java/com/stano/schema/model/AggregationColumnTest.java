package com.stano.schema.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AggregationColumnTest {
  @ParameterizedTest
  @MethodSource("constructorProvider")
  void testConstructor(AggregationType type, String source, String destination) {
    AggregationColumn col = new AggregationColumn(type, source, destination);

    assertEquals(type, col.getAggregationType());
    assertEquals(source, col.getSourceColumn());
    assertEquals(destination, col.getDestinationColumn());
  }

  static Stream<Arguments> constructorProvider() {
    return Stream.of(
        Arguments.of(AggregationType.SUM, "amount", "total_amount"),
        Arguments.of(AggregationType.COUNT, "id", "count_id"));
  }
}

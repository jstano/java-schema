package com.stano.schema.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AggregationGroupTest {
  @ParameterizedTest
  @MethodSource("constructorProvider")
  void testConstructor(String source, String sourceDerivedFrom, String destination) {
    AggregationGroup group = new AggregationGroup(source, sourceDerivedFrom, destination);

    assertEquals(source, group.getSource());
    assertEquals(sourceDerivedFrom, group.getSourceDerivedFrom());
    assertEquals(destination, group.getDestination());
  }

  static Stream<Arguments> constructorProvider() {
    return Stream.of(
        Arguments.of("country", null, "country"), Arguments.of("city", "country", "city"));
  }
}

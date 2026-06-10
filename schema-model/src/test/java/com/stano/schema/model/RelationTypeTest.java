package com.stano.schema.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RelationTypeTest {
  @Test
  @DisplayName("enum should contain exactly the expected values in order")
  void testEnumValues() {
    RelationType[] values = RelationType.values();
    assertEquals(4, values.length);
    assertEquals("CASCADE", values[0].name());
    assertEquals("ENFORCE", values[1].name());
    assertEquals("SETNULL", values[2].name());
    assertEquals("DONOTHING", values[3].name());
  }

  @ParameterizedTest
  @MethodSource("valueOfProvider")
  @DisplayName("valueOf should return the correct enum constant for each name")
  void testValueOf(String name, RelationType constant) {
    assertEquals(constant, RelationType.valueOf(name));
    assertEquals(name, constant.name());
  }

  static Stream<Arguments> valueOfProvider() {
    return Stream.of(
      Arguments.of("CASCADE", RelationType.CASCADE),
      Arguments.of("ENFORCE", RelationType.ENFORCE),
      Arguments.of("SETNULL", RelationType.SETNULL),
      Arguments.of("DONOTHING", RelationType.DONOTHING)
    );
  }

  @Test
  @DisplayName("all enum values should be unique")
  void testEnumValuesUnique() {
    RelationType[] values = RelationType.values();
    assertEquals(new HashSet<>(java.util.Arrays.asList(values)).size(), values.length);
  }

  @Test
  @DisplayName("can be used in Relation without errors")
  void testRelationIntegration() {
    Relation rel1 = new Relation("orders", "user_id", "users", "id", RelationType.CASCADE, false);
    Relation rel2 = new Relation("orders", "user_id", "users", "id", RelationType.SETNULL, true);

    assertEquals(RelationType.CASCADE, rel1.getType());
    assertFalse(rel1.isDisableUsageChecking());
    assertEquals(RelationType.SETNULL, rel2.getType());
    assertTrue(rel2.isDisableUsageChecking());
  }
}

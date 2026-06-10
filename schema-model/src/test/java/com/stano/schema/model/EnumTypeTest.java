package com.stano.schema.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnumTypeTest {
  @Test
  @DisplayName("constructor should set name and values should be empty initially")
  void testConstructor() {
    EnumType et = new EnumType("Color");

    assertEquals("Color", et.getName());
    assertTrue(et.getValues().isEmpty());
  }

  @Test
  @DisplayName("addValue should append EnumValue instances preserving order")
  void testAddValue() {
    EnumType et = new EnumType("Color");

    et.addValue(new EnumValue("RED", "R"));
    et.addValue(new EnumValue("GREEN", "G"));
    et.addValue(new EnumValue("BLUE", "B"));

    List<EnumValue> values = et.getValues();
    assertEquals(3, values.size());
    assertEquals("RED", values.get(0).getName());
    assertEquals("GREEN", values.get(1).getName());
    assertEquals("BLUE", values.get(2).getName());
    assertEquals("R", values.get(0).getCode());
    assertEquals("G", values.get(1).getCode());
    assertEquals("B", values.get(2).getCode());
  }

  @Test
  @DisplayName("EnumValue.getCode should default to name when code is null")
  void testEnumValueCodeDefault() {
    EnumValue v1 = new EnumValue("ACTIVE", null);
    EnumValue v2 = new EnumValue("INACTIVE", "I");

    assertEquals("ACTIVE", v1.getName());
    assertEquals("ACTIVE", v1.getCode());
    assertEquals("INACTIVE", v2.getName());
    assertEquals("I", v2.getCode());
  }

  @Test
  @DisplayName("getValues returns a live list reflecting mutations (current contract)")
  void testGetValuesLiveList() {
    EnumType et = new EnumType("Status");
    et.addValue(new EnumValue("OPEN", null));

    List<EnumValue> listRef = et.getValues();
    listRef.add(new EnumValue("CLOSED", "C"));

    List<EnumValue> values = et.getValues();
    assertEquals(2, values.size());
    assertEquals("OPEN", values.get(0).getName());
    assertEquals("CLOSED", values.get(1).getName());
    assertEquals("OPEN", values.get(0).getCode());
    assertEquals("C", values.get(1).getCode());
  }

  @Test
  @DisplayName("integration: build enum type with mixed null and non-null codes")
  void testEnumTypeIntegration() {
    EnumType et = new EnumType("Priority");
    et.addValue(new EnumValue("HIGH", "H"));
    et.addValue(new EnumValue("MEDIUM", null));
    et.addValue(new EnumValue("LOW", "L"));

    assertEquals("Priority", et.getName());
    List<EnumValue> values = et.getValues();
    assertEquals(3, values.size());
    assertEquals("HIGH", values.get(0).getName());
    assertEquals("MEDIUM", values.get(1).getName());
    assertEquals("LOW", values.get(2).getName());
    assertEquals("H", values.get(0).getCode());
    assertEquals("MEDIUM", values.get(1).getCode());
    assertEquals("L", values.get(2).getCode());
  }
}

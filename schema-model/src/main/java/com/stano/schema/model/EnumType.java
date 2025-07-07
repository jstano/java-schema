package com.stano.schema.model;

import java.util.ArrayList;
import java.util.List;

public class EnumType {
  private final String name;
  private final List<EnumValue> values = new ArrayList<>();

  public EnumType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<EnumValue> getValues() {
    return values;
  }

  public void addValue(EnumValue value) {
    values.add(value);
  }
}

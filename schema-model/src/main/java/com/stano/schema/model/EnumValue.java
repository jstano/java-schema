package com.stano.schema.model;

public class EnumValue {
  private final String name;
  private final String code;

  public EnumValue(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    if (code != null) {
      return code;
    }

    return name;
  }
}

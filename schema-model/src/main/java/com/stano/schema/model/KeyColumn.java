package com.stano.schema.model;

public class KeyColumn {
  private final String name;
  private final String expression;

  public KeyColumn(String name) {
    this.name = name;
    this.expression = null;
  }

  public KeyColumn(String name, String expression) {
    this.name = name;
    this.expression = expression;
  }

  public String getName() {
    return name;
  }

  public String getExpression() {
    return expression;
  }
}

package com.stano.schema.model;

public class Column {
  private final String name;
  private final ColumnType type;
  private final int length;
  private final int scale;
  private final boolean required;
  private final String checkConstraint;
  private final String defaultConstraint;
  private final String generated;
  private final String minValue;
  private final String maxValue;
  private final String enumType;
  private final ColumnType elementType;
  private final boolean ignoreCase;

  public Column(String name,
                ColumnType type,
                int length,
                boolean required) {
    this.name = name;
    this.type = type;
    this.length = length;
    this.scale = 0;
    this.required = required;
    this.checkConstraint = null;
    this.defaultConstraint = null;
    this.generated = null;
    this.minValue = null;
    this.maxValue = null;
    this.enumType = null;
    this.elementType = null;
    this.ignoreCase = false;
  }

  public Column(String name,
                ColumnType type,
                int length,
                boolean required,
                String checkConstraint) {
    this.name = name;
    this.type = type;
    this.length = length;
    this.scale = 0;
    this.required = required;
    this.checkConstraint = checkConstraint;
    this.defaultConstraint = null;
    this.generated = null;
    this.minValue = null;
    this.maxValue = null;
    this.enumType = null;
    this.elementType = null;
    this.ignoreCase = false;
  }

  public Column(String name,
                ColumnType type,
                int length,
                int scale,
                boolean required,
                String checkConstraint,
                String defaultConstraint,
                String generated,
                String minValue,
                String maxValue,
                String enumType,
                ColumnType elementType,
                boolean ignoreCase) {
    this.name = name;
    this.type = type;
    this.length = length;
    this.scale = scale;
    this.required = required;
    this.checkConstraint = checkConstraint;
    this.defaultConstraint = defaultConstraint;
    this.generated = generated;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.enumType = enumType;
    this.elementType = elementType;
    this.ignoreCase = ignoreCase;
  }

  public String getName() {
    return name;
  }

  public ColumnType getType() {
    return type;
  }

  public int getLength() {
    return length;
  }

  public int getScale() {
    return scale;
  }

  public boolean isRequired() {
    return required;
  }

  public String getCheckConstraint() {
    return checkConstraint;
  }

  public String getDefaultConstraint() {
    return defaultConstraint;
  }

  public String getGenerated() {
    return generated;
  }

  public String getMinValue() {
    return minValue;
  }

  public String getMaxValue() {
    return maxValue;
  }

  public String getEnumType() {
    return enumType;
  }

  public ColumnType getElementType() {
    return elementType;
  }

  public boolean isIgnoreCase() {
    return ignoreCase;
  }

  public boolean needsCheckConstraints(BooleanMode booleanMode) {
    return checkConstraint != null || minValue != null || maxValue != null || enumType != null || (type == ColumnType.BOOLEAN && booleanMode != BooleanMode.NATIVE);
  }

  public boolean hasMinOrMaxValue() {
    return minValue != null || maxValue != null;
  }
}

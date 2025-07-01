package com.stano.schema.importer;

public record KeyDataColumn(
  String columnName,
  int ordinalPosition
) implements Comparable<KeyDataColumn> {
  @Override
  public int compareTo(KeyDataColumn other) {
    return ordinalPosition - other.ordinalPosition;
  }
}

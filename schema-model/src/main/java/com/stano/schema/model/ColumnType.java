package com.stano.schema.model;

import java.util.stream.Stream;

public enum ColumnType {
  SEQUENCE,
  LONGSEQUENCE,
  BYTE,
  SHORT,
  INT,
  LONG,
  FLOAT,
  DOUBLE,
  DECIMAL,
  BOOLEAN,
  DATE,
  DATETIME,
  TIME,
  TIMESTAMP,
  CHAR,
  VARCHAR,
  ENUM,
  TEXT,
  BINARY,
  UUID,
  JSON,
  ARRAY;

  public boolean isText() {
    return this == CHAR || this == VARCHAR || this == ENUM || this == TEXT || this == JSON || this == UUID;
  }

  public boolean isNumeric() {
    return this == SEQUENCE || this == LONGSEQUENCE || this == BYTE || this == SHORT || this == INT || this == LONG || this == FLOAT || this == DOUBLE || this == DECIMAL;
  }

  public static ColumnType getColumnType(String typeName) {
    return Stream.of(values())
                 .filter(it -> it.name().equals(typeName.toUpperCase()))
                 .findFirst()
                 .orElseThrow(() -> new IllegalArgumentException("The type '" + typeName + "' is not valid."));
  }
}

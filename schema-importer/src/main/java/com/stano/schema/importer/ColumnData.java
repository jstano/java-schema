package com.stano.schema.importer;

public record ColumnData(
  String columnName,
  int dataType,
  String typeName,
  String columnDef,
  int columnSize,
  int decimalDigits,
  String nullable,
  String autoIncrement,
  String generatedColumn
) {
}

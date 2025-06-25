package com.stano.schema.importer;

public record PrimaryKeyData(
  String tableName,
  String columnName,
  int keySequence
) implements Comparable<PrimaryKeyData> {
  @Override
  public int compareTo(PrimaryKeyData other) {
    return keySequence - other.keySequence;
  }
}

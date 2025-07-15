package com.stano.schema.model;

import java.util.List;

public class Key {
  private final KeyType type;
  private final List<KeyColumn> columns;
  private final boolean cluster;
  private final boolean compress;
  private final boolean unique;
  private final String include;

  public Key(KeyType type, List<KeyColumn> columns, boolean cluster, boolean compress, boolean unique, String include) {
    this.type = type;
    this.columns = List.copyOf(columns);
    this.cluster = cluster;
    this.compress = compress;
    this.unique = unique;
    this.include = include;
  }

  public Key(KeyType type, List<KeyColumn> columns) {
    this.type = type;
    this.columns = List.copyOf(columns);
    this.cluster = false;
    this.compress = false;
    this.unique = false;
    this.include = null;
  }

  public KeyType getType() {
    return type;
  }

  public List<KeyColumn> getColumns() {
    return columns;
  }

  public boolean isCluster() {
    return cluster;
  }

  public boolean isCompress() {
    return compress;
  }

  public boolean isUnique() {
    return unique;
  }

  public String getInclude() {
    return include;
  }

  public boolean containsColumn(String columnName) {
    return columns.stream().map(KeyColumn::getName).anyMatch(colName -> colName.equals(columnName));
  }

  public String getColumnsAsString() {
    return String.join(",", columns.stream().map(KeyColumn::getName).toArray(String[]::new));
  }
}

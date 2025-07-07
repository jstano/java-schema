package com.stano.schema.model;

public class Relation {
  private final String fromTableName;
  private final String fromColumnName;
  private final String toTableName;
  private final String toColumnName;
  private final RelationType type;
  private final boolean disableUsageChecking;

  public Relation(String fromTableName,
                  String fromColumnName,
                  String toTableName,
                  String toColumnName,
                  RelationType type,
                  boolean disableUsageChecking) {
    this.fromTableName = fromTableName;
    this.fromColumnName = fromColumnName;
    this.toTableName = toTableName;
    this.toColumnName = toColumnName;
    this.type = type;
    this.disableUsageChecking = disableUsageChecking;
  }

  public String getFromTableName() {
    return fromTableName;
  }

  public String getFromColumnName() {
    return fromColumnName;
  }

  public String getToTableName() {
    return toTableName;
  }

  public String getToColumnName() {
    return toColumnName;
  }

  public RelationType getType() {
    return type;
  }

  public boolean isDisableUsageChecking() {
    return disableUsageChecking;
  }
}

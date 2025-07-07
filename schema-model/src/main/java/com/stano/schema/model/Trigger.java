package com.stano.schema.model;

public class Trigger {
  private final String triggerText;
  private final TriggerType triggerType;
  private final DatabaseType databaseType;

  public Trigger(String triggerText, TriggerType triggerType, DatabaseType databaseType) {
    this.triggerText = triggerText;
    this.triggerType = triggerType;
    this.databaseType = databaseType;
  }

  public String getTriggerText() {
    return triggerText;
  }

  public TriggerType getTriggerType() {
    return triggerType;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }
}

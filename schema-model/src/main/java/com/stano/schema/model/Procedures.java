package com.stano.schema.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Procedures {
  private final DatabaseType databaseType;
  private final List<Procedure> procedures;

  public Procedures(DatabaseType databaseType, List<Procedure> procedures) {
    this.databaseType = databaseType;
    this.procedures = Collections.unmodifiableList(new ArrayList<>(procedures));
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }

  public List<Procedure> getProcedures() {
    return procedures;
  }
}

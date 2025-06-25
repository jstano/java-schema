package com.stano.schema.migrations;

public class MigrationException extends RuntimeException {
  public MigrationException(Throwable x) {
    super(x);
  }
}

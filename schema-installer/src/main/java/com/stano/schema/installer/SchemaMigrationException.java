package com.stano.schema.installer;

public class SchemaMigrationException extends RuntimeException {
  public SchemaMigrationException(Throwable x) {
    super(x);
  }
}

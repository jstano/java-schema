package com.stano.schema.installer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("SchemaMigrationException")
class SchemaMigrationExceptionTest {

  @Test
  @DisplayName("wraps cause and exposes it via getCause")
  void wrapsCauseAndExposesItViaGetCause() {
    RuntimeException cause = new RuntimeException("original");
    SchemaMigrationException ex = new SchemaMigrationException(cause);
    assertSame(cause, ex.getCause());
  }
}

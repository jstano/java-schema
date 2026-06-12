package com.stano.schema.installer.liquibase;

import static org.junit.jupiter.api.Assertions.assertSame;

import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LiquibaseRuntimeException")
class LiquibaseRuntimeExceptionTest {

  @Test
  @DisplayName("wraps cause and exposes it via getCause")
  void wrapsCauseAndExposesItViaGetCause() {
    LiquibaseException cause = new LiquibaseException("original");
    LiquibaseRuntimeException ex = new LiquibaseRuntimeException(cause);
    assertSame(cause, ex.getCause());
  }
}

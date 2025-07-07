package com.stano.schema.installer.liquibase;

import liquibase.database.ObjectQuotingStrategy;
import liquibase.database.core.PostgresDatabase;

public class CustomPostgresDatabase extends PostgresDatabase {
  public CustomPostgresDatabase() {
    quotingStrategy = ObjectQuotingStrategy.QUOTE_ONLY_RESERVED_WORDS;
  }

  @Override
  public ObjectQuotingStrategy getObjectQuotingStrategy() {
    return ObjectQuotingStrategy.QUOTE_ONLY_RESERVED_WORDS;
  }

  @Override
  public void setObjectQuotingStrategy(final ObjectQuotingStrategy quotingStrategy) {
    // ignore since getObjectQuotingStrategy always returns the same value
  }
}

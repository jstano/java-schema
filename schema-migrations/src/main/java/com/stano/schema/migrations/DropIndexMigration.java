package com.stano.schema.migrations;

import java.sql.Statement;

public class DropIndexMigration implements StatementAction<Void> {
  private final String tableName;
  private final String indexName;

  public DropIndexMigration(String tableName, String indexName) {
    this.tableName = tableName;
    this.indexName = indexName;
  }

  @Override
  public Void execute(Statement statement) {
    if (indexExists(statement)) {
      dropIndex(statement);
    }

    return null;
  }

  private Boolean indexExists(Statement statement) {
    return new IndexExistsMigration(indexName).execute(statement);
  }

  private Object dropIndex(Statement statement) {
    return new ExecuteSQLMigration(String.format("drop index %s.%s", tableName, indexName))
        .execute(statement);
  }
}

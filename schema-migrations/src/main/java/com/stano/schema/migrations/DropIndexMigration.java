package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.Statement;

public class DropIndexMigration implements ExecuteWithStatement<Void> {
  private final String tableName;
  private final String indexName;

  public DropIndexMigration(String tableName, String indexName) {
    this.tableName = tableName;
    this.indexName = indexName;
  }

  @Override
  public Void executeWithStatement(Statement statement) {
    if (indexExists(statement)) {
      dropIndex(statement);
    }

    return null;
  }

  private Boolean indexExists(Statement statement) {
    return new IndexExistsMigration(indexName).executeWithStatement(statement);
  }

  private Object dropIndex(Statement statement) {
    return new ExecuteSQLMigration(String.format("drop index %s.%s", tableName, indexName)).executeWithStatement(statement);
  }
}

package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IndexExistsMigration implements ExecuteWithStatement<Boolean> {
  private final String indexName;

  public IndexExistsMigration(String indexName) {
    this.indexName = indexName;
  }

  @Override
  public Boolean executeWithStatement(Statement statement) {
    try (ResultSet rs = statement.executeQuery(String.format("select name from dbo.sysindexes where name = '%s'", indexName))) {
      return rs.next();
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

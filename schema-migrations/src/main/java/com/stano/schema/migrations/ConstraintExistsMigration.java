package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConstraintExistsMigration implements ExecuteWithStatement<Boolean> {
  private final String constraintName;

  public ConstraintExistsMigration(String constraintName) {
    this.constraintName = constraintName;
  }

  @Override
  public Boolean executeWithStatement(Statement statement) {
    //SELECT * FROM pg_constraint

    try (ResultSet rs = statement.executeQuery(String.format("select * from dbo.sysobjects where name = '%s'", constraintName))) {
      return rs.next();
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

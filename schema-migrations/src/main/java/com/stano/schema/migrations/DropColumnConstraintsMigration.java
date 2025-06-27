package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DropColumnConstraintsMigration implements ExecuteWithStatement<Void> {
  private final String table;
  private final String column;

  public DropColumnConstraintsMigration(String table, String column) {
    this.table = table;
    this.column = column;
  }

  @Override
  public Void executeWithStatement(Statement statement) {
    try {
      statement.execute(String.format("exec sp_helpconstraint %s", table));

      ResultSet rs = statement.getResultSet();
      rs.close();

      List<String> constraints = new ArrayList<>();

      if (statement.getMoreResults()) {
        rs = statement.getResultSet();

        try {
          String checkConstraintText = String.format("CHECK on column %s", column);
          String defaultConstraintText = String.format("DEFAULT on column %s", column);

          while (rs.next()) {
            String constraintType = rs.getString(1);
            String constraintName = rs.getString(2);

            if (constraintType.equalsIgnoreCase(checkConstraintText)) {
              constraints.add(constraintName);
            }
            else if (constraintType.equalsIgnoreCase(defaultConstraintText)) {
              constraints.add(constraintName);
            }
          }
        }
        finally {
          rs.close();
        }
      }

      for (String constraint : constraints) {
        statement.execute("alter table " + table + " drop constraint " + constraint);
      }

      return null;
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

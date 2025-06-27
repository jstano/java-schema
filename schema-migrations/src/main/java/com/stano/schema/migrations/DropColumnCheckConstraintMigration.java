package com.stano.schema.migrations;

import com.stano.jdbcutils.utils.ExecuteWithStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DropColumnCheckConstraintMigration implements ExecuteWithStatement<Object> {
  private final String table;
  private final String column;

  public DropColumnCheckConstraintMigration(String table, String column) {
    this.table = table;
    this.column = column;
  }

  @Override
  public Object executeWithStatement(Statement statement) {
    try {
      statement.execute(String.format("exec sp_helpconstraint %s", table));

      if (statement.getMoreResults()) {
        String checkConstraintText = String.format("CHECK on column %s", column);

        try (ResultSet rs = statement.getResultSet()) {
          while (rs.next()) {
            String constraintType = rs.getString(1);
            String constraintName = rs.getString(2);

            if (constraintType.equalsIgnoreCase(checkConstraintText)) {
              statement.execute("alter table " + table + " drop constraint " + constraintName);
              break;
            }
          }
        }
      }

      return null;
    }
    catch (SQLException x) {
      throw new MigrationException(x);
    }
  }
}

package com.stano.schema.migrations;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
interface StatementAction<T> {
  T execute(Statement statement) throws SQLException;
}

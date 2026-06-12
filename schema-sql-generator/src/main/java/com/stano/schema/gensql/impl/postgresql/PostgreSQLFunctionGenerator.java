package com.stano.schema.gensql.impl.postgresql;

import com.stano.schema.gensql.impl.common.FunctionGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Function;

public class PostgreSQLFunctionGenerator extends FunctionGenerator {

  protected PostgreSQLFunctionGenerator(SQLGenerator sqlGenerator) {

    super(sqlGenerator);
  }

  @Override
  protected void outputFunction(Function function) {

    sqlWriter.println(function.getSql() + statementSeparator);
    sqlWriter.println();
  }
}

package com.stano.schema.gensql.impl.postgresql;

import com.stano.schema.gensql.impl.common.ProcedureGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Procedure;

public class PostgreSQLProcedureGenerator extends ProcedureGenerator {

  protected PostgreSQLProcedureGenerator(SQLGenerator sqlGenerator) {

    super(sqlGenerator);
  }

  @Override
  protected void outputProcedure(Procedure procedure) {

    sqlWriter.println(procedure.getSql() + statementSeparator);
    sqlWriter.println();
  }
}

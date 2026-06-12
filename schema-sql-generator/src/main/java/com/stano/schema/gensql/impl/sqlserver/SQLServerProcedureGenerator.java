package com.stano.schema.gensql.impl.sqlserver;

import com.stano.schema.gensql.impl.common.ProcedureGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Procedure;

class SQLServerProcedureGenerator extends ProcedureGenerator {

  SQLServerProcedureGenerator(SQLGenerator sqlGenerator) {

    super(sqlGenerator);
  }

  @Override
  protected void outputProcedure(Procedure procedure) {

    sqlWriter.println(procedure.getSql() + statementSeparator);
    sqlWriter.println();
  }
}

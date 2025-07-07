package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.ProcedureGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Procedure;

class MSSQLProcedureGenerator extends ProcedureGenerator {

   MSSQLProcedureGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputProcedure(Procedure procedure) {

      sqlWriter.println(procedure.getSql() + statementSeparator);
      sqlWriter.println();
   }
}

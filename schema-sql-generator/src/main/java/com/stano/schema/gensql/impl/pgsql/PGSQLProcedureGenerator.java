package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.ProcedureGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Procedure;

public class PGSQLProcedureGenerator extends ProcedureGenerator {

   protected PGSQLProcedureGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputProcedure(Procedure procedure) {

      sqlWriter.println(procedure.getSql() + statementSeparator);
      sqlWriter.println();
   }
}

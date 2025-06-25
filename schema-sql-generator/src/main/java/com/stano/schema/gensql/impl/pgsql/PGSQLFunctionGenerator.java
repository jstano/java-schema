package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.FunctionGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Function;

public class PGSQLFunctionGenerator extends FunctionGenerator {

   protected PGSQLFunctionGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputFunction(Function function) {

      sqlWriter.println(function.getSql() + statementSeparator);
      sqlWriter.println();
   }
}

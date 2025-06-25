package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.FunctionGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Function;

class MSSQLFunctionGenerator extends FunctionGenerator {

   MSSQLFunctionGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputFunction(Function function) {

      String functionName = function.getName();

      sqlWriter.println("if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[" + functionName + "]') and objectproperty(id, N'IsScalarFunction') = 1)");
      sqlWriter.println("   drop function dbo." + functionName + statementSeparator);
      sqlWriter.println(function.getSql() + statementSeparator);
      sqlWriter.println();
   }
}

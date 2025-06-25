package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;
import com.stano.schema.model.View;

class MSSQLViewGenerator extends ViewGenerator {

   MSSQLViewGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected String getFullyQualifiedViewName(View view) {

      String schemaName = view.getSchemaName().equalsIgnoreCase("public") ? "dbo" : view.getSchemaName();
      return schemaName + "." + view.getName();
   }

   @Override
   protected void outputView(View view) {

      String viewName = getFullyQualifiedViewName(view);

      sqlWriter.println(String.format("/* %s */", viewName));
      sqlWriter.println("if exists (select name from dbo.sysobjects where name = '" + view.getName() + "' and type = 'V')");
      sqlWriter.println("   drop view " + viewName + statementSeparator);
      sqlWriter.println("create view " + viewName + " as");
      sqlWriter.println("   " + view.getSql() + statementSeparator);
      sqlWriter.println();
   }
}

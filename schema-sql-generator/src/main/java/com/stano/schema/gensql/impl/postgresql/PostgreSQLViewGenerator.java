package com.stano.schema.gensql.impl.postgresql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;
import com.stano.schema.model.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgreSQLViewGenerator extends ViewGenerator {

   private static final Logger LOGGER = LoggerFactory.getLogger(PostgreSQLViewGenerator.class);

   protected PostgreSQLViewGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputView(View view) {

      String viewName = getFullyQualifiedViewName(view);

      sqlWriter.println(String.format("/* %s */", viewName));
      sqlWriter.println("create or replace view " + viewName + " as");
      sqlWriter.println("   " + view.getSql() + statementSeparator);
      sqlWriter.println();
   }
}

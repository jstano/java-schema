package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;
import com.stano.schema.model.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PGSQLViewGenerator extends ViewGenerator {

   private static final Logger LOGGER = LoggerFactory.getLogger(PGSQLViewGenerator.class);

   protected PGSQLViewGenerator(SQLGenerator sqlGenerator) {

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

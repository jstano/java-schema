package com.stano.schema.gensql.impl.hsql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;
import com.stano.schema.model.View;

class HSQLViewGenerator extends ViewGenerator {

   HSQLViewGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputView(View view) {

      sqlWriter.println("drop view " + view.getName() + " if exists" + statementSeparator);
      sqlWriter.println("create view " + view.getName() + " as");
      sqlWriter.println("   " + view.getSql() + statementSeparator);
      sqlWriter.println();
   }
}

package com.stano.schema.gensql.impl.h2;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;
import com.stano.schema.model.View;

class H2ViewGenerator extends ViewGenerator {

   H2ViewGenerator(SQLGenerator sqlGenerator) {

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

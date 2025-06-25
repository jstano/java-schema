package com.stano.schema.gensql.impl.mysql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;
import com.stano.schema.model.View;

class MySQLViewGenerator extends ViewGenerator {

   MySQLViewGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputView(View view) {

   }
}

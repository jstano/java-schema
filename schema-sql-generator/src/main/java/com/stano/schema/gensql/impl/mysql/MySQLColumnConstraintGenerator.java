package com.stano.schema.gensql.impl.mysql;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class MySQLColumnConstraintGenerator extends ColumnConstraintGenerator {

   MySQLColumnConstraintGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

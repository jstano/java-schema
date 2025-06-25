package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class MSSQLColumnConstraintGenerator extends ColumnConstraintGenerator {

   MSSQLColumnConstraintGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

package com.stano.schema.gensql.impl.mysql;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Column;
import com.stano.schema.model.ColumnType;

class MySQLColumnConstraintGenerator extends ColumnConstraintGenerator {

   MySQLColumnConstraintGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected String getCheckConstraintSQL(Column column) {
      if (column.getType() == ColumnType.ENUM) {
         return null;
      }
      return super.getCheckConstraintSQL(column);
   }
}

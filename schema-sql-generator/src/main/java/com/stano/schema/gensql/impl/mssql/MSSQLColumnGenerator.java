package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.ColumnGenerator;
import com.stano.schema.gensql.impl.common.ColumnTypeGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.BooleanMode;

class MSSQLColumnGenerator extends ColumnGenerator {

   private final ColumnTypeGenerator columnTypeGenerator;

   MSSQLColumnGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);

      this.columnTypeGenerator = new MSSQLColumnTypeGenerator(sqlGenerator);
   }

   @Override
   protected ColumnTypeGenerator getColumnTypeGenerator() {

      return columnTypeGenerator;
   }

   @Override
   protected String convertBooleanDefaultConstraint(boolean defaultValue) {

      if (booleanMode == BooleanMode.YES_NO) {
         return defaultValue ? "'Yes'" : "'No'";
      }

      if (booleanMode == BooleanMode.YN) {
         return defaultValue ? "'Y'" : "'N'";
      }

      return defaultValue ? "1" : "0";
   }
}

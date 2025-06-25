package com.stano.schema.gensql.impl.mysql;

import com.stano.schema.gensql.impl.common.ColumnGenerator;
import com.stano.schema.gensql.impl.common.ColumnTypeGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class MySQLColumnGenerator extends ColumnGenerator {

   private final ColumnTypeGenerator columnTypeGenerator;

   MySQLColumnGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);

      this.columnTypeGenerator = new MySQLColumnTypeGenerator(sqlGenerator);
   }

   @Override
   protected ColumnTypeGenerator getColumnTypeGenerator() {

      return columnTypeGenerator;
   }
}

package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.OtherSqlGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class MSSQLOtherSqlGenerator extends OtherSqlGenerator {

   MSSQLOtherSqlGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputOtherSql(String sql) {

      sqlWriter.println(sql + statementSeparator);
      sqlWriter.println();
   }
}

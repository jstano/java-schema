package com.stano.schema.gensql.impl.postgresql;

import com.stano.schema.gensql.impl.common.OtherSqlGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

public class PostgreSQLOtherSqlGenerator extends OtherSqlGenerator {

   protected PostgreSQLOtherSqlGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputOtherSql(String sql) {

      sqlWriter.println(sql + statementSeparator);
      sqlWriter.println();
   }
}

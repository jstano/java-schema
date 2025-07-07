package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.OtherSqlGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

public class PGSQLOtherSqlGenerator extends OtherSqlGenerator {

   protected PGSQLOtherSqlGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputOtherSql(String sql) {

      sqlWriter.println(sql + statementSeparator);
      sqlWriter.println();
   }
}

package com.stano.schema.gensql.impl.sqlserver;

import com.stano.schema.gensql.impl.common.OtherSqlGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class SQLServerOtherSqlGenerator extends OtherSqlGenerator {

  SQLServerOtherSqlGenerator(SQLGenerator sqlGenerator) {

    super(sqlGenerator);
  }

  @Override
  protected void outputOtherSql(String sql) {

    sqlWriter.println(sql + statementSeparator);
    sqlWriter.println();
  }
}

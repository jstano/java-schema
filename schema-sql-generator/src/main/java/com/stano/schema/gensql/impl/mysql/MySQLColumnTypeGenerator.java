package com.stano.schema.gensql.impl.mysql;

import com.stano.schema.gensql.impl.common.ColumnTypeGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.Column;
import com.stano.schema.model.Schema;

class MySQLColumnTypeGenerator extends ColumnTypeGenerator {
  MySQLColumnTypeGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }

  @Override
  protected String getSequenceSql() {
    return "integer auto_increment";
  }

  @Override
  protected String getLongSequenceSql() {
    return "bigint auto_increment";
  }

  @Override
  protected String getTextSql(Column column) {
    return "mediumtext";
  }

  @Override
  protected String getBinarySql() {
    return "mediumblob";
  }

  @Override
  protected String getUUIDDefaultValueSql(Schema schema) {
    return "uuid()";
  }

  @Override
  protected String getBooleanSql() {
    if (booleanMode == BooleanMode.YES_NO) {
      return "enum('Yes','No')";
    }

    if (booleanMode == BooleanMode.YN) {
      return "enum('Y','N')";
    }

    return getNativeBooleanSql();
  }

  @Override
  protected String getArraySql(Column column) {
    throw new UnsupportedOperationException("MySQL does not support arrays");
  }
}

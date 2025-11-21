package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.ColumnTypeGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.Column;
import com.stano.schema.model.EnumType;
import com.stano.schema.model.EnumValue;
import com.stano.schema.model.Schema;

class MSSQLColumnTypeGenerator extends ColumnTypeGenerator {
  MSSQLColumnTypeGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }

  @Override
  protected String getSequenceSql() {
    return "integer identity(1,1)";
  }

  @Override
  protected String getLongSequenceSql() {
    return "bigint identity(1,1)";
  }

  @Override
  protected String getNativeBooleanSql() {
    return "bit";
  }

  @Override
  protected String getDateSql() {
    return "datetime";
  }

  @Override
  protected String getDateTimeSql() {
    return "datetime";
  }

  @Override
  protected String getTimeSql() {
    return "datetime";
  }

  @Override
  protected String getCharSql(Column column) {
    return "char(" + column.getLength() + ")";
  }

  @Override
  protected String getVarcharSql(Column column) {
    return "nvarchar(" + (column.getLength() == -1 ? "max" : column.getLength()) + ")";
  }

  @Override
  protected String getTextSql(Column column) {
   return "nvarchar(max)";
  }

  @Override
  protected String getBinarySql() {
    return "varbinary(max)";
  }

  @Override
  protected String getUUIDDefaultValueSql(Schema schema) {
    return "newid()";
  }

  @Override
  protected String getBooleanSql() {
    if (booleanMode == BooleanMode.YES_NO) {
      return "nvarchar(3)";
    }

    if (booleanMode == BooleanMode.YN) {
      return "nchar(1)";
    }

    return getNativeBooleanSql();
  }

  @Override
  protected String getEnumSql(Column column) {
    EnumType enumType = schema.getEnumType(column.getEnumType());

    int minLength = Integer.MAX_VALUE;
    int maxLength = 0;

    for (EnumValue enumValue : enumType.getValues()) {
      String code = enumValue.getCode();

      minLength = Math.min(minLength, code.length());
      maxLength = Math.max(maxLength, code.length());
    }

    if (minLength != maxLength) {
      return "nvarchar(" + maxLength + ")";
    }

    return "nchar(" + maxLength + ")";
  }

  @Override
  protected String getUUIDSql(Column column) {
    return "uniqueidentifier";
  }

  @Override
  protected String getArraySql(Column column) {
    throw new UnsupportedOperationException("MSSQL does not support arrays");
  }
}

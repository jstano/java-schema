package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.ColumnTypeGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Column;
import com.stano.schema.model.ColumnType;
import com.stano.schema.model.Schema;

public class PGSQLColumnTypeGenerator extends ColumnTypeGenerator {
  protected PGSQLColumnTypeGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }

  @Override
  protected String getSequenceSql() {
    return "serial";
  }

  @Override
  protected String getLongSequenceSql() {
    return "bigserial";
  }

  @Override
  protected String getByteSql() {
    return "smallint";
  }

  @Override
  protected String getNativeBooleanSql() {
    return "boolean";
  }

  @Override
  protected String getVarcharSql(Column column) {
    return "text";
  }

  @Override
  protected String getTextSql(Column column) {
    if (column != null && column.isIgnoreCase()) {
      return "citext";
    }

    return "text";
  }

  @Override
  protected String getBinarySql() {
    return "bytea";
  }

  @Override
  protected String getJsonSql() {
    return "jsonb";
  }

  @Override
  protected String getUUIDSql(Column column) {
    return "uuid";
  }

  @Override
  protected String getUUIDDefaultValueSql(Schema schema) {
    return "generate_uuid()";
  }

  protected String getArraySql(Column column) {
    ColumnType elementType = column.getElementType();

    return switch (elementType) {
      case ColumnType.VARCHAR -> getVarcharSql(column) + "[]";
      case ColumnType.CHAR -> getCharSql(column) + "[]";
      case ColumnType.TEXT -> getTextSql(column) + "[]";
      case ColumnType.DECIMAL -> getDecimalSql(column) + "[]";
      case ColumnType.BYTE -> getByteSql() + "[]";
      case ColumnType.SHORT -> getShortSql() + "[]";
      case ColumnType.INT -> getIntSql() + "[]";
      case ColumnType.LONG -> getLongSql() + "[]";
      default -> throw new IllegalArgumentException("Unsupported array type: " + elementType);
    };
  }
}

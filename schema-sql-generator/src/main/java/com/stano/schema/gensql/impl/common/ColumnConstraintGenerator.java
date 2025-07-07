package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.Column;
import com.stano.schema.model.ColumnType;
import com.stano.schema.model.EnumValue;
import com.stano.schema.model.Table;

import java.util.List;
import java.util.stream.Collectors;

public class ColumnConstraintGenerator extends BaseGenerator {
  private static final String CK_PREFIX = "ck_";

  protected ColumnConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }

  public List<String> getColumnCheckConstraints(Table table) {
    List<Column> columns = table.getColumnsWithCheckConstraints(booleanMode);

    return columns.stream().map(column -> generateConstraint(table, column)).collect(Collectors.toList());
  }

  protected String getCheckConstraintSQL(Column column) {
    if (column.getType() == ColumnType.BOOLEAN) {
      if (booleanMode == BooleanMode.YES_NO) {
        return String.format("check(%s in ('Yes','No'))", column.getName());
      }

      if (booleanMode == BooleanMode.YN) {
        return String.format("check(%s in ('Y','N'))", column.getName());
      }

      return null;
    }

    if (column.getCheckConstraint() != null) {
      return column.getCheckConstraint();
    }

    return buildCheckConstraint(column);
  }

  private String generateConstraint(Table table, Column column) {
    return String.format("   constraint %s %s",
                         getConstraintName(table.getName(), column.getName()),
                         getCheckConstraintSQL(column));
  }

  private String getConstraintName(String tableName, String columnName) {
    tableName = tableName.toLowerCase();
    columnName = columnName.toLowerCase();

    String name = tableName + "_" + columnName;
    String hashCode = Integer.toHexString(name.hashCode()).toUpperCase();

    if (tableName.length() > 9) {
      tableName = tableName.substring(0, 9);
    }

    if (columnName.length() > 9) {
      columnName = columnName.substring(0, 9);
    }

    return CK_PREFIX + tableName + "_" + columnName + "_" + hashCode;
  }

  private String buildCheckConstraint(Column column) {
    if (column.getType() == ColumnType.ENUM) {
      return buildEnumCheckConstraintSql(column);
    }

    if (column.hasMinOrMaxValue()) {
      return buildMinMaxConstraintSql(column);
    }

    return null;
  }

  private String buildEnumCheckConstraintSql(Column column) {
    List<EnumValue> enumValues = schema.getEnumType(column.getEnumType()).getValues();

    return String.format("check(%s in (%s))",
                         column.getName(),
                         enumValues.stream().map(it -> "'" + it.getCode() + "'").collect(Collectors.joining(",")));
  }

  private String buildMinMaxConstraintSql(Column column) {
    boolean hasMinValue = column.getMinValue() != null;
    boolean hasMaxValue = column.getMaxValue() != null;

    StringBuilder constraintSql = new StringBuilder("check(");

    if (hasMinValue) {
      constraintSql.append(column.getName());
      constraintSql.append(" >= ");
      constraintSql.append(column.getMinValue());
    }

    if (hasMinValue && hasMaxValue) {
      constraintSql.append(" and ");
    }

    if (hasMaxValue) {
      constraintSql.append(column.getName());
      constraintSql.append(" <= ");
      constraintSql.append(column.getMaxValue());
    }

    constraintSql.append(")");

    return constraintSql.toString();
  }
}

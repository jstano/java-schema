package com.stano.schema.gensql.impl.sqlserver;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.ColumnGenerator;
import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.KeyGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;
import com.stano.schema.gensql.impl.common.TableGenerator;
import com.stano.schema.model.Table;
import com.stano.schema.model.TableOption;

class SQLServerTableGenerator extends TableGenerator {
  private final ColumnGenerator columnGenerator;
  private final KeyGenerator keyGenerator;
  private final ColumnConstraintGenerator columnConstraintGenerator;
  private final TableConstraintGenerator tableConstraintGenerator;
  private final IndexGenerator indexGenerator;

  SQLServerTableGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);

    this.columnGenerator = new SQLServerColumnGenerator(sqlGenerator);
    this.keyGenerator = new SQLServerKeyGenerator(sqlGenerator);
    this.columnConstraintGenerator = new SQLServerColumnConstraintGenerator(sqlGenerator);
    this.tableConstraintGenerator = new SQLServerTableConstraintGenerator(sqlGenerator);
    this.indexGenerator = new SQLServerIndexGenerator(sqlGenerator);
  }

  @Override
  protected void outputTableHeader(Table table) {
    String tableName = getFullyQualifiedTableName(table);

    sqlWriter.println("/* " + table.getName() + " */");
    sqlWriter.println(
        "if exists (select name from dbo.sysobjects where name = '"
            + table.getName()
            + "' and type = 'U')");
    sqlWriter.println("drop table " + tableName + statementSeparator);
    sqlWriter.println();
    sqlWriter.println("create table " + tableName);
    sqlWriter.println("(");
  }

  @Override
  protected void outputTableFooter(Table table) {
    if (table.getOptions().contains(TableOption.COMPRESS)) {
      sqlWriter.println(") with (data_compression = page)" + statementSeparator);
    } else {
      sqlWriter.println(")" + statementSeparator);
    }

    if (table.getLockEscalation() != null) {
      sqlWriter.println();
      sqlWriter.println(
          "alter table "
              + table.getName()
              + " set (lock_escalation = "
              + table.getLockEscalation().name().toLowerCase()
              + ")"
              + statementSeparator);
    }

    sqlWriter.println();
  }

  @Override
  protected ColumnGenerator getColumnGenerator() {
    return columnGenerator;
  }

  @Override
  protected KeyGenerator getKeyGenerator() {
    return keyGenerator;
  }

  @Override
  protected ColumnConstraintGenerator getColumnConstraintGenerator() {
    return columnConstraintGenerator;
  }

  @Override
  protected TableConstraintGenerator getTableConstraintGenerator() {
    return tableConstraintGenerator;
  }

  @Override
  protected IndexGenerator getIndexGenerator() {
    return indexGenerator;
  }

  @Override
  protected String getFullyQualifiedTableName(Table table) {
    String schemaName =
        table.getSchemaName().equalsIgnoreCase("public") ? "dbo" : table.getSchemaName();
    return schemaName + "." + table.getName();
  }
}

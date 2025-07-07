package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.ColumnGenerator;
import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.KeyGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;
import com.stano.schema.gensql.impl.common.TableGenerator;
import com.stano.schema.model.Table;

class PGSQLTableGenerator extends TableGenerator {
  private final ColumnGenerator columnGenerator;
  private final KeyGenerator keyGenerator;
  private final ColumnConstraintGenerator columnConstraintGenerator;
  private final TableConstraintGenerator tableConstraintGenerator;
  private final IndexGenerator indexGenerator;

  PGSQLTableGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);

    this.columnGenerator = new PGSQLColumnGenerator(sqlGenerator);
    this.keyGenerator = new PGSQLKeyGenerator(sqlGenerator);
    this.columnConstraintGenerator = new PGSQLColumnConstraintGenerator(sqlGenerator);
    this.tableConstraintGenerator = new PGSQLTableConstraintGenerator(sqlGenerator);
    this.indexGenerator = new PGSQLIndexGenerator(sqlGenerator);
  }

  @Override
  protected void outputTableHeader(Table table) {
    String tableName = getFullyQualifiedTableName(table);

    sqlWriter.println(String.format("/* %s */", tableName));
    sqlWriter.println("drop table if exists " + tableName + " cascade" + statementSeparator);
    sqlWriter.println();
    sqlWriter.println("create table " + tableName);
    sqlWriter.println("(");
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
}

package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.ColumnGenerator;
import com.stano.schema.gensql.impl.common.ColumnTypeGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Column;
import com.stano.schema.model.Table;

class PGSQLColumnGenerator extends ColumnGenerator {
  private final ColumnTypeGenerator columnTypeGenerator;

  PGSQLColumnGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);

    this.columnTypeGenerator = new PGSQLColumnTypeGenerator(sqlGenerator);
  }

  @Override
  protected ColumnTypeGenerator getColumnTypeGenerator() {
    return columnTypeGenerator;
  }

  @Override
  protected String createDefaultConstraint(Table table, Column column, String defaultValue) {
    return String.format("default %s", defaultValue);
  }
}

package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Column;
import com.stano.schema.model.ColumnType;

class PGSQLColumnConstraintGenerator extends ColumnConstraintGenerator {
  PGSQLColumnConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }

  protected String getCheckConstraintSQL(Column column) {
    if (column.getType() == ColumnType.VARCHAR) {
      return String.format("check(length(%s) <= %d)", column.getName(), column.getLength());
    }

    return super.getCheckConstraintSQL(column);
  }
}

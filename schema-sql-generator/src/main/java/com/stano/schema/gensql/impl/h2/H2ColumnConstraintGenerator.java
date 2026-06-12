package com.stano.schema.gensql.impl.h2;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Column;
import com.stano.schema.model.ColumnType;

class H2ColumnConstraintGenerator extends ColumnConstraintGenerator {

  H2ColumnConstraintGenerator(SQLGenerator sqlGenerator) {

    super(sqlGenerator);
  }

  @Override
  protected String getCheckConstraintSQL(Column column) {
    if (column.getType() == ColumnType.ENUM) {
      return null;
    }
    return super.getCheckConstraintSQL(column);
  }
}

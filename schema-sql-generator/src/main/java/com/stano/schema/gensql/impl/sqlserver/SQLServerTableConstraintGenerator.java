package com.stano.schema.gensql.impl.sqlserver;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;

public class SQLServerTableConstraintGenerator extends TableConstraintGenerator {
  protected SQLServerTableConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

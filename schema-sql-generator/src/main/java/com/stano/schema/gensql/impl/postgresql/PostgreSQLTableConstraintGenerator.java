package com.stano.schema.gensql.impl.postgresql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;

public class PostgreSQLTableConstraintGenerator extends TableConstraintGenerator {
  protected PostgreSQLTableConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

package com.stano.schema.gensql.impl.postgresql;

import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

public class PostgreSQLIndexGenerator extends IndexGenerator {
  protected PostgreSQLIndexGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

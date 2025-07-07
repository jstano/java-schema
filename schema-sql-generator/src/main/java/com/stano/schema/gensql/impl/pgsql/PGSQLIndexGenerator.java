package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

public class PGSQLIndexGenerator extends IndexGenerator {
  protected PGSQLIndexGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

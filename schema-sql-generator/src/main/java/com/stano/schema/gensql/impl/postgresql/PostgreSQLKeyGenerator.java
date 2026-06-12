package com.stano.schema.gensql.impl.postgresql;

import com.stano.schema.gensql.impl.common.KeyGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

public class PostgreSQLKeyGenerator extends KeyGenerator {

  PostgreSQLKeyGenerator(SQLGenerator sqlGenerator) {

    super(sqlGenerator);
  }
}

package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.KeyGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

public class PGSQLKeyGenerator extends KeyGenerator {

   PGSQLKeyGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

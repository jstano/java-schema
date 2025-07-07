package com.stano.schema.gensql.impl.mysql;

import com.stano.schema.gensql.impl.common.KeyGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class MySQLKeyGenerator extends KeyGenerator {

   MySQLKeyGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

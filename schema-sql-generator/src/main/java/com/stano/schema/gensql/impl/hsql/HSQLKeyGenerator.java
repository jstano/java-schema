package com.stano.schema.gensql.impl.hsql;

import com.stano.schema.gensql.impl.common.KeyGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class HSQLKeyGenerator extends KeyGenerator {

   HSQLKeyGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected boolean disallowDuplicateUniqueKeyConstraints() {

      return true;
   }
}

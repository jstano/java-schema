package com.stano.schema.gensql.impl.h2;

import com.stano.schema.gensql.impl.common.KeyGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class H2KeyGenerator extends KeyGenerator {

   H2KeyGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

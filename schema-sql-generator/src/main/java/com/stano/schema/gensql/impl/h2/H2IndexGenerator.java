package com.stano.schema.gensql.impl.h2;

import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class H2IndexGenerator extends IndexGenerator {

   H2IndexGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

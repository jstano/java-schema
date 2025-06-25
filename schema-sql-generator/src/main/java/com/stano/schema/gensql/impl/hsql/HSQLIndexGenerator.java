package com.stano.schema.gensql.impl.hsql;

import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class HSQLIndexGenerator extends IndexGenerator {

   HSQLIndexGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

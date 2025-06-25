package com.stano.schema.gensql.impl.hsql;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class HSQLColumnConstraintGenerator extends ColumnConstraintGenerator {

   HSQLColumnConstraintGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

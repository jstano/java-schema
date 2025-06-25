package com.stano.schema.gensql.impl.h2;

import com.stano.schema.gensql.impl.common.ColumnConstraintGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;

class H2ColumnConstraintGenerator extends ColumnConstraintGenerator {

   H2ColumnConstraintGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }
}

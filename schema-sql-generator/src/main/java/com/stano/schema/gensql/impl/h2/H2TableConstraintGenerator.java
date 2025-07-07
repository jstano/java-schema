package com.stano.schema.gensql.impl.h2;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;

public class H2TableConstraintGenerator extends TableConstraintGenerator {
  protected H2TableConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

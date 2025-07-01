package com.stano.schema.gensql.impl.hsql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;

public class HSQLTableConstraintGenerator extends TableConstraintGenerator {
  protected HSQLTableConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

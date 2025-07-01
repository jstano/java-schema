package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;

public class PGSQLTableConstraintGenerator extends TableConstraintGenerator {
  protected PGSQLTableConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;

public class MSSQLTableConstraintGenerator extends TableConstraintGenerator {
  protected MSSQLTableConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

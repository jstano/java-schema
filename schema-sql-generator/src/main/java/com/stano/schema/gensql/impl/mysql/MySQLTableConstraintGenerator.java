package com.stano.schema.gensql.impl.mysql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TableConstraintGenerator;

public class MySQLTableConstraintGenerator extends TableConstraintGenerator {
  protected MySQLTableConstraintGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }
}

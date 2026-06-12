package com.stano.schema.gensql.impl.sqlserver;

import com.stano.schema.gensql.impl.common.RelationGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Table;

class SQLServerRelationGenerator extends RelationGenerator {

  SQLServerRelationGenerator(SQLGenerator sqlGenerator) {

    super(sqlGenerator);
  }

  @Override
  protected String getFullyQualifiedTableName(Table table) {

    String schemaName =
        table.getSchemaName().equalsIgnoreCase("public") ? "dbo" : table.getSchemaName();
    return schemaName + "." + table.getName();
  }
}

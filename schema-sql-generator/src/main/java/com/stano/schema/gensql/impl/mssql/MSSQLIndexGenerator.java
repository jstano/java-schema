package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Key;
import com.stano.schema.model.Table;

class MSSQLIndexGenerator extends IndexGenerator {

   MSSQLIndexGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected String getFullyQualifiedTableName(Table table) {

      String schemaName = table.getSchemaName().equalsIgnoreCase("public") ? "dbo" : table.getSchemaName();
      return schemaName + "." + table.getName();
   }

   @Override
   protected String getIndexOptions(Key key) {

      if (key.getInclude() != null && !key.getInclude().isEmpty()) {
         return String.format("include (%s)", key.getInclude());
      }

      if (key.isCompress()) {
         return "with (data_compression = page)";
      }

      return null;
   }
}

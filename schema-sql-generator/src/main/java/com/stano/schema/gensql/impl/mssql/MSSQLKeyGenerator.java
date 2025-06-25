package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.KeyGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Key;
import com.stano.schema.model.KeyType;

class MSSQLKeyGenerator extends KeyGenerator {

   MSSQLKeyGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   protected String getKeyClusterSql(Key key) {

      if (!key.isCluster() && key.getType() == KeyType.PRIMARY) {
         return "nonclustered";
      }

      if (key.isCluster() && key.getType() == KeyType.UNIQUE) {
         return "clustered";
      }

      return null;
   }

   protected String getKeyCompressSql(Key key) {

      if (key.isCompress()) {
         sqlWriter.print("with (data_compression = page)");
      }

      return null;
   }
}

package com.stano.schema.gensql.impl.common;

import com.stano.schema.gensql.impl.h2.H2Generator;
import com.stano.schema.gensql.impl.mssql.MSSQLGenerator;
import com.stano.schema.gensql.impl.mysql.MySQLGenerator;
import com.stano.schema.gensql.impl.pgsql.PGSQLGenerator;
import com.stano.schema.model.DatabaseType;

public class SQLGeneratorFactory {

  public SQLGenerator createSQLGenerator(SQLGeneratorOptions sqlGeneratorOptions) {

    DatabaseType databaseType = sqlGeneratorOptions.getDatabaseType();

    if (databaseType != null) {
      switch (databaseType) {
        case H2:
          return new H2Generator(sqlGeneratorOptions);
        case MYSQL:
          return new MySQLGenerator(sqlGeneratorOptions);
        case POSTGRES:
          return new PGSQLGenerator(sqlGeneratorOptions);
        case SQL_SERVER:
          return new MSSQLGenerator(sqlGeneratorOptions);
      }
    }

    throw new IllegalArgumentException("Unable to locate a generator");
  }
}

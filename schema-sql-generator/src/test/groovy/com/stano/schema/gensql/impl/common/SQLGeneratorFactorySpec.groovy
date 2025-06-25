package com.stano.schema.gensql.impl.common

import com.stano.schema.gensql.impl.h2.H2Generator
import com.stano.schema.gensql.impl.hsql.HSQLGenerator
import com.stano.schema.gensql.impl.mssql.MSSQLGenerator
import com.stano.schema.gensql.impl.mysql.MySQLGenerator
import com.stano.schema.gensql.impl.pgsql.PGSQLGenerator
import com.stano.schema.model.BooleanMode
import com.stano.schema.model.DatabaseType
import com.stano.schema.model.ForeignKeyMode
import com.stano.schema.model.Schema
import spock.lang.Specification

class SQLGeneratorFactorySpec extends Specification {
  def schema = Mock(Schema)
  def writer = Mock(PrintWriter)
  def sqlGeneratorFactory = new SQLGeneratorFactory()

  def "should be able to create a generator for all database types"() {
    def sqlGeneratorOptions = new SQLGeneratorOptions(schema, writer, databaseType, ForeignKeyMode.RELATIONS, BooleanMode.NATIVE, OutputMode.ALL)

    expect:
    sqlGeneratorFactory.createSQLGenerator(sqlGeneratorOptions).getClass().isAssignableFrom(generatorClass)

    where:
    databaseType       | generatorClass
    DatabaseType.H2    | H2Generator
    DatabaseType.HSQL  | HSQLGenerator
    DatabaseType.MSSQL | MSSQLGenerator
    DatabaseType.MYSQL | MySQLGenerator
    DatabaseType.PGSQL | PGSQLGenerator
  }

  def "should get an IllegalStateException if an exception is thrown"() {
    def sqlGeneratorOptions = Mock(SQLGeneratorOptions)

    when:
    sqlGeneratorFactory.createSQLGenerator(sqlGeneratorOptions)

    then:
    thrown IllegalArgumentException
  }
}

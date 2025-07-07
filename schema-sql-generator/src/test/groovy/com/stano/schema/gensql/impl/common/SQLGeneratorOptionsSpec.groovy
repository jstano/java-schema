package com.stano.schema.gensql.impl.common

import com.stano.schema.model.BooleanMode
import com.stano.schema.model.DatabaseType
import com.stano.schema.model.ForeignKeyMode
import com.stano.schema.model.Schema
import spock.lang.Specification

class SQLGeneratorOptionsSpec extends Specification {
  def "should be able to create an object using the shorter constructor and get the values out"() {
    def schema = Mock(Schema)
    def sqlWriter = Mock(PrintWriter)
    def options = new SQLGeneratorOptions(schema, sqlWriter, DatabaseType.PGSQL, ForeignKeyMode.RELATIONS, BooleanMode.YES_NO, OutputMode.ALL)

    expect:
    options.schema == schema
    options.sqlWriter == sqlWriter
    options.databaseType == DatabaseType.PGSQL
    options.foreignKeyMode == ForeignKeyMode.RELATIONS
    options.booleanMode == BooleanMode.YES_NO
    options.outputMode == OutputMode.ALL
    options.statementSeparator == ';'
  }

  def "should be able to create an object using the full constructor and get the values out"() {
    def schema = Mock(Schema)
    def sqlWriter = Mock(PrintWriter)
    def options = new SQLGeneratorOptions(schema, sqlWriter, DatabaseType.PGSQL, ForeignKeyMode.RELATIONS, BooleanMode.YES_NO, OutputMode.ALL, 'THE_SEPARATOR')

    expect:
    options.schema == schema
    options.sqlWriter == sqlWriter
    options.databaseType == DatabaseType.PGSQL
    options.foreignKeyMode == ForeignKeyMode.RELATIONS
    options.booleanMode == BooleanMode.YES_NO
    options.outputMode == OutputMode.ALL
    options.statementSeparator == 'THE_SEPARATOR'
  }

  def "if the ForeignKeyMode is set to TRIGGERS and the database doesn't support triggers then the getForeignKeyMode method should return RELATIONS"() {
    def schema = Mock(Schema)
    def sqlWriter = Mock(PrintWriter)
    def options = new SQLGeneratorOptions(schema, sqlWriter, DatabaseType.HSQL, ForeignKeyMode.TRIGGERS, BooleanMode.YES_NO, OutputMode.ALL)

    expect:
    options.foreignKeyMode == ForeignKeyMode.RELATIONS
  }

  def "if the ForeignKeyMode is set to TRIGGERS and the database supports triggers then the getForeignKeyMode method should return TRIGGERS"() {
    def schema = Mock(Schema)
    def sqlWriter = Mock(PrintWriter)
    def options = new SQLGeneratorOptions(schema, sqlWriter, DatabaseType.MSSQL, ForeignKeyMode.TRIGGERS, BooleanMode.YES_NO, OutputMode.ALL)

    expect:
    options.foreignKeyMode == ForeignKeyMode.TRIGGERS
  }
}

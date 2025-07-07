package com.stano.schema.gensql

import com.stano.schema.gensql.impl.common.OutputMode
import com.stano.schema.gensql.impl.common.SQLGenerator
import com.stano.schema.gensql.impl.common.SQLGeneratorFactory
import com.stano.schema.model.BooleanMode
import com.stano.schema.model.DatabaseType
import com.stano.schema.model.ForeignKeyMode
import com.stano.schema.model.Schema
import spock.lang.Specification

class GenSQLSpec extends Specification {
  def "should be able to generate the sql from the schema using the full generateSQL method"() {
    def sqlGenerated = false
    def writerClosed = false
    def schema = Mock(Schema)
    def writer = Mock(PrintWriter) {
      close() >> {
        writerClosed = true
      }
    }
    def sqlGenerator = Mock(SQLGenerator) {
      generate() >> {
        sqlGenerated = true
      }
    }
    def sqlGeneratorFactory = Mock(SQLGeneratorFactory) {
      createSQLGenerator(_) >> { args ->
        def options = args[0]

        if (options.schema == schema && options.sqlWriter == writer && options.databaseType == DatabaseType.PGSQL && options.foreignKeyMode == ForeignKeyMode.RELATIONS && options.booleanMode == BooleanMode.NATIVE && options.outputMode == OutputMode.ALL && options.statementSeparator == ';') {
          return sqlGenerator
        }

        return null
      }
    }
    def genSql = new GenSQL()
    genSql.sqlGeneratorFactory = sqlGeneratorFactory

    genSql.generateSQL(DatabaseType.PGSQL, schema, writer, ForeignKeyMode.RELATIONS, BooleanMode.NATIVE, OutputMode.ALL, ";")

    expect:
    sqlGenerated
    writerClosed
  }

  def "should be able to generate the sql from the schema using the generateSQL method without the OutputMode"() {
    def sqlGenerated = false
    def writerClosed = false
    def schema = Mock(Schema)
    def writer = Mock(PrintWriter) {
      close() >> {
        writerClosed = true
      }
    }
    def sqlGenerator = Mock(SQLGenerator) {
      generate() >> {
        sqlGenerated = true
      }
    }
    def sqlGeneratorFactory = Mock(SQLGeneratorFactory) {
      createSQLGenerator(_) >> { args ->
        def options = args[0]

        if (options.schema == schema && options.sqlWriter == writer && options.databaseType == DatabaseType.PGSQL && options.foreignKeyMode == ForeignKeyMode.RELATIONS && options.booleanMode == BooleanMode.NATIVE && options.outputMode == OutputMode.ALL && options.statementSeparator == ';') {
          return sqlGenerator
        }

        return null
      }
    }
    def genSql = new GenSQL()
    genSql.sqlGeneratorFactory = sqlGeneratorFactory

    genSql.generateSQL(DatabaseType.PGSQL, schema, writer, ForeignKeyMode.RELATIONS, BooleanMode.NATIVE, ";")

    expect:
    sqlGenerated
    writerClosed
  }

  def "should be able to generate the sql from the schema using the middle generateSQL method"() {
    def sqlGenerated = false
    def writerClosed = false
    def schema = Mock(Schema)
    def writer = Mock(PrintWriter) {
      close() >> {
        writerClosed = true
      }
    }
    def sqlGenerator = Mock(SQLGenerator) {
      generate() >> {
        sqlGenerated = true
      }
    }
    def sqlGeneratorFactory = Mock(SQLGeneratorFactory) {
      createSQLGenerator(_) >> { args ->
        def options = args[0]

        if (options.schema == schema && options.sqlWriter == writer && options.databaseType == DatabaseType.PGSQL && options.foreignKeyMode == ForeignKeyMode.RELATIONS && options.booleanMode == BooleanMode.NATIVE && options.outputMode == OutputMode.ALL && options.statementSeparator == ';') {
          return sqlGenerator
        }

        return null
      }
    }
    def genSql = new GenSQL()
    genSql.sqlGeneratorFactory = sqlGeneratorFactory

    genSql.generateSQL(DatabaseType.PGSQL, schema, writer, ForeignKeyMode.RELATIONS, BooleanMode.NATIVE, OutputMode.ALL)

    expect:
    sqlGenerated
    writerClosed
  }
}

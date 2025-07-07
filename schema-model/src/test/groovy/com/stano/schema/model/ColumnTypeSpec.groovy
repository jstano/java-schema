package com.stano.schema.model

import spock.lang.Specification

class ColumnTypeSpec extends Specification {
  def "getColumnType should return the correct ColumnType for all supported types"() {
    expect:
    ColumnType.values().size() == 22
    ColumnType.getColumnType('sequence') == ColumnType.SEQUENCE
    ColumnType.getColumnType('longsequence') == ColumnType.LONGSEQUENCE
    ColumnType.getColumnType('byte') == ColumnType.BYTE
    ColumnType.getColumnType('short') == ColumnType.SHORT
    ColumnType.getColumnType('int') == ColumnType.INT
    ColumnType.getColumnType('long') == ColumnType.LONG
    ColumnType.getColumnType('float') == ColumnType.FLOAT
    ColumnType.getColumnType('double') == ColumnType.DOUBLE
    ColumnType.getColumnType('decimal') == ColumnType.DECIMAL
    ColumnType.getColumnType('boolean') == ColumnType.BOOLEAN
    ColumnType.getColumnType('date') == ColumnType.DATE
    ColumnType.getColumnType('datetime') == ColumnType.DATETIME
    ColumnType.getColumnType('time') == ColumnType.TIME
    ColumnType.getColumnType('timestamp') == ColumnType.TIMESTAMP
    ColumnType.getColumnType('char') == ColumnType.CHAR
    ColumnType.getColumnType('varchar') == ColumnType.VARCHAR
    ColumnType.getColumnType('enum') == ColumnType.ENUM
    ColumnType.getColumnType('text') == ColumnType.TEXT
    ColumnType.getColumnType('binary') == ColumnType.BINARY
    ColumnType.getColumnType('uuid') == ColumnType.UUID
    ColumnType.getColumnType('json') == ColumnType.JSON
  }

  def "getColumnType should throw an IllegalArgumentException if the type name is not valid"() {
    when:
    ColumnType.getColumnType('invalid')

    then:
    thrown IllegalArgumentException
  }

  def "isText should return true for all text types"() {
    expect:
    !ColumnType.SEQUENCE.text
    !ColumnType.LONGSEQUENCE.text
    !ColumnType.BYTE.text
    !ColumnType.SHORT.text
    !ColumnType.INT.text
    !ColumnType.LONG.text
    !ColumnType.FLOAT.text
    !ColumnType.DOUBLE.text
    !ColumnType.DECIMAL.text
    !ColumnType.BOOLEAN.text
    !ColumnType.DATE.text
    !ColumnType.DATETIME.text
    !ColumnType.TIME.text
    !ColumnType.TIMESTAMP.text
    ColumnType.CHAR.text
    ColumnType.VARCHAR.text
    ColumnType.ENUM.text
    ColumnType.TEXT.text
    !ColumnType.BINARY.text
    ColumnType.UUID.text
    ColumnType.JSON.text
  }

  def "isNumeric should return true for all numeric types"() {
    expect:
    ColumnType.SEQUENCE.numeric
    ColumnType.LONGSEQUENCE.numeric
    ColumnType.BYTE.numeric
    ColumnType.SHORT.numeric
    ColumnType.INT.numeric
    ColumnType.LONG.numeric
    ColumnType.FLOAT.numeric
    ColumnType.DOUBLE.numeric
    ColumnType.DECIMAL.numeric
    !ColumnType.BOOLEAN.numeric
    !ColumnType.DATE.numeric
    !ColumnType.DATETIME.numeric
    !ColumnType.TIME.numeric
    !ColumnType.TIMESTAMP.numeric
    !ColumnType.CHAR.numeric
    !ColumnType.VARCHAR.numeric
    !ColumnType.ENUM.numeric
    !ColumnType.TEXT.numeric
    !ColumnType.BINARY.numeric
    !ColumnType.UUID.numeric
    !ColumnType.JSON.numeric
  }
}

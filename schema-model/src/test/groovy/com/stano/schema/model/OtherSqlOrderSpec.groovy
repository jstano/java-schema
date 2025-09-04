package com.stano.schema.model

import spock.lang.Specification

class OtherSqlOrderSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    OtherSqlOrder.values()*.name() == [
      'BOTTOM', 'TOP'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    OtherSqlOrder.valueOf(name) == constant
    constant.name() == name

    where:
    name       | constant
    'BOTTOM'   | OtherSqlOrder.BOTTOM
    'TOP'      | OtherSqlOrder.TOP
  }

  def "all enum values should be unique"() {
    given:
    def values = OtherSqlOrder.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "integration: OtherSql should retain the provided OtherSqlOrder"() {
    when:
    def top = new OtherSql(DatabaseType.PGSQL, OtherSqlOrder.TOP, "A;")
    def bottom = new OtherSql(DatabaseType.MYSQL, OtherSqlOrder.BOTTOM, "B;")

    then:
    top.order == OtherSqlOrder.TOP
    bottom.order == OtherSqlOrder.BOTTOM
  }
}

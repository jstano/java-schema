package com.stano.schema.model

import spock.lang.Specification

class ConstraintSpec extends Specification {
  def "constructor should set fields and getters should return them for various database types"() {
    when:
    def constraint = new Constraint(name, sql, dbType)

    then:
    constraint.name == name
    constraint.sql == sql
    constraint.databaseType == dbType

    where:
    name            | sql                      | dbType
    "ck_positive"  | "amount > 0"            | DatabaseType.SQL_SERVER
    "ck_not_null"  | "col is not null"       | DatabaseType.POSTGRES
    "ck_unique"    | "unique (a,b)"          | DatabaseType.MYSQL
  }

  def "supports null sql value and still returns correct fields"() {
    when:
    def constraint = new Constraint("ck_empty", null, DatabaseType.H2)

    then:
    constraint.name == "ck_empty"
    constraint.sql == null
    constraint.databaseType == DatabaseType.H2
  }
}

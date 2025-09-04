package com.stano.schema.model

import spock.lang.Specification

class TableOptionSpec extends Specification {
  def "enum should contain exactly the expected values in order"() {
    expect:
    TableOption.values()*.name() == [
      'DATA', 'NO_EXPORT', 'COMPRESS'
    ]
  }

  def "valueOf should return the correct enum constant for each name"() {
    expect:
    TableOption.valueOf(name) == constant
    constant.name() == name

    where:
    name         | constant
    'DATA'       | TableOption.DATA
    'NO_EXPORT'  | TableOption.NO_EXPORT
    'COMPRESS'   | TableOption.COMPRESS
  }

  def "all enum values should be unique"() {
    given:
    def values = TableOption.values()

    expect:
    new HashSet(values as List).size() == values.length
  }

  def "integration with Table: getOptions is live list and hasOption checks identity"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def table = new Table(schema, "public", "orders", null, LockEscalation.AUTO, false)

    expect: "initially, no options and hasOption returns false for all"
    table.options.isEmpty()
    !table.hasOption(TableOption.DATA)
    !table.hasOption(TableOption.NO_EXPORT)
    !table.hasOption(TableOption.COMPRESS)

    when: "add options via the live list"
    table.options.add(TableOption.DATA)
    table.options.add(TableOption.COMPRESS)

    then: "the table reflects additions and hasOption matches presence"
    table.options == [TableOption.DATA, TableOption.COMPRESS]
    table.hasOption(TableOption.DATA)
    !table.hasOption(TableOption.NO_EXPORT)
    table.hasOption(TableOption.COMPRESS)

    when: "remove an option and re-check"
    table.options.remove(TableOption.DATA)

    then:
    table.options == [TableOption.COMPRESS]
    !table.hasOption(TableOption.DATA)
    table.hasOption(TableOption.COMPRESS)
  }
}

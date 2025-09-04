package com.stano.schema.model

import spock.lang.Specification

class EnumTypeSpec extends Specification {
  def "constructor should set name and values should be empty initially"() {
    when:
    def et = new EnumType("Color")

    then:
    et.name == "Color"
    et.values.isEmpty()
  }

  def "addValue should append EnumValue instances preserving order"() {
    given:
    def et = new EnumType("Color")

    when:
    et.addValue(new EnumValue("RED", "R"))
    et.addValue(new EnumValue("GREEN", "G"))
    et.addValue(new EnumValue("BLUE", "B"))

    then:
    et.values*.name == ["RED", "GREEN", "BLUE"]
    et.values*.code == ["R", "G", "B"]
  }

  def "EnumValue.getCode should default to name when code is null"() {
    when:
    def v1 = new EnumValue("ACTIVE", null)
    def v2 = new EnumValue("INACTIVE", "I")

    then:
    v1.name == "ACTIVE"
    v1.code == "ACTIVE"
    v2.name == "INACTIVE"
    v2.code == "I"
  }

  def "getValues returns a live list reflecting mutations (current contract)"() {
    given:
    def et = new EnumType("Status")
    et.addValue(new EnumValue("OPEN", null))

    when: "mutate the list returned by getter"
    def listRef = et.values
    listRef.add(new EnumValue("CLOSED", "C"))

    then: "EnumType reflects the change since it exposes the live list"
    et.values*.name == ["OPEN", "CLOSED"]
    et.values*.code == ["OPEN", "C"]
  }

  def "integration: build enum type with mixed null and non-null codes"() {
    when:
    def et = new EnumType("Priority")
    et.addValue(new EnumValue("HIGH", "H"))
    et.addValue(new EnumValue("MEDIUM", null))
    et.addValue(new EnumValue("LOW", "L"))

    then:
    et.name == "Priority"
    et.values*.name == ["HIGH", "MEDIUM", "LOW"]
    et.values*.code == ["H", "MEDIUM", "L"]
  }
}

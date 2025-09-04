package com.stano.schema.model

import spock.lang.Specification

class VersionSpec extends Specification {
  def "constructors should set fields and getters should return them"() {
    when: "2-arg ctor defaults patch=0 and preRelease=false"
    def v2 = new Version(1, 2)

    then:
    v2.majorVersion == 1
    v2.minorVersion == 2
    v2.patchVersion == 0
    !v2.preReleaseSuffix

    when: "3-arg ctor sets patch and preRelease=false"
    def v3 = new Version(1, 2, 3)

    then:
    v3.majorVersion == 1
    v3.minorVersion == 2
    v3.patchVersion == 3
    !v3.preReleaseSuffix

    when: "4-arg ctor sets patch and preRelease flag"
    def v4 = new Version(2, 0, 0, true)

    then:
    v4.majorVersion == 2
    v4.minorVersion == 0
    v4.patchVersion == 0
    v4.preReleaseSuffix
  }

  def "string constructor should parse versions with and without patch and SNAPSHOT"() {
    when:
    def a = new Version("01.02")
    def b = new Version("01.02.03")
    def c = new Version("10.20-SNAPSHOT")
    def d = new Version("03.04.00-SNAPSHOT")

    then:
    a.majorVersion == 1
    a.minorVersion == 2
    a.patchVersion == 0
    !a.preReleaseSuffix

    and:
    b.majorVersion == 1
    b.minorVersion == 2
    b.patchVersion == 3
    !b.preReleaseSuffix

    and:
    c.majorVersion == 10
    c.minorVersion == 20
    c.patchVersion == 0
    c.preReleaseSuffix

    and:
    d.majorVersion == 3
    d.minorVersion == 4
    d.patchVersion == 0
    d.preReleaseSuffix
  }

  def "toString should zero-pad and include -SNAPSHOT when preReleaseSuffix is true"() {
    expect:
    new Version(1, 2).toString() == "01.02"
    new Version(1, 2, 0).toString() == "01.02"
    new Version(1, 2, 3).toString() == "01.02.03"
    new Version(1, 2, 3, true).toString() == "01.02.03-SNAPSHOT"
    new Version(1, 2, 0, true).toString() == "01.02-SNAPSHOT"

    and: "string-parsed toString round-trips formatting (normalized)"
    new Version("01.02").toString() == "01.02"
    new Version("1.2").toString() == "01.02"
    new Version("1.2.3").toString() == "01.02.03"
    new Version("1.2-SNAPSHOT").toString() == "01.02-SNAPSHOT"
  }

  def "equals and hashCode should be consistent and depend on all fields"() {
    given:
    def v1 = new Version(1, 2, 0, false)
    def v2 = new Version("01.02")
    def v3 = new Version(1, 2, 0, true) // differs in preRelease

    expect:
    v1 == v2
    v1.hashCode() == v2.hashCode()
    v1 != v3
  }

  def "compareTo should order by major, minor, patch, then preRelease (preRelease considered greater)"() {
    expect:
    new Version(1, 0).compareTo(new Version(2, 0)) < 0
    new Version(2, 0).compareTo(new Version(1, 9, 9)) > 0

    and: "minor comparison"
    new Version(1, 2).compareTo(new Version(1, 3)) < 0
    new Version(1, 4).compareTo(new Version(1, 3, 10)) > 0

    and: "patch comparison"
    new Version(1, 2, 3).compareTo(new Version(1, 2, 4)) < 0
    new Version(1, 2, 5).compareTo(new Version(1, 2, 4)) > 0

    and: "preRelease vs non: preRelease is greater per implementation"
    new Version(1, 2, 0, true).compareTo(new Version(1, 2, 0, false)) > 0
    new Version(1, 2, 3, false).compareTo(new Version(1, 2, 3, true)) < 0

    and: "equal when all fields equal"
    new Version(1, 2, 3, true).compareTo(new Version(1, 2, 3, true)) == 0
  }

  def "Schema should store and return Version via getter/setter"() {
    given:
    def schema = new Schema(new URL("https://example.com/schema.json"))
    def ver = new Version(1, 2, 3, true)

    when:
    schema.setVersion(ver)

    then:
    schema.getVersion().is(ver)
  }

  def "equals should handle null, type, and differences across fields; hashCode and contract properties"() {
    given:
    def a = new Version(1, 2, 0, false)
    def b = new Version("1.2")
    def c = new Version(1, 2, 1, false) // patch differs
    def d = new Version(1, 3, 0, false) // minor differs
    def e = new Version(2, 2, 0, false) // major differs
    def f = new Version(1, 2, 0, true)  // preRelease differs

    expect: "basic equality and hashCode equality for equal instances"
    a == b
    a.hashCode() == b.hashCode()

    and: "inequality for each differing field"
    a != c
    a != d
    a != e
    a != f

    and: "equals handles null and different type"
    !a.equals(null)
    !a.equals("1.2")

    and: "symmetry"
    (a == b) && (b == a)

    and: "consistency"
    a == b
    a == b // second time

    and: "transitivity with three equals instances"
    def b2 = new Version(1, 2, 0, false)
    (a == b) && (b == b2) && (a == b2)

    and: "hashCode consistency and equal objects share hashCode"
    a.hashCode() == a.hashCode()
    b.hashCode() == b2.hashCode()
  }

  def "compareTo contract: reflexivity, anti-symmetry, transitivity, and consistency with equals"() {
    given:
    def v1 = new Version(1, 2)
    def v1b = new Version("1.2")
    def v2 = new Version(1, 3)
    def v3 = new Version(2, 0)

    expect: "reflexivity"
    v1.compareTo(v1) == 0

    and: "consistency with equals (equal -> 0)"
    v1 == v1b
    v1.compareTo(v1b) == 0

    and: "anti-symmetry"
    Integer.signum(v1.compareTo(v2)) == -Integer.signum(v2.compareTo(v1))

    and: "transitivity (v1 < v2 < v3 => v1 < v3)"
    v1.compareTo(v2) < 0
    v2.compareTo(v3) < 0
    v1.compareTo(v3) < 0

    and: "zero-patch equivalence vs explicit zero"
    new Version(1, 2).compareTo(new Version(1, 2, 0)) == 0

    and: "preRelease sign consistency in both directions (preRelease greater)"
    Integer.signum(new Version(1,2,0,true).compareTo(new Version(1,2,0,false))) == 1
    Integer.signum(new Version(1,2,0,false).compareTo(new Version(1,2,0,true))) == -1
  }
}

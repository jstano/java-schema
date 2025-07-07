package com.stano.schema.model

import spock.lang.Specification

public class VersionSpec extends Specification {
  def "creating a Version with only a major and minor version should return those values and have the revision set to 0 and the snapshot set to false"() {
    def version = new Version(8, 12)

    expect:
    version.majorVersion == 8
    version.minorVersion == 12
    version.patchVersion == 0
    !version.preReleaseSuffix
  }

  def "creating a Version with only a major, minor and revision values should return those values and have the snapshot set to false"() {
    def version = new Version(8, 12, 3)

    expect:
    version.majorVersion == 8
    version.minorVersion == 12
    version.patchVersion == 3
    !version.preReleaseSuffix
  }

  def "creating a Version with all values should return the values"() {
    def version = new Version(8, 12, 3, true)

    expect:
    version.majorVersion == 8
    version.minorVersion == 12
    version.patchVersion == 3
    version.preReleaseSuffix
  }

  def "creating a Version from a string with only a major and minor version should return those values and have the revision set to 0 and the snapshot set to false"() {
    def version = new Version("08.12")

    expect:
    version.majorVersion == 8
    version.minorVersion == 12
    version.patchVersion == 0
    !version.preReleaseSuffix
  }

  def "creating a Version from a string with only a major, minor and revision values should return those values and have the snapshot set to false"() {
    def version = new Version("08.12.03")

    expect:
    version.majorVersion == 8
    version.minorVersion == 12
    version.patchVersion == 3
    !version.preReleaseSuffix
  }

  def "creating a Version from a string with only a major, minor and snapshot values should return those values and have the revision set yo 0"() {
    def version = new Version("08.12-SNAPSHOT")

    expect:
    version.majorVersion == 8
    version.minorVersion == 12
    version.patchVersion == 0
    version.preReleaseSuffix
  }

  def "creating a Version from a string with all values should return the values"() {
    def version = new Version("08.12.03-SNAPSHOT")

    expect:
    version.majorVersion == 8
    version.minorVersion == 12
    version.patchVersion == 3
    version.preReleaseSuffix
  }

  def "All combination of versions should return the correct strings from the toString method"() {
    expect:
    version.toString() == versionStr

    where:
    version                          | versionStr
    new Version("08.12")             | "08.12"
    new Version("08.12.03")          | "08.12.03"
    new Version("08.12.03-SNAPSHOT") | "08.12.03-SNAPSHOT"
  }

  def "equals should return the correct value for various combinations of versions"() {
    expect:
    version1.equals(version1)
    version1.equals(version2) == expectedResult

    where:
    version1                         | version2                         | expectedResult
    new Version("08.12")             | new Version("08.12")             | true
    new Version("08.12.03")          | new Version("08.12.03")          | true
    new Version("08.12.03-SNAPSHOT") | new Version("08.12.03-SNAPSHOT") | true

    new Version("08.12")             | new Version("08.12.03")          | false
    new Version("08.12")             | new Version("08.12.03-SNAPSHOT") | false
    new Version("08.12")             | new Version("09.12")             | false
    new Version("08.12")             | new Version("09.12.04")          | false
    new Version("08.12")             | new Version("09.12.04-SNAPSHOT") | false

    new Version("08.12")             | null                             | false
    new Version("08.12")             | "08.12"                          | false
  }

  def "hashCode should be unique"() {
    def version1a = new Version("08.12")
    def version1b = new Version("08.12")
    def version2a = new Version("08.12.01")
    def version2b = new Version("08.12.01")
    def version3a = new Version("08.12.01-SNAPSHOT")
    def version3b = new Version("08.12.01-SNAPSHOT")

    def map = new HashMap()
    map.put(version1a, version1a)
    map.put(version2a, version2a)
    map.put(version3a, version3a)

    expect:
    map.get(version1b) == version1a
    map.get(version2b) == version2a
    map.get(version3b) == version3a
  }

  def "compareTo should return the correct value for various combinations of versions"() {
    expect:
    version1.compareTo(version1) == 0
    version1.compareTo(version2) == expectedResult

    where:
    version1                         | version2                         | expectedResult
    new Version("08.12")             | new Version("08.12")             | 0
    new Version("08.12.03")          | new Version("08.12.03")          | 0
    new Version("08.12.03-SNAPSHOT") | new Version("08.12.03-SNAPSHOT") | 0

    new Version("08.12")             | new Version("09.12")             | -1
    new Version("09.12")             | new Version("08.12")             | 1

    new Version("08.12")             | new Version("08.14")             | -1
    new Version("08.14")             | new Version("08.12")             | 1

    new Version("08.12.03")          | new Version("08.12.04")          | -1
    new Version("08.12.04")          | new Version("08.12.03")          | 1

    new Version("08.12.03")          | new Version("08.12.03-SNAPSHOT") | -1
    new Version("08.12.03-SNAPSHOT") | new Version("08.12.03")          | 1

    new Version("08.12.03")          | new Version("08.12.04-SNAPSHOT") | -1
    new Version("08.12.04-SNAPSHOT") | new Version("08.12.03")          | 1
  }
}

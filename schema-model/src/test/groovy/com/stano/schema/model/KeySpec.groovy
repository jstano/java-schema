package com.stano.schema.model

import spock.lang.Specification

class KeySpec extends Specification {
  def key = new Key(KeyType.PRIMARY, [new KeyColumn('a'), new KeyColumn('b'), new KeyColumn('c')], false, false, true, null)
  def key2a = new Key(KeyType.PRIMARY, [new KeyColumn('a'), new KeyColumn('b'), new KeyColumn('c')], false, false, true, null)
  def key2b = new Key(KeyType.PRIMARY, [new KeyColumn('c'), new KeyColumn('b'), new KeyColumn('a')], false, false, true, null)
  def key3 = new Key(KeyType.PRIMARY, [new KeyColumn('a'), new KeyColumn('b'), new KeyColumn('z')], false, false, true, null)

  def "containsColumn should return true if the column is in the key"() {
    expect:
    key.containsColumn('a')
    key.containsColumn('b')
    key.containsColumn('c')
    !key.containsColumn('z')
  }

  def "getColumnsAsString should return all the column names in the key separated by commas"() {
    expect:
    key.columnsAsString == 'a,b,c'
    key2a.columnsAsString == 'a,b,c'
    key2b.columnsAsString == 'c,b,a'
    key3.columnsAsString == 'a,b,z'
  }
}

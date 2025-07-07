package com.stano.schema.installer.schemacontext

import spock.lang.Specification

import java.sql.Connection

class FileSchemaContextSpec extends Specification {
  def "the FileSchemaContext should work"() {
    def file = Mock(File) {
      toURI() >> new URI('file://test')
    }
    def connection = Mock(Connection)
    def fileSchemaContext = new FileSchemaContext(file)

    expect:
    fileSchemaContext.getSchemaUrl().toString() == 'file://test'
    fileSchemaContext.getMigrationScriptLocator(connection) == null
    !fileSchemaContext.schemaIsInstalled(connection)
  }
}

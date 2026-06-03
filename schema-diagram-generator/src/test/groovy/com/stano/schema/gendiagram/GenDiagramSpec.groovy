package com.stano.schema.gendiagram

import com.stano.schema.model.Schema
import spock.lang.Specification

class GenDiagramSpec extends Specification {
  def "should generate a diagram and close the writer"() {
    def generated = false
    def writerClosed = false

    def schema = Mock(Schema)
    def writer = Mock(PrintWriter) {
      close() >> { writerClosed = true }
    }
    def diagramGenerator = Mock(DiagramGenerator) {
      generate() >> { generated = true }
    }
    def factory = Mock(DiagramGeneratorFactory) {
      createDiagramGenerator(_) >> { args ->
        def opts = args[0]
        if (opts.schema == schema && opts.writer == writer && opts.format == DiagramFormat.MERMAID) {
          return diagramGenerator
        }
        return null
      }
    }

    def genDiagram = new GenDiagram()
    genDiagram.diagramGeneratorFactory = factory

    genDiagram.generateDiagram(schema, DiagramFormat.MERMAID, writer)

    expect:
    generated
    writerClosed
  }

  def "should generate a plantuml diagram and close the writer"() {
    def generated = false
    def writerClosed = false

    def schema = Mock(Schema)
    def writer = Mock(PrintWriter) {
      close() >> { writerClosed = true }
    }
    def diagramGenerator = Mock(DiagramGenerator) {
      generate() >> { generated = true }
    }
    def factory = Mock(DiagramGeneratorFactory) {
      createDiagramGenerator(_) >> { args ->
        def opts = args[0]
        if (opts.schema == schema && opts.writer == writer && opts.format == DiagramFormat.PLANTUML) {
          return diagramGenerator
        }
        return null
      }
    }

    def genDiagram = new GenDiagram()
    genDiagram.diagramGeneratorFactory = factory

    genDiagram.generateDiagram(schema, DiagramFormat.PLANTUML, writer)

    expect:
    generated
    writerClosed
  }
}

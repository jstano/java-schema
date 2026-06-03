package com.stano.schema.gendiagram.impl

import com.stano.schema.gendiagram.DiagramFormat
import com.stano.schema.gendiagram.DiagramGeneratorOptions
import com.stano.schema.model.Column
import com.stano.schema.model.ColumnType
import com.stano.schema.model.Key
import com.stano.schema.model.KeyColumn
import com.stano.schema.model.KeyType
import com.stano.schema.model.LockEscalation
import com.stano.schema.model.Relation
import com.stano.schema.model.RelationType
import com.stano.schema.model.Schema
import com.stano.schema.model.Table
import spock.lang.Specification

class MermaidERDiagramGeneratorSpec extends Specification {

  private Schema buildSchema() {
    def schema = new Schema(null)
    schema
  }

  private Table buildTable(Schema schema, String name) {
    def table = new Table(schema, "dbo", name, null, LockEscalation.AUTO, false)
    schema.addTable(table)
    table
  }

  def "should output erDiagram header"() {
    def schema = buildSchema()
    buildTable(schema, "CUSTOMER")

    def output = new StringWriter()
    def writer = new PrintWriter(output)
    def generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID))

    when:
    generator.generate()
    writer.flush()

    then:
    output.toString().startsWith("erDiagram")
  }

  def "should output table with columns"() {
    def schema = buildSchema()
    def table = buildTable(schema, "CUSTOMER")
    table.getColumns().add(new Column("id", ColumnType.INT, 0, true))
    table.getColumns().add(new Column("name", ColumnType.VARCHAR, 255, false))
    table.getKeys().add(new Key(KeyType.PRIMARY, [new KeyColumn("id")]))

    def output = new StringWriter()
    def writer = new PrintWriter(output)
    def generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID))

    when:
    generator.generate()
    writer.flush()
    def text = output.toString()

    then:
    text.contains("CUSTOMER {")
    text.contains("int id PK")
    text.contains("string name")
    text.contains("}")
  }

  def "should annotate FK columns"() {
    def schema = buildSchema()
    def customer = buildTable(schema, "CUSTOMER")
    customer.getColumns().add(new Column("id", ColumnType.INT, 0, true))
    customer.getKeys().add(new Key(KeyType.PRIMARY, [new KeyColumn("id")]))

    def order = buildTable(schema, "ORDER")
    order.getColumns().add(new Column("id", ColumnType.INT, 0, true))
    order.getColumns().add(new Column("customer_id", ColumnType.INT, 0, true))
    order.getKeys().add(new Key(KeyType.PRIMARY, [new KeyColumn("id")]))
    order.getRelations().add(new Relation("ORDER", "customer_id", "CUSTOMER", "id", RelationType.ENFORCE, false))

    def output = new StringWriter()
    def writer = new PrintWriter(output)
    def generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID))

    when:
    generator.generate()
    writer.flush()
    def text = output.toString()

    then:
    text.contains("int customer_id FK")
  }

  def "should output relationship lines"() {
    def schema = buildSchema()
    def customer = buildTable(schema, "CUSTOMER")
    customer.getColumns().add(new Column("id", ColumnType.INT, 0, true))
    customer.getKeys().add(new Key(KeyType.PRIMARY, [new KeyColumn("id")]))

    def order = buildTable(schema, "ORDER")
    order.getColumns().add(new Column("id", ColumnType.INT, 0, true))
    order.getColumns().add(new Column("customer_id", ColumnType.INT, 0, true))
    order.getKeys().add(new Key(KeyType.PRIMARY, [new KeyColumn("id")]))
    order.getRelations().add(new Relation("ORDER", "customer_id", "CUSTOMER", "id", RelationType.ENFORCE, false))

    def output = new StringWriter()
    def writer = new PrintWriter(output)
    def generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID))

    when:
    generator.generate()
    writer.flush()
    def text = output.toString()

    then:
    text.contains('ORDER }o--|| CUSTOMER : "customer_id"')
  }

  def "should use o| cardinality for SETNULL relations"() {
    def schema = buildSchema()
    def customer = buildTable(schema, "CUSTOMER")
    customer.getColumns().add(new Column("id", ColumnType.INT, 0, true))

    def order = buildTable(schema, "ORDER")
    order.getColumns().add(new Column("customer_id", ColumnType.INT, 0, false))
    order.getRelations().add(new Relation("ORDER", "customer_id", "CUSTOMER", "id", RelationType.SETNULL, false))

    def output = new StringWriter()
    def writer = new PrintWriter(output)
    def generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID))

    when:
    generator.generate()
    writer.flush()
    def text = output.toString()

    then:
    text.contains('ORDER }o--o| CUSTOMER : "customer_id"')
  }
}

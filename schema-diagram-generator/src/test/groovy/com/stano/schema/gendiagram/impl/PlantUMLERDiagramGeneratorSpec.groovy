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

class PlantUMLERDiagramGeneratorSpec extends Specification {

  private Schema buildSchema() {
    new Schema(null)
  }

  private Table buildTable(Schema schema, String name) {
    def table = new Table(schema, "dbo", name, null, LockEscalation.AUTO, false)
    schema.addTable(table)
    table
  }

  def "should output @startuml and @enduml wrappers"() {
    def schema = buildSchema()
    buildTable(schema, "CUSTOMER")

    def output = new StringWriter()
    def writer = new PrintWriter(output)
    def generator = new PlantUMLERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.PLANTUML))

    when:
    generator.generate()
    writer.flush()
    def text = output.toString()

    then:
    text.contains("@startuml")
    text.contains("@enduml")
  }

  def "should output entity with columns"() {
    def schema = buildSchema()
    def table = buildTable(schema, "CUSTOMER")
    table.getColumns().add(new Column("id", ColumnType.INT, 0, true))
    table.getColumns().add(new Column("name", ColumnType.VARCHAR, 255, false))
    table.getKeys().add(new Key(KeyType.PRIMARY, [new KeyColumn("id")]))

    def output = new StringWriter()
    def writer = new PrintWriter(output)
    def generator = new PlantUMLERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.PLANTUML))

    when:
    generator.generate()
    writer.flush()
    def text = output.toString()

    then:
    text.contains("entity CUSTOMER {")
    text.contains("* id : INT <<PK>>")
    text.contains("--")
    text.contains("  name : VARCHAR(255)")
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
    def generator = new PlantUMLERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.PLANTUML))

    when:
    generator.generate()
    writer.flush()
    def text = output.toString()

    then:
    text.contains("customer_id : INT <<FK>>")
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
    def generator = new PlantUMLERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.PLANTUML))

    when:
    generator.generate()
    writer.flush()
    def text = output.toString()

    then:
    text.contains("ORDER }o--|| CUSTOMER : customer_id")
  }
}

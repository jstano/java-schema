package com.stano.schema.gendiagram.impl;

import com.stano.schema.gendiagram.DiagramFormat;
import com.stano.schema.gendiagram.DiagramGeneratorOptions;
import com.stano.schema.model.Column;
import com.stano.schema.model.ColumnType;
import com.stano.schema.model.Key;
import com.stano.schema.model.KeyColumn;
import com.stano.schema.model.KeyType;
import com.stano.schema.model.LockEscalation;
import com.stano.schema.model.Relation;
import com.stano.schema.model.RelationType;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MermaidERDiagramGenerator")
class MermaidERDiagramGeneratorTest {

  private Schema buildSchema() {
    return new Schema(null);
  }

  private Table buildTable(Schema schema, String name) {
    Table table = new Table(schema, "dbo", name, null, LockEscalation.AUTO, false);
    schema.addTable(table);
    return table;
  }

  @Test
  @DisplayName("should output erDiagram header")
  void shouldOutputErDiagramHeader() {
    Schema schema = buildSchema();
    buildTable(schema, "CUSTOMER");

    StringWriter output = new StringWriter();
    PrintWriter writer = new PrintWriter(output);
    MermaidERDiagramGenerator generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID));

    generator.generate();
    writer.flush();

    assertTrue(output.toString().startsWith("erDiagram"));
  }

  @Test
  @DisplayName("should output table with columns")
  void shouldOutputTableWithColumns() {
    Schema schema = buildSchema();
    Table table = buildTable(schema, "CUSTOMER");
    table.getColumns().add(new Column("id", ColumnType.INT, 0, true));
    table.getColumns().add(new Column("name", ColumnType.VARCHAR, 255, false));
    table.getKeys().add(new Key(KeyType.PRIMARY, new java.util.ArrayList<>(java.util.List.of(new KeyColumn("id")))));

    StringWriter output = new StringWriter();
    PrintWriter writer = new PrintWriter(output);
    MermaidERDiagramGenerator generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID));

    generator.generate();
    writer.flush();
    String text = output.toString();

    assertTrue(text.contains("CUSTOMER {"));
    assertTrue(text.contains("int id PK"));
    assertTrue(text.contains("string name"));
    assertTrue(text.contains("}"));
  }

  @Test
  @DisplayName("should annotate FK columns")
  void shouldAnnotateFKColumns() {
    Schema schema = buildSchema();
    Table customer = buildTable(schema, "CUSTOMER");
    customer.getColumns().add(new Column("id", ColumnType.INT, 0, true));
    customer.getKeys().add(new Key(KeyType.PRIMARY, new java.util.ArrayList<>(java.util.List.of(new KeyColumn("id")))));

    Table order = buildTable(schema, "ORDER");
    order.getColumns().add(new Column("id", ColumnType.INT, 0, true));
    order.getColumns().add(new Column("customer_id", ColumnType.INT, 0, true));
    order.getKeys().add(new Key(KeyType.PRIMARY, new java.util.ArrayList<>(java.util.List.of(new KeyColumn("id")))));
    order.getRelations().add(new Relation("ORDER", "customer_id", "CUSTOMER", "id", RelationType.ENFORCE, false));

    StringWriter output = new StringWriter();
    PrintWriter writer = new PrintWriter(output);
    MermaidERDiagramGenerator generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID));

    generator.generate();
    writer.flush();
    String text = output.toString();

    assertTrue(text.contains("int customer_id FK"));
  }

  @Test
  @DisplayName("should output relationship lines")
  void shouldOutputRelationshipLines() {
    Schema schema = buildSchema();
    Table customer = buildTable(schema, "CUSTOMER");
    customer.getColumns().add(new Column("id", ColumnType.INT, 0, true));
    customer.getKeys().add(new Key(KeyType.PRIMARY, new java.util.ArrayList<>(java.util.List.of(new KeyColumn("id")))));

    Table order = buildTable(schema, "ORDER");
    order.getColumns().add(new Column("id", ColumnType.INT, 0, true));
    order.getColumns().add(new Column("customer_id", ColumnType.INT, 0, true));
    order.getKeys().add(new Key(KeyType.PRIMARY, new java.util.ArrayList<>(java.util.List.of(new KeyColumn("id")))));
    order.getRelations().add(new Relation("ORDER", "customer_id", "CUSTOMER", "id", RelationType.ENFORCE, false));

    StringWriter output = new StringWriter();
    PrintWriter writer = new PrintWriter(output);
    MermaidERDiagramGenerator generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID));

    generator.generate();
    writer.flush();
    String text = output.toString();

    assertTrue(text.contains("ORDER }o--|| CUSTOMER : \"customer_id\""));
  }

  @Test
  @DisplayName("should use o| cardinality for SETNULL relations")
  void shouldUseOPipeCardinalityForSETNULLRelations() {
    Schema schema = buildSchema();
    Table customer = buildTable(schema, "CUSTOMER");
    customer.getColumns().add(new Column("id", ColumnType.INT, 0, true));

    Table order = buildTable(schema, "ORDER");
    order.getColumns().add(new Column("customer_id", ColumnType.INT, 0, false));
    order.getRelations().add(new Relation("ORDER", "customer_id", "CUSTOMER", "id", RelationType.SETNULL, false));

    StringWriter output = new StringWriter();
    PrintWriter writer = new PrintWriter(output);
    MermaidERDiagramGenerator generator = new MermaidERDiagramGenerator(new DiagramGeneratorOptions(schema, writer, DiagramFormat.MERMAID));

    generator.generate();
    writer.flush();
    String text = output.toString();

    assertTrue(text.contains("ORDER }o--o| CUSTOMER : \"customer_id\""));
  }
}

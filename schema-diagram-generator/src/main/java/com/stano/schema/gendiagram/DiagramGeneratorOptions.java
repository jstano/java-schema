package com.stano.schema.gendiagram;

import com.stano.schema.model.Schema;

import java.io.PrintWriter;

public class DiagramGeneratorOptions {
  private final Schema schema;
  private final PrintWriter writer;
  private final DiagramFormat format;

  public DiagramGeneratorOptions(Schema schema, PrintWriter writer, DiagramFormat format) {
    this.schema = schema;
    this.writer = writer;
    this.format = format;
  }

  public Schema getSchema() {
    return schema;
  }

  public PrintWriter getWriter() {
    return writer;
  }

  public DiagramFormat getFormat() {
    return format;
  }
}

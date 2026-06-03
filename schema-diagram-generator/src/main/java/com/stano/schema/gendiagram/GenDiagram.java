package com.stano.schema.gendiagram;

import com.stano.schema.model.Schema;
import com.stano.schema.parser.SchemaParser;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;

public class GenDiagram {
  private DiagramGeneratorFactory diagramGeneratorFactory = new DiagramGeneratorFactory();

  public void generateDiagram(Schema schema, DiagramFormat format, PrintWriter writer) {
    try {
      DiagramGenerator generator = diagramGeneratorFactory.createDiagramGenerator(
        new DiagramGeneratorOptions(schema, writer, format));

      generator.generate();
    }
    finally {
      writer.close();
    }
  }

  private static File createOutputFile(String schemaFilename, DiagramFormat format) {
    String baseFilename = schemaFilename.substring(0, schemaFilename.lastIndexOf('.'));
    String extension = format == DiagramFormat.MERMAID ? "mmd" : "puml";
    return new File(String.format("%s.%s", baseFilename, extension));
  }

  private static String getSchemaFileName(String schemaFileName) {
    if (schemaFileName.matches("^[a-zA-Z]:.*$")) {
      return "/" + schemaFileName;
    }
    return schemaFileName;
  }

  public static void main(String[] args) {
    try {
      if (args.length < 2) {
        System.out.println("USAGE: GenDiagram <format> <schema-filename>");
        System.out.println("   where <format> is one of: MERMAID, PLANTUML");
        System.exit(1);
      }

      DiagramFormat format = DiagramFormat.valueOf(args[0].toUpperCase());
      String schemaFilename = getSchemaFileName(args[1]);
      URL schemaURL = new URI("file://" + schemaFilename).toURL();
      Schema schema = new SchemaParser().parseSchema(schemaURL);

      GenDiagram genDiagram = new GenDiagram();
      genDiagram.generateDiagram(schema,
                                 format,
                                 new PrintWriter(new FileWriter(createOutputFile(schemaFilename, format))));
    }
    catch (Throwable x) {
      x.printStackTrace();
    }
  }
}

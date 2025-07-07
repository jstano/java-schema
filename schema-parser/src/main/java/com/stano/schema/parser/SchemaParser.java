package com.stano.schema.parser;

import com.stano.schema.model.Schema;
import com.stano.schema.parser.xmlparser.SchemaContentHandler;
import com.stano.schema.parser.xmlparser.XMLSchemaParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SchemaParser {
  public Schema parseSchema(URL schemaURL) {
    try {
      return parseSchema(schemaURL, schemaURL.openStream());
    }
    catch (IOException x) {
      throw new SchemaParserException(x);
    }
  }

  public Schema parseSchema(URL schemaURL, InputStream inputStream) {
    try {
      Schema schema = new Schema(schemaURL);

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        XMLSchemaParser parser = new XMLSchemaParser();

        parser.parse(reader, new SchemaContentHandler(schema, schemaURL));
      }

      schema.sortTablesByName();
      schema.buildReverseRelations();

      return schema;
    }
    catch (Exception x) {
      throw new IllegalStateException(x);
    }
  }
}

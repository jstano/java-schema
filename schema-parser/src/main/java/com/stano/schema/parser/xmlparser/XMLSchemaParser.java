package com.stano.schema.parser.xmlparser;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;

public class XMLSchemaParser {
  public void parse(BufferedReader reader, SchemaContentHandler schemaContentHandler) {
    try {
      XMLReader parser = createXMLReader();

      parser.setContentHandler(schemaContentHandler);
      parser.parse(new InputSource(reader));
    }
    catch (Exception x) {
      throw new RuntimeException(x);
    }
  }

  private XMLReader createXMLReader() {
    try {
      // Allocate and configure JAXP SAX parser factory
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setValidating(true);
      factory.setNamespaceAware(true);
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

      // Allocate parser
      SAXParser parser = factory.newSAXParser();

      // Return configured SAX XMLReader
      XMLReader reader = parser.getXMLReader();
      reader.setErrorHandler(new DefaultHandler());

      return reader;
    }
    catch (Exception x) {
      throw new RuntimeException(x);
    }
  }
}

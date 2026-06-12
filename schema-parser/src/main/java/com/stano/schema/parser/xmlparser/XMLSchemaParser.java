package com.stano.schema.parser.xmlparser;

import java.io.BufferedReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XMLSchemaParser {
  public void parse(BufferedReader reader, SchemaContentHandler schemaContentHandler) {
    try {
      XMLReader parser = createXMLReader();

      parser.setContentHandler(schemaContentHandler);
      parser.parse(new InputSource(reader));
    } catch (Exception x) {
      throw new RuntimeException(x);
    }
  }

  private XMLReader createXMLReader() {
    try {
      // Load XSD schema from classpath
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = schemaFactory.newSchema(getClass().getResource("/schema.xsd"));

      // Allocate and configure JAXP SAX parser factory
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setSchema(schema);
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
    } catch (Exception x) {
      throw new RuntimeException(x);
    }
  }
}

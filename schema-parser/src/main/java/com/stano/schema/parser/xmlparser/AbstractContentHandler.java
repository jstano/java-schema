package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public abstract class AbstractContentHandler implements ContentHandler {
  private final SchemaContentHandler schemaContentHandler;
  protected final Schema schema;

  private StringBuilder content;

  protected AbstractContentHandler(SchemaContentHandler schemaContentHandler, Schema schema) {
    this.schemaContentHandler = schemaContentHandler;
    this.schema = schema;
  }

  @Override
  public void setDocumentLocator(Locator locator) {
  }

  @Override
  public void startDocument() throws SAXException {
  }

  @Override
  public void endDocument() throws SAXException {
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
  }

  @Override
  public void endPrefixMapping(String prefix) throws SAXException {
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (content != null) {
      content.append(new String(ch, start, length));
    }
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
  }

  @Override
  public void processingInstruction(String target, String data) throws SAXException {
  }

  @Override
  public void skippedEntity(String name) throws SAXException {
  }

  protected String getCurrentSchemaName() {
    return schemaContentHandler.getCurrentSchemaName();
  }

  protected void initContentStorage() {
    if (content == null) {
      content = new StringBuilder();
    }

    content.setLength(0);
  }

  protected String getContent() {
    if (content != null) {
      return content.toString().trim();
    }

    return "";
  }
}

package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.net.URL;
import java.util.Stack;

public class SchemaContentHandler extends AbstractContentHandler {
  private final URL schemaURL;

  private final ContentHandler databaseContentHandler;
  private final ContentHandler tableContentHandler;
  private final ContentHandler enumContentHandler;
  private final ContentHandler viewContentHandler;
  private final ContentHandler functionsContentHandler;
  private final ContentHandler proceduresContentHandler;
  private final ContentHandler otherSqlContentHandler;
  private final ContentHandler customSqlContentHandler;

  private final Stack<ContentHandler> contentHandlerStack = new Stack<ContentHandler>();

  private String currentSchemaName;

  public SchemaContentHandler(Schema schema, URL schemaURL) {
    super(null, schema);

    this.schemaURL = schemaURL;

    databaseContentHandler = new DatabaseContentHandler(schema);
    tableContentHandler = new TableContentHandler(this, schema);
    enumContentHandler = new EnumContentHandler(schema);
    viewContentHandler = new ViewContentHandler(this, schema);
    functionsContentHandler = new FunctionsContentHandler(this, schema);
    proceduresContentHandler = new ProceduresContentHandler(this, schema);
    otherSqlContentHandler = new OtherSqlContentHandler(schema);
    customSqlContentHandler = new CustomSQLContentHandler(this, schema);
  }

  public String getCurrentSchemaName() {
    return currentSchemaName == null ? "public" : currentSchemaName;
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.startElement(namespaceURI, localName, qName, atts);
    }

    ContentHandler contentHandler = getContentHandlerForElementType(localName);

    if (contentHandler != null) {
      contentHandlerStack.push(contentHandler);

      contentHandler.startElement(namespaceURI, localName, qName, atts);
    }

    if (localName.equals("schema")) {
      currentSchemaName = atts.getValue("name");
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.endElement(namespaceURI, localName, qName);
    }

    if (getContentHandlerForElementType(localName) != null) {
      contentHandlerStack.pop();
    }

    if (localName.equals("schema")) {
      currentSchemaName = null;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.characters(ch, start, length);
    }
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.startPrefixMapping(prefix, uri);
    }
  }

  @Override
  public void endPrefixMapping(String prefix) throws SAXException {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.endPrefixMapping(prefix);
    }
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.ignorableWhitespace(ch, start, length);
    }
  }

  @Override
  public void processingInstruction(String target, String data) throws SAXException {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.processingInstruction(target, data);
    }
  }

  @Override
  public void setDocumentLocator(Locator locator) {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.setDocumentLocator(locator);
    }
  }

  @Override
  public void skippedEntity(String name) throws SAXException {
    ContentHandler currentContentHandler = getCurrentContentHandler();

    if (currentContentHandler != null) {
      currentContentHandler.skippedEntity(name);
    }
  }

  private ContentHandler getContentHandlerForElementType(String localName) {
    return switch (localName) {
      case "database" -> databaseContentHandler;
      case "table" -> tableContentHandler;
      case "enum" -> enumContentHandler;
      case "view" -> viewContentHandler;
      case "functions" -> functionsContentHandler;
      case "procedures" -> proceduresContentHandler;
      case "otherSql" -> otherSqlContentHandler;
      case "customSQL" -> customSqlContentHandler;
      default -> null;
    };
  }

  private ContentHandler getCurrentContentHandler() {
    if (contentHandlerStack.empty()) {
      return null;
    }

    return contentHandlerStack.peek();
  }
}

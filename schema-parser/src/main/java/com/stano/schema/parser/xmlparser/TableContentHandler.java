package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.LockEscalation;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;
import com.stano.schema.model.TableOption;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TableContentHandler extends AbstractContentHandler {
  private Table table;
  protected ContentHandler contentHandler;

  public TableContentHandler(SchemaContentHandler schemaContentHandler, Schema schema) {
    super(schemaContentHandler, schema);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (contentHandler != null) {
      contentHandler.startElement(namespaceURI, localName, qName, atts);
      return;
    }

    switch (localName) {
      case "table" -> table = parseTable(atts);
      case "columns" -> {
        contentHandler = new TableColumnContentHandler(schema, table, this);
        contentHandler.startElement(namespaceURI, localName, qName, atts);
      }
      case "keys" -> {
        contentHandler = new KeyContentHandler(schema, table, this);
        contentHandler.startElement(namespaceURI, localName, qName, atts);
      }
      case "aggregations" -> {
        contentHandler = new AggregationContentHandler(schema, table, this);
        contentHandler.startElement(namespaceURI, localName, qName, atts);
      }
      case "triggers" -> {
        contentHandler = new TriggerContentHandler(schema, table, this);
        contentHandler.startElement(namespaceURI, localName, qName, atts);
      }
      case "relations" -> {
        contentHandler = new RelationContentHandler(schema, table, this);
        contentHandler.startElement(namespaceURI, localName, qName, atts);
      }
      case "constraints" -> {
        contentHandler = new ConstraintContentHandler(schema, table, this);
        contentHandler.startElement(namespaceURI, localName, qName, atts);
      }
      case "initialData" -> {
        contentHandler = new InitialDataContentHandler(schema, table, this);
        contentHandler.startElement(namespaceURI, localName, qName, atts);
      }
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (contentHandler != null) {
      contentHandler.characters(ch, start, length);
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (contentHandler != null) {
      contentHandler.endElement(namespaceURI, localName, qName);
      return;
    }

    if (localName.equals("table")) {
      schema.addTable(table);
    }
  }

  private Table parseTable(Attributes atts) {
    String lockEscalationStr = atts.getValue("lockEscalation");

    table = new Table(schema,
                      getCurrentSchemaName(),
                      atts.getValue("name"),
                      atts.getValue("exportDataColumn"),
                      StringUtils.isBlank(lockEscalationStr) ? null : LockEscalation.valueOf(lockEscalationStr.toUpperCase()),
                      Boolean.parseBoolean(atts.getValue("noExport")));

    if (atts.getValue("noExport") != null && "true".equalsIgnoreCase(atts.getValue("noExport"))) {
      table.getOptions().add(TableOption.NO_EXPORT);
    }

    if (atts.getValue("compress") != null && "true".equalsIgnoreCase(atts.getValue("compress"))) {
      table.getOptions().add(TableOption.COMPRESS);
    }

    if (atts.getValue("data") != null && "true".equalsIgnoreCase(atts.getValue("data"))) {
      table.getOptions().add(TableOption.DATA);
    }

    return table;
  }
}

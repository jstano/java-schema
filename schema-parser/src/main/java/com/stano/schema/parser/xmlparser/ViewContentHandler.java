package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Schema;
import com.stano.schema.model.View;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ViewContentHandler extends AbstractContentHandler {
  private String name;
  private DatabaseType databaseType;

  public ViewContentHandler(SchemaContentHandler schemaContentHandler, Schema schema) {
    super(schemaContentHandler, schema);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals("view")) {
      name = atts.getValue("name");
      databaseType = atts.getValue("databaseType") == null ? null : DatabaseType.fromString(atts.getValue("databaseType").toUpperCase());

      initContentStorage();
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (localName.equals("view")) {
      schema.addView(new View(getCurrentSchemaName(), name, getContent(), databaseType));
    }
  }
}

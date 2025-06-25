package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.Relation;
import com.stano.schema.model.RelationType;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RelationContentHandler extends AbstractContentHandler {
  private final Table table;
  private final TableContentHandler tableContentHandler;

  protected RelationContentHandler(Schema schema, Table table, TableContentHandler tableContentHandler) {
    super(null, schema);

    this.table = table;
    this.tableContentHandler = tableContentHandler;
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals("relation")) {
      table.getRelations()
           .add(new Relation(table.getName(),
                             atts.getValue("src"),
                             atts.getValue("table"),
                             atts.getValue("column"),
                             RelationType.valueOf(atts.getValue("type").toUpperCase()),
                             Boolean.parseBoolean(atts.getValue("disableUsageChecking"))));
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (localName.equals("relations")) {
      tableContentHandler.contentHandler = null;
    }
  }
}

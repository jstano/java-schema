package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.OtherSql;
import com.stano.schema.model.OtherSqlOrder;
import com.stano.schema.model.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class OtherSqlContentHandler extends AbstractContentHandler {
  private DatabaseType databaseType;
  private OtherSqlOrder order;

  public OtherSqlContentHandler(Schema schema) {
    super(null, schema);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals("otherSql")) {
      databaseType = DatabaseType.valueOf(atts.getValue("databaseType").toUpperCase());
      order = OtherSqlOrder.valueOf(atts.getValue("order").toUpperCase());

      initContentStorage();
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (localName.equals("otherSql")) {
      schema.addOtherSql(new OtherSql(databaseType, order, getContent()));
    }
  }
}

package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.InitialData;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class InitialDataContentHandler extends AbstractContentHandler {
  private final Table table;
  private final TableContentHandler tableContentHandler;

  private DatabaseType databaseType;

  protected InitialDataContentHandler(Schema schema, Table table, TableContentHandler tableContentHandler) {
    super(null, schema);

    this.table = table;
    this.tableContentHandler = tableContentHandler;
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals("sql")) {
      initContentStorage();

      String databaseType = atts.getValue("databaseType");

      if (StringUtils.isNotBlank(databaseType)) {
        this.databaseType = DatabaseType.valueOf(databaseType.toUpperCase());
      }
      else {
        this.databaseType = null;
      }
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (localName.equals("initialData")) {
      tableContentHandler.contentHandler = null;
    }
    else if (localName.equals("sql")) {
      String content = getContent();

      if (StringUtils.isNotBlank(content)) {
        table.getInitialData().add(new InitialData(content, databaseType));
      }
    }
  }
}

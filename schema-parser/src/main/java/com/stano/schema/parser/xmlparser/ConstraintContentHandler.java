package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.Constraint;
import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConstraintContentHandler extends AbstractContentHandler {
  private final Table table;
  private final TableContentHandler tableContentHandler;

  private String name;
  private DatabaseType databaseType;

  protected ConstraintContentHandler(Schema schema, Table table, TableContentHandler tableContentHandler) {
    super(null, schema);

    this.table = table;
    this.tableContentHandler = tableContentHandler;
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals("constraint")) {
      initContentStorage();

      this.name = atts.getValue("name");

      String databaseType = atts.getValue("databaseType");

      if (StringUtils.isNotBlank(databaseType)) {
        this.databaseType = DatabaseType.fromString(databaseType.toUpperCase());
      }
      else {
        this.databaseType = null;
      }
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (localName.equals("constraints")) {
      tableContentHandler.contentHandler = null;
    }
    else if (localName.equals("constraint")) {
      String content = getContent();

      if (StringUtils.isNotBlank(content)) {
        table.getConstraints().add(new Constraint(name, content, databaseType));
      }
    }
  }
}

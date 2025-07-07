package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Function;
import com.stano.schema.model.OtherSql;
import com.stano.schema.model.OtherSqlOrder;
import com.stano.schema.model.Procedure;
import com.stano.schema.model.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Collections;

public class CustomSQLContentHandler extends AbstractContentHandler {
  private DatabaseType databaseType;
  private String name;

  public CustomSQLContentHandler(SchemaContentHandler schemaContentHandler, Schema schema) {
    super(schemaContentHandler, schema);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    switch (localName) {
      case "customSQL" -> databaseType = DatabaseType.valueOf(atts.getValue("databaseType").toUpperCase());
      case "function", "procedure" -> {
        name = atts.getValue("name");
        initContentStorage();
      }
      case "other" -> {
        name = null;
        initContentStorage();
      }
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    switch (localName) {
      case "function" -> schema.addFunctions(Collections.singletonList(new Function(getCurrentSchemaName(), name, databaseType, getContent())));
      case "procedure" -> schema.addProcedures(Collections.singletonList(new Procedure(getCurrentSchemaName(), name, databaseType, getContent())));
      case "other" -> schema.addOtherSql(new OtherSql(databaseType, OtherSqlOrder.BOTTOM, getContent()));
    }
  }
}

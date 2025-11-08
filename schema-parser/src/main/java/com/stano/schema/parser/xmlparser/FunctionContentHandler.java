package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Function;
import com.stano.schema.model.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class FunctionContentHandler extends AbstractContentHandler {
  private String name;
  private DatabaseType databaseType;
  private List<VendorSql> vendorSqlList = new ArrayList<>();

  public FunctionContentHandler(SchemaContentHandler schemaContentHandler, Schema schema) {
    super(schemaContentHandler, schema);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    switch (localName) {
      case "function" -> {
        name = atts.getValue("name");
        vendorSqlList.clear();
      }
      case "sql" -> {
        databaseType = DatabaseType.fromString(atts.getValue("databaseType").toUpperCase());
        initContentStorage();
      }
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    switch (localName) {
      case "function" -> vendorSqlList.forEach(vendorSql -> {
        schema.addFunction(new Function(getCurrentSchemaName(), name, vendorSql.getDatabaseType(), vendorSql.getSql()));
      });
      case "sql" -> vendorSqlList.add(new VendorSql(databaseType, getContent()));
    }
  }
}

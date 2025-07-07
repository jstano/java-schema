package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Function;
import com.stano.schema.model.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class FunctionsContentHandler extends AbstractContentHandler {
  private String name;
  private DatabaseType databaseType;
  private List<Function> functions = new ArrayList<>();
  private List<VendorSql> vendorSqlList = new ArrayList<>();

  public FunctionsContentHandler(SchemaContentHandler schemaContentHandler, Schema schema) {
    super(schemaContentHandler, schema);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    switch (localName) {
      case "functions" -> functions.clear();
      case "function" -> {
        name = atts.getValue("name");
        vendorSqlList.clear();
      }
      case "sql" -> {
        databaseType = DatabaseType.valueOf(atts.getValue("databaseType").toUpperCase());
        initContentStorage();
      }
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    switch (localName) {
      case "functions" -> schema.addFunctions(functions);
      case "function" -> vendorSqlList.forEach(vendorSql -> {
        functions.add(new Function(getCurrentSchemaName(), name, vendorSql.getDatabaseType(), vendorSql.getSql()));
      });
      case "sql" -> vendorSqlList.add(new VendorSql(databaseType, getContent()));
    }
  }
}

package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.Key;
import com.stano.schema.model.KeyColumn;
import com.stano.schema.model.KeyType;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class KeyContentHandler extends AbstractContentHandler {
  private final Table table;
  private final TableContentHandler tableContentHandler;

  private KeyType keyType;
  private boolean cluster;
  private boolean compress;
  private boolean unique;
  private String include;
  private List<KeyColumn> columns = new ArrayList<>();

  protected KeyContentHandler(Schema schema, Table table, TableContentHandler tableContentHandler) {
    super(null, schema);

    this.table = table;
    this.tableContentHandler = tableContentHandler;
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals("primary") || localName.equals("unique") || localName.equals("index")) {
      keyType = KeyType.valueOf(localName.toUpperCase());
      cluster = Boolean.parseBoolean(atts.getValue("cluster"));
      compress = Boolean.parseBoolean(atts.getValue("compress"));
      unique = Boolean.parseBoolean(atts.getValue("unique"));
      include = atts.getValue("include");
      columns.clear();
    }
    else if (localName.equals("column")) {
      columns.add(new KeyColumn(atts.getValue("name")));
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    switch (localName) {
      case "keys" -> tableContentHandler.contentHandler = null;
      case "primary", "unique" -> table.getKeys().add(new Key(keyType, columns, cluster, compress, true, include));
      case "index" -> table.getIndexes().add(new Key(keyType, columns, false, compress, unique, include));
    }
  }
}

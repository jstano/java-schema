package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;
import com.stano.schema.model.Trigger;
import com.stano.schema.model.TriggerType;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TriggerContentHandler extends AbstractContentHandler {
  private final Table table;
  private final TableContentHandler tableContentHandler;

  private DatabaseType databaseType;
  private TriggerType triggerType;

  protected TriggerContentHandler(Schema schema, Table table, TableContentHandler tableContentHandler) {
    super(null, schema);

    this.table = table;
    this.tableContentHandler = tableContentHandler;
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if ((localName.equals("update") || localName.equals("delete"))) {
      databaseType = DatabaseType.fromString(atts.getValue("databaseType").toUpperCase());
      triggerType = TriggerType.valueOf(localName.toUpperCase());

      initContentStorage();
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (localName.equals("triggers")) {
      tableContentHandler.contentHandler = null;
    }
    else if (localName.equals("update") || localName.equals("delete")) {
      String content = getContent();

      if (StringUtils.isNotBlank(content)) {
        table.getTriggers().add(new Trigger(content, triggerType, databaseType));
      }

      databaseType = null;
      triggerType = null;
    }
  }
}

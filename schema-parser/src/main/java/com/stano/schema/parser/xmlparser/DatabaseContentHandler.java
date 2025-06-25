package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.ForeignKeyMode;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class DatabaseContentHandler extends AbstractContentHandler {
  public DatabaseContentHandler(Schema schema) {
    super(null, schema);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals("database")) {
      String versionAttribute = atts.getValue("version");
      String foreignKeyMode = atts.getValue("foreignKeyMode");
      String booleanMode = atts.getValue("booleanMode");

      if (schema.getVersion() == null && versionAttribute != null) {
        schema.setVersion(new Version(versionAttribute));
      }

      if (foreignKeyMode != null) {
        schema.setForeignKeyMode(ForeignKeyMode.valueOf(foreignKeyMode.toUpperCase()));
      }

      if (booleanMode != null) {
        schema.setBooleanMode(BooleanMode.valueOf(booleanMode.toUpperCase()));
      }
    }
  }
}

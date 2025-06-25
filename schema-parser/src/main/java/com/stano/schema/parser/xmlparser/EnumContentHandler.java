package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.EnumType;
import com.stano.schema.model.EnumValue;
import com.stano.schema.model.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EnumContentHandler extends AbstractContentHandler {
  private EnumType enumType;
  private EnumValue enumValue;

  public EnumContentHandler(Schema schema) {
    super(null, schema);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals("enum")) {
      enumType = new EnumType(atts.getValue("name"));
      schema.addEnumType(enumType);
    }
    else if (localName.equals("value")) {
      enumValue = new EnumValue(atts.getValue("name"), atts.getValue("code"));
      enumType.addValue(enumValue);
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (localName.equals("enum")) {
      enumType = null;
    }
    else if (localName.equals("value")) {
      enumValue = null;
    }
  }
}

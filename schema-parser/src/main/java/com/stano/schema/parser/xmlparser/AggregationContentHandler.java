package com.stano.schema.parser.xmlparser;

import com.stano.schema.model.Aggregation;
import com.stano.schema.model.AggregationColumn;
import com.stano.schema.model.AggregationFrequency;
import com.stano.schema.model.AggregationGroup;
import com.stano.schema.model.AggregationType;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class AggregationContentHandler extends AbstractContentHandler {
  private final Table table;
  private final TableContentHandler tableContentHandler;

  private String destinationTable;
  private String dateColumn;
  private String criteria;
  private String timestampColumn;
  private AggregationFrequency frequency;
  private List<AggregationColumn> aggregationColumns = new ArrayList<>();
  private List<AggregationGroup> aggregationGroups = new ArrayList<>();

  private String groupSource;
  private String groupDestination;
  private String groupSourceDerivedFrom;

  protected AggregationContentHandler(Schema schema, Table table, TableContentHandler tableContentHandler) {
    super(null, schema);

    this.table = table;
    this.tableContentHandler = tableContentHandler;
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    switch (localName) {
      case "aggregate" -> {
        destinationTable = atts.getValue("destinationTable");
        dateColumn = atts.getValue("dateColumn");
        criteria = atts.getValue("criteria");
        timestampColumn = atts.getValue("timestampColumn");
        frequency = AggregationFrequency.valueOf(atts.getValue("frequency").toUpperCase());
        aggregationColumns.clear();
        aggregationGroups.clear();
      }
      case "sum", "count" -> aggregationColumns.add(new AggregationColumn(AggregationType.valueOf(localName.toUpperCase()),
                                                                          atts.getValue("sourceColumn"),
                                                                          atts.getValue("destinationColumn")));
      case "column" -> {
        groupSource = atts.getValue("source");
        groupDestination = atts.getValue("destination");
        groupSourceDerivedFrom = atts.getValue("sourceDerivedFrom");
      }
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    switch (localName) {
      case "aggregations" -> tableContentHandler.contentHandler = null;
      case "aggregate" -> table.getAggregations().add(new Aggregation(destinationTable,
                                                                      dateColumn,
                                                                      criteria,
                                                                      timestampColumn,
                                                                      frequency,
                                                                      aggregationColumns,
                                                                      aggregationGroups));
      case "column" -> aggregationGroups.add(new AggregationGroup(groupSource,
                                                                  groupSourceDerivedFrom,
                                                                  groupDestination));
    }
  }
}

package com.stano.schema.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Aggregation")
class AggregationTest {

  @Test
  @DisplayName("constructor should store values and expose unmodifiable lists")
  void constructorShouldStoreValuesAndExposeUnmodifiableLists() {
    List<AggregationColumn> cols = new ArrayList<>(List.of(
        new AggregationColumn(AggregationType.SUM, "amount", "total_amount"),
        new AggregationColumn(AggregationType.COUNT, "id", "count_id")
    ));
    List<AggregationGroup> groups = new ArrayList<>(List.of(
        new AggregationGroup("country", null, "country"),
        new AggregationGroup("city", "country", "city")
    ));

    Aggregation agg = new Aggregation(
        "agg_sales",
        "sale_date",
        "status = 'CONFIRMED'",
        "ts_updated",
        AggregationFrequency.DAILY,
        cols,
        groups
    );

    assertEquals(agg.getDestinationTable(), "agg_sales");
    assertEquals(agg.getDateColumn(), "sale_date");
    assertEquals(agg.getCriteria(), "status = 'CONFIRMED'");
    assertEquals(agg.getTimeStampColumn(), "ts_updated");
    assertEquals(agg.getAggregationFrequency(), AggregationFrequency.DAILY);

    assertEquals(agg.getAggregationColumns().size(), 2);
    assertEquals(agg.getAggregationGroups().size(), 2);
    assertEquals(agg.getAggregationColumns().get(0).getSourceColumn(), "amount");
    assertEquals(agg.getAggregationColumns().get(0).getDestinationColumn(), "total_amount");
    assertEquals(agg.getAggregationColumns().get(0).getAggregationType(), AggregationType.SUM);
    assertEquals(agg.getAggregationGroups().get(1).getSource(), "city");
    assertEquals(agg.getAggregationGroups().get(1).getSourceDerivedFrom(), "country");
    assertEquals(agg.getAggregationGroups().get(1).getDestination(), "city");

    cols.clear();
    groups.clear();

    assertEquals(agg.getAggregationColumns().size(), 2);
    assertEquals(agg.getAggregationGroups().size(), 2);

    assertThrows(UnsupportedOperationException.class, () ->
        agg.getAggregationColumns().add(new AggregationColumn(AggregationType.COUNT, "x", "y"))
    );
  }
}

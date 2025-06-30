package com.stano.schema.importer;

import com.stano.schema.model.Column;
import com.stano.schema.model.Key;
import com.stano.schema.model.KeyType;
import com.stano.schema.model.Relation;
import com.stano.schema.model.Schema;
import com.stano.schema.model.Table;

import java.io.PrintWriter;
import java.util.List;

public class SchemaWriter {
  private final PrintWriter out;

  public SchemaWriter(PrintWriter out) {
    this.out = out;
  }

  public void outputSchema(Schema schema) {
    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.println("""
                  <database xmlns="http://stano.com/database"
                            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                            xsi:schemaLocation="http://stano.com/database http://stano.com/database"
                            version="1.0">""");

    for (int i = 0; i < schema.getTables().size(); i++) {
      var table = schema.getTables().get(i);
      outputTable(table);

      if (i < schema.getTables().size() - 1) {
        out.println();
      }
    }

    out.println("</database>");
  }

  private void outputTable(Table table) {
    out.printf("  <table name=\"%s\">\n", table.getName());

    if (!table.getColumns().isEmpty()) {
      out.printf("    <columns>\n");
      table.getColumns().forEach(this::outputColumn);
      out.printf("    </columns>\n");
    }

    if (!table.getKeys().isEmpty()) {
      out.printf("    <keys>\n");

      if (table.getPrimaryKey() != null) {
        outputPrimaryKey(table.getPrimaryKey());
      }

      outputUniqueKeys(table.getKeys());
      outputIndexKeys(table.getKeys());
      out.printf("    </keys>\n");
    }

    if (!table.getRelations().isEmpty()) {
      out.printf("    <relations>\n");
      table.getRelations().forEach(this::outputRelation);
      out.printf("    </relations>\n");
    }

    out.printf("  </table>\n");
  }

  private void outputColumn(Column column) {
    var name = column.getName();
    var columnType = column.getType();
    var length = column.getLength();
    var scale = column.getScale();

    if (column.isRequired()) {
      out.printf("      <column name=\"%s\" type=\"%s\"%s required=\"true\"/>\n",
                 name,
                 columnType.toString().toLowerCase(),
                 lengthScale(length, scale));
    }
    else {
      out.printf("      <column name=\"%s\" type=\"%s\"%s/>\n",
                 name,
                 columnType.toString().toLowerCase(),
                 lengthScale(length, scale));
    }
  }

  private String lengthScale(int length, int scale) {
    if (length == 0 && scale == 0) {
      return "";
    }

    if (length > 0 && scale == 0) {
      return String.format(" length=\"%d\"", length);
    }

    return String.format(" length=\"%d\" scale=\"%d\"", length, scale);
  }

  private void outputRelation(Relation relation) {
    out.printf("      <relation src=\"%s\" table=\"%s\" column=\"%s\" type=\"%s\"/>\n",
               relation.getFromColumnName(),
               relation.getToTableName(),
               relation.getToColumnName(),
               relation.getType().toString().toLowerCase());
  }

  private void outputPrimaryKey(Key key) {
    out.printf("      <primary>\n");
    outputKeyColumns(key);
    out.printf("      </primary>\n");
  }

  private void outputUniqueKeys(List<Key> keys) {
    var uniqueKeys = keys.stream().filter(key -> key.getType() == KeyType.UNIQUE).toList();

    if (!uniqueKeys.isEmpty()) {
      out.printf("      <unique>\n");
      uniqueKeys.forEach(this::outputKeyColumns);
      out.printf("      </unique>\n");
    }
  }

  private void outputIndexKeys(List<Key> keys) {
    var indexKeys = keys.stream().filter(key -> key.getType() == KeyType.INDEX).toList();

    if (!indexKeys.isEmpty()) {
      out.printf("      <index>\n");
      indexKeys.forEach(this::outputKeyColumns);
      out.printf("      </index>\n");
    }
  }

  private void outputKeyColumns(Key key) {
    key.getColumns().forEach(columnName -> {
      out.printf("        <column name=\"%s\"/>\n", columnName);
    });
  }
}

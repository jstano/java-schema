package com.stano.schema.model;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table {
  private final Schema schema;
  private final String schemaName;
  private final String name;
  private final String exportDateColumn;
  private final LockEscalation lockEscalation;
  private final boolean noExport;

  private final List<Column> columns = new ArrayList<>();
  private final List<Key> keys = new ArrayList<>();
  private final List<Key> indexes = new ArrayList<>();
  private final List<Relation> relations = new ArrayList<>();
  private final List<Relation> reverseRelations = new ArrayList<>();
  private final List<Trigger> triggers = new ArrayList<>();
  private final List<Constraint> constraints = new ArrayList<>();
  private final List<InitialData> initialData = new ArrayList<>();
  private final List<TableOption> options = new ArrayList<>();
  private final List<Aggregation> aggregations = new ArrayList<>();
  private final Map<String, Column> columnMap = new CaseInsensitiveMap<>();

  public Table(Schema schema, String schemaName, String name, String exportDateColumn, LockEscalation lockEscalation, boolean noExport) {
    this.schema = schema;
    this.schemaName = schemaName;
    this.name = name;
    this.exportDateColumn = exportDateColumn;
    this.lockEscalation = lockEscalation;
    this.noExport = noExport;
  }

  public Schema getSchema() {
    return schema;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public String getName() {
    return name;
  }

  public LockEscalation getLockEscalation() {
    return lockEscalation;
  }

  public boolean isNoExport() {
    return noExport;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public List<Key> getKeys() {
    return keys;
  }

  public List<Key> getIndexes() {
    return indexes;
  }

  public List<Relation> getRelations() {
    return relations;
  }

  public List<Relation> getReverseRelations() {
    return reverseRelations;
  }

  public List<Trigger> getTriggers() {
    return triggers;
  }

  public List<Constraint> getConstraints() {
    return constraints;
  }

  public List<InitialData> getInitialData() {
    return initialData;
  }

  public List<TableOption> getOptions() {
    return options;
  }

  public List<Aggregation> getAggregations() {
    return aggregations;
  }

  public Column getColumn(String columnName) {
    if (columnMap.isEmpty()) {
      for (Column column : columns) {
        columnMap.put(column.getName(), column);
      }
    }

    return columnMap.get(columnName);
  }

  public Key getPrimaryKey() {
    for (Key key : keys) {
      if (key.getType() == KeyType.PRIMARY) {
        return key;
      }
    }

    return null;
  }

  public boolean hasColumn(String columnName) {
    for (Column column : columns) {
      if (column.getName().equalsIgnoreCase(columnName)) {
        return true;
      }
    }

    return false;
  }

  public Column getIdentityColumn() {
    for (Column column : columns) {
      if (column.getType() == ColumnType.SEQUENCE || column.getType() == ColumnType.LONGSEQUENCE) {
        return column;
      }
    }

    return null;
  }

  public List<String> getPrimaryKeyColumns() {
    for (Key key : keys) {
      if (key.getType() == KeyType.PRIMARY) {
        return key.getColumns().stream().map(KeyColumn::getName).toList();
      }
    }

    return null;
  }

  public boolean hasOption(TableOption option) {
    for (TableOption tableOption : options) {
      if (tableOption == option) {
        return true;
      }
    }

    return false;
  }

  public boolean hasColumnConstraints(BooleanMode booleanMode) {
    for (Column column : columns) {
      if (column.needsCheckConstraints(booleanMode)) {
        return true;
      }
    }

    return false;
  }

  public List<Column> getColumnsWithCheckConstraints(BooleanMode booleanMode) {
    List<Column> cols = new ArrayList<Column>();

    for (Column column : columns) {
      if (column.needsCheckConstraints(booleanMode)) {
        cols.add(column);
      }
    }

    return cols;
  }

  public Relation getColumnRelation(Column column) {
    for (Relation relation : relations) {
      if (relation.getFromColumnName().equalsIgnoreCase(column.getName())) {
        return relation;
      }
    }

    return null;
  }

  public String getExportDateColumn() {
    return exportDateColumn;
  }

  @Override
  public String toString() {
    return name;
  }
}

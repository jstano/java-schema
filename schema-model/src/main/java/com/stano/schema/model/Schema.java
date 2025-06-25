package com.stano.schema.model;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Schema {
  private final URL schemaURL;
  private final List<Table> tables = new ArrayList<>();
  private final List<View> views = new ArrayList<>();
  private final List<Function> functions = new ArrayList<>();
  private final List<Procedure> procedures = new ArrayList<>();
  private final List<OtherSql> otherSql = new ArrayList<>();
  private final Map<String, Table> tableMap = new CaseInsensitiveMap<>();
  private final Map<String, EnumType> enumTypes = new HashMap<>();
  private Version version;
  private ForeignKeyMode foreignKeyMode;
  private BooleanMode booleanMode = BooleanMode.NATIVE;

  public Schema(URL schemaURL) {
    this.schemaURL = schemaURL;
  }

  public URL getSchemaURL() {
    return schemaURL;
  }

  public Version getVersion() {
    return version;
  }

  public void setVersion(Version version) {
    this.version = version;
  }

  public ForeignKeyMode getForeignKeyMode() {
    return foreignKeyMode;
  }

  public void setForeignKeyMode(ForeignKeyMode foreignKeyMode) {
    this.foreignKeyMode = foreignKeyMode;
  }

  public BooleanMode getBooleanMode() {
    return booleanMode;
  }

  public void setBooleanMode(BooleanMode booleanMode) {
    this.booleanMode = booleanMode;
  }

  public List<Table> getTables() {
    return Collections.unmodifiableList(tables);
  }

  public Table getTable(String name) {
    Table table = tableMap.get(name);

    if (table != null) {
      return table;
    }

    throw new IllegalStateException(String.format("Unable to locate a table with the name '%s'", name));
  }

  public Optional<Table> getOptionalTable(String name) {
    return Optional.ofNullable(tableMap.get(name));
  }

  public List<View> getViews(DatabaseType databaseType) {
    Map<String, View> viewMap = new HashMap<>();

    views.stream()
         .filter(view -> view.getDatabaseType() == databaseType)
         .forEach(view -> {
           viewMap.put(view.getName().toLowerCase(), view);
         });

    views.stream()
         .filter(view -> view.getDatabaseType() == null)
         .forEach(view -> {
           String viewName = view.getName().toLowerCase();

           if (!viewMap.containsKey(viewName)) {
             viewMap.put(viewName, view);
           }
         });

    return Collections.unmodifiableList(views.stream()
                                             .map(view -> view.getName().toLowerCase())
                                             .distinct()
                                             .map(viewMap::get)
                                             .filter(Objects::nonNull)
                                             .collect(Collectors.toList()));
  }

  public Collection<EnumType> getEnumTypes() {
    return enumTypes.values();
  }

  public EnumType getEnumType(String typeName) {
    EnumType enumType = enumTypes.get(typeName);

    if (enumType != null) {
      return enumType;
    }

    throw new IllegalStateException(String.format("Unable to locate an enum type with name '%s'", typeName));
  }

  public void addTable(Table table) {
    tables.add(table);
    tableMap.put(table.getName(), table);
  }

  public void addView(View view) {
    views.add(view);
  }

  public void addEnumType(EnumType enumType) {
    enumTypes.put(enumType.getName(), enumType);
  }

  public void addFunctions(List<Function> functions) {
    this.functions.addAll(functions);
  }

  public List<Function> getFunctions() {
    return Collections.unmodifiableList(new ArrayList<>(functions));
  }

  public void addProcedures(List<Procedure> procedures) {
    this.procedures.addAll(procedures);
  }

  public List<Procedure> getProcedures() {
    return Collections.unmodifiableList(new ArrayList<>(procedures));
  }

  public void addOtherSql(OtherSql otherSql) {
    this.otherSql.add(otherSql);
  }

  public List<OtherSql> getOtherSql() {
    return Collections.unmodifiableList(new ArrayList<>(otherSql));
  }

  public List<String> validate() {
    List<String> errors = new ArrayList<String>();

    for (Table table : tables) {
      for (Relation relation : table.getRelations()) {
        if (relation.getType() == RelationType.SETNULL) {
          String fromTableName = relation.getFromTableName();
          String fromColumnName = relation.getFromColumnName();

          Table fromTable = getTable(fromTableName);

          if (fromTable.getColumn(fromColumnName).isRequired()) {
            errors.add(String.format("ERROR: %s.%s is required. The %s.%s relation specifies setnull, which is not allowed",
                                     fromTableName,
                                     fromColumnName,
                                     relation.getToTableName(),
                                     relation.getToColumnName()));
          }
        }
      }
    }

    return errors;
  }

  public void sortTablesByName() {
    tables.sort(Comparator.comparing(Table::getName));
  }

  public void buildReverseRelations() {
    for (Table table : tables) {
      if (!table.getRelations().isEmpty()) {
        for (Relation relation : table.getRelations()) {
          String parentTableName = relation.getToTableName();
          Table parentTable = getTable(parentTableName);

          Relation reverseRelation = new Relation(relation.getToTableName(),
                                                  relation.getToColumnName(),
                                                  relation.getFromTableName(),
                                                  relation.getFromColumnName(),
                                                  relation.getType(),
                                                  false);

          parentTable.getReverseRelations().add(reverseRelation);
        }
      }
    }
  }
}

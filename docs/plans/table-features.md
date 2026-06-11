# Plan: Add `features="timestamps version"` to table model and generator

## Context
Tables in the XML schema need a `features` attribute that auto-generates common audit/optimistic-locking columns. This avoids repetitive column declarations across every table that needs them. The `timestamps` feature adds `created_at`/`updated_at` (type `timestamptz`), and the `version` feature adds a `version` column (type `int`). A new `TIMESTAMPTZ` column type is also required to support this (timestamp with time zone).

The existing in-progress work already added `TIMESTAMPTZ` support across all generators; this plan builds on top of that.

## Changes

### 1. Update XSD
**File:** `schema-model/src/main/resources/schema.xsd`

Three changes:
- Add `<enumeration value="timestamptz"/>` and `<enumeration value="timestamptz"/>` to the `columnTypeEnum` (after `timestamp`)
- Define two new simple types:
  ```xml
  <simpleType name="tableFeatureEnum">
    <restriction base="string">
      <enumeration value="timestamps"/>
      <enumeration value="version"/>
    </restriction>
  </simpleType>
  <simpleType name="tableFeatureList">
    <list itemType="database:tableFeatureEnum"/>
  </simpleType>
  ```
- Add `<attribute name="features" type="database:tableFeatureList"/>` to `tableType` (alongside the other table attributes)

### 2. New: `TableFeature` enum
**File:** `schema-model/src/main/java/com/stano/schema/model/TableFeature.java`

```java
public enum TableFeature { TIMESTAMPS, VERSION }
```

### 3. Add `TIMESTAMPTZ` to `ColumnType` enum
**File:** `schema-model/src/main/java/com/stano/schema/model/ColumnType.java`

Add `TIMESTAMPTZ` after `TIMESTAMPTZ`.

### 4. Update `Table` model
**File:** `schema-model/src/main/java/com/stano/schema/model/Table.java`

Add `Set<TableFeature> features = new LinkedHashSet<>()` (no constructor change — same pattern as `options`). Add:
- `getFeatures()` returning the set
- `hasFeature(TableFeature feature)` convenience method

### 5. Update `ColumnTypeGenerator` (base)
**File:** `schema-sql-generator/src/main/java/com/stano/schema/gensql/impl/common/ColumnTypeGenerator.java`

- Add dispatch for `TIMESTAMPTZ` calling `getTimestampTZSql()`
- Default `getTimestampTZSql()` returns `"timestamp"`

### 6. Override `getTimestampTZSql()` in each DB generator
| Generator | SQL type |
|---|---|
| PGSQL | `timestamptz` |
| H2 | `timestamp with time zone` |
| MSSQL | `datetimeoffset` |
| MySQL | `datetime` (no native TZ support) |

### 7. Update diagram generators for `TIMESTAMPTZ`
- **Mermaid** (`MermaidERDiagramGenerator`): add `TIMESTAMPTZ` to the `case DATETIME, TIMESTAMP` branch
- **PlantUML** (`PlantUMLERDiagramGenerator`): same

### 8. Update `TableContentHandler` (parser)
**File:** `schema-parser/src/main/java/com/stano/schema/parser/xmlparser/TableContentHandler.java`

In `parseTable()`: parse `features` attribute (space-split, `TableFeature.valueOf(token.toUpperCase())`).

In `endElement()` for `"table"`, before `schema.addTable(table)`, inject synthetic columns:
```java
for (TableFeature feature : table.getFeatures()) {
  switch (feature) {
    case TIMESTAMPS -> {
      table.getColumns().add(new Column("created_at", ColumnType.TIMESTAMPTZ, 0, true));
      table.getColumns().add(new Column("updated_at", ColumnType.TIMESTAMPTZ, 0, true));
    }
    case VERSION -> table.getColumns().add(new Column("version", ColumnType.INT, 0, true));
  }
}
```
Columns are appended after explicitly declared columns.

### 9. Update test XML
**File:** `schema-parser/src/test/resources/schema-parser-test-schema.xml`

Add a `FeaturesTestTable` with `features="timestamps version"` and at least one regular column.

### 10. Update tests
- **`SchemaParserTest`**: verify `FeaturesTestTable` has the correct `features` set and that `created_at`, `updated_at`, `version` columns are appended with correct types and `required=true`.
- **`ColumnTypeTest`**: add `TIMESTAMPTZ` coverage.
- **`GenSQLTest`**: verify PGSQL output for a features table emits `timestamptz` and `integer` columns.

## Verification
```bash
./gradlew test
```

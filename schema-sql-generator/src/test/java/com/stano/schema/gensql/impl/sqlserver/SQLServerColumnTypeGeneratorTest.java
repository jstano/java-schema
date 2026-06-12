package com.stano.schema.gensql.impl.sqlserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stano.schema.gensql.impl.common.OutputMode;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.SQLGeneratorOptions;
import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.Column;
import com.stano.schema.model.ColumnType;
import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.EnumType;
import com.stano.schema.model.EnumValue;
import com.stano.schema.model.ForeignKeyMode;
import com.stano.schema.model.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("SQL Server column type SQL generation")
class SQLServerColumnTypeGeneratorTest {

  private SQLServerColumnTypeGenerator generator;

  @BeforeEach
  void setUp() {
    generator = createGenerator(BooleanMode.NATIVE, null);
  }

  @ParameterizedTest(name = "{0} -> {1}")
  @DisplayName("simple types should map to expected SQL")
  @CsvSource({
    "SEQUENCE,    'integer identity(1,1)'",
    "LONGSEQUENCE, 'bigint identity(1,1)'",
    "BYTE,        tinyint",
    "SHORT,       smallint",
    "INT,         integer",
    "LONG,        bigint",
    "FLOAT,       real",
    "DOUBLE,      double precision",
    "DATE,        datetime",
    "DATETIME,    datetime",
    "TIMESTAMP,   datetime",
    "TIME,        datetime",
    "TIMESTAMPTZ, datetimeoffset",
    "UUID,        uniqueidentifier",
    "BINARY,      varbinary(max)",
    "JSON,        nvarchar(max)",
    "CITEXT,      nvarchar(max)",
    "CSTEXT,      nvarchar(max)",
    "TEXT,        nvarchar(max)",
  })
  void simpleTypeShouldMapToExpectedSql(String typeName, String expectedSql) {
    Column col = new Column("col", ColumnType.getColumnType(typeName.trim()), 0, false);
    assertEquals(expectedSql.trim(), generator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("CHAR with length should produce char(n)")
  void charShouldProduceCharWithLength() {
    Column col = new Column("col", ColumnType.CHAR, 10, false);
    assertEquals("char(10)", generator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("VARCHAR with length should produce nvarchar(n)")
  void varcharWithLengthShouldProduceNvarcharWithLength() {
    Column col = new Column("col", ColumnType.VARCHAR, 255, false);
    assertEquals("nvarchar(255)", generator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("VARCHAR with length -1 should produce nvarchar(max)")
  void varcharWithNegativeLengthShouldProduceNvarcharMax() {
    Column col = new Column("col", ColumnType.VARCHAR, -1, false);
    assertEquals("nvarchar(max)", generator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("DECIMAL with precision and scale should produce decimal(p,s)")
  void decimalWithPrecisionAndScaleShouldProduceDecimalPrecisionScale() {
    Column col =
        new Column(
            "col", ColumnType.DECIMAL, 19, 4, false, null, null, null, null, null, null, null);
    assertEquals("decimal(19,4)", generator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("DECIMAL with no precision or scale should produce decimal")
  void decimalWithNoPrecisionOrScaleShouldProduceBareDecimal() {
    Column col =
        new Column(
            "col", ColumnType.DECIMAL, 0, 0, false, null, null, null, null, null, null, null);
    assertEquals("decimal", generator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("BOOLEAN with NATIVE mode should produce bit")
  void booleanNativeShouldProduceBit() {
    Column col = new Column("col", ColumnType.BOOLEAN, 0, false);
    assertEquals("bit", generator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("BOOLEAN with YES_NO mode should produce nvarchar(3)")
  void booleanYesNoShouldProduceNvarchar3() {
    SQLServerColumnTypeGenerator yesNoGenerator = createGenerator(BooleanMode.YES_NO, null);
    Column col = new Column("col", ColumnType.BOOLEAN, 0, false);
    assertEquals("nvarchar(3)", yesNoGenerator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("BOOLEAN with YN mode should produce nchar(1)")
  void booleanYnShouldProduceNchar1() {
    SQLServerColumnTypeGenerator ynGenerator = createGenerator(BooleanMode.YN, null);
    Column col = new Column("col", ColumnType.BOOLEAN, 0, false);
    assertEquals("nchar(1)", ynGenerator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("ENUM with variable-length values should produce nvarchar(maxLength)")
  void enumWithVariableLengthValuesShouldProduceNvarcharMax() {
    Schema schema = new Schema(null);
    EnumType enumType = new EnumType("Status");
    enumType.addValue(new EnumValue("Active", "A"));
    enumType.addValue(new EnumValue("Inactive", "INACTIVE"));
    schema.addEnumType(enumType);
    SQLServerColumnTypeGenerator enumGenerator = createGenerator(BooleanMode.NATIVE, schema);
    Column col =
        new Column(
            "col", ColumnType.ENUM, 0, 0, false, null, null, null, null, null, "Status", null);
    assertEquals("nvarchar(8)", enumGenerator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("ENUM with fixed-length values should produce nchar(length)")
  void enumWithFixedLengthValuesShouldProduceNchar() {
    Schema schema = new Schema(null);
    EnumType enumType = new EnumType("Flag");
    enumType.addValue(new EnumValue("Yes", "YES"));
    enumType.addValue(new EnumValue("No", "NOO"));
    schema.addEnumType(enumType);
    SQLServerColumnTypeGenerator enumGenerator = createGenerator(BooleanMode.NATIVE, schema);
    Column col =
        new Column("col", ColumnType.ENUM, 0, 0, false, null, null, null, null, null, "Flag", null);
    assertEquals("nchar(3)", enumGenerator.getColumnTypeSql(null, col));
  }

  @Test
  @DisplayName("ARRAY should throw UnsupportedOperationException")
  void arrayShouldThrowUnsupportedOperationException() {
    Column col =
        new Column(
            "col",
            ColumnType.ARRAY,
            0,
            0,
            false,
            null,
            null,
            null,
            null,
            null,
            null,
            ColumnType.INT);
    assertThrows(UnsupportedOperationException.class, () -> generator.getColumnTypeSql(null, col));
  }

  private SQLServerColumnTypeGenerator createGenerator(BooleanMode booleanMode, Schema schema) {
    SQLGenerator sqlGen =
        new SQLGenerator(
            new SQLGeneratorOptions(
                schema,
                null,
                DatabaseType.SQL_SERVER,
                ForeignKeyMode.RELATIONS,
                booleanMode,
                OutputMode.ALL)) {
          @Override
          protected void outputTables() {}

          @Override
          protected void outputRelations() {}

          @Override
          protected void outputIndexes() {}

          @Override
          protected void outputTriggers() {}

          @Override
          protected void outputFunctions() {}

          @Override
          protected void outputViews() {}

          @Override
          protected void outputProcedures() {}

          @Override
          protected void outputOtherSqlTop() {}

          @Override
          protected void outputOtherSqlBottom() {}
        };
    return new SQLServerColumnTypeGenerator(sqlGen);
  }
}

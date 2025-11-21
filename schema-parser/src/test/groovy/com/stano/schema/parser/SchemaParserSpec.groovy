package com.stano.schema.parser

import com.stano.schema.model.AggregationFrequency
import com.stano.schema.model.AggregationType
import com.stano.schema.model.ColumnType
import com.stano.schema.model.DatabaseType
import com.stano.schema.model.KeyType
import com.stano.schema.model.LockEscalation
import com.stano.schema.model.OtherSqlOrder
import com.stano.schema.model.RelationType
import com.stano.schema.model.Table
import com.stano.schema.model.TableOption
import com.stano.schema.model.TriggerType
import com.stano.schema.model.Version
import spock.lang.Specification

class SchemaParserSpec extends Specification {
  def "should be able to parse a valid schema file"() {
    def schemaParser = new SchemaParser()

    when:
    def schema = schemaParser.parseSchema(getClass().getClassLoader().getResource("schema-parser-test-schema.xml"))

    def parentTable = schema.getTable("ParentTable")
    def childTable = schema.getTable("ChildTable")
    def columnTesterTable = schema.getTable("ColumnTesterTable")
    def kbiTable = schema.getTable("KBI")
    def unitTable = schema.getTable("Unit")

    then:
    schema.version == new Version(1, 2)

    schema.tables.size() == 8

    verifyParentTable(parentTable)

    verifyChildTable(childTable)

    verifyColumnTesterTable(columnTesterTable)

    verifyKBITable(kbiTable)

    schema.views.size() == 4
    schema.views.get(0).schemaName == 'test'
    schema.views.get(0).name == 'TestView1'
    schema.views.get(0).sql == 'select * from ParentTable'
    !schema.views.get(0).databaseType

    schema.views.get(1).schemaName == 'public'
    schema.views.get(1).name == 'TestView1'
    schema.views.get(1).sql == 'select * from ParentTable'
    !schema.views.get(1).databaseType

    schema.views.get(2).schemaName == 'public'
    schema.views.get(2).name == 'TestView2'
    schema.views.get(2).sql == 'select * from pgsql'
    schema.views.get(2).databaseType == DatabaseType.POSTGRES

    schema.views.get(3).schemaName == 'public'
    schema.views.get(3).name == 'TestView2'
    schema.views.get(3).sql == 'select * from mssql'
    schema.views.get(3).databaseType == DatabaseType.SQL_SERVER

    assert unitTable.schemaName == 'test'

    schema.enumTypes.size() == 2
    schema.getEnumType("GenderType").name == 'GenderType'
    schema.getEnumType("GenderType").values.size() == 2
    schema.getEnumType("GenderType").values.get(0).name == 'MALE'
    schema.getEnumType("GenderType").values.get(0).code == 'M'
    schema.getEnumType("GenderType").values.get(1).name == 'FEMALE'
    schema.getEnumType("GenderType").values.get(1).code == 'F'
    schema.getEnumType("TestEnumType").name == 'TestEnumType'
    schema.getEnumType("TestEnumType").values.size() == 2
    schema.getEnumType("TestEnumType").values.get(0).name == 'ONE'
    schema.getEnumType("TestEnumType").values.get(0).code == '1'
    schema.getEnumType("TestEnumType").values.get(1).name == 'TWO'
    schema.getEnumType("TestEnumType").values.get(1).code == '2'

    schema.otherSql.size() == 8
    schema.otherSql.get(0).databaseType == DatabaseType.POSTGRES
    schema.otherSql.get(0).order == OtherSqlOrder.TOP
    schema.otherSql.get(0).sql == 'other top sql for pgsql 1'
    schema.otherSql.get(1).databaseType == DatabaseType.POSTGRES
    schema.otherSql.get(1).order == OtherSqlOrder.TOP
    schema.otherSql.get(1).sql == 'other top sql for pgsql 2'
    schema.otherSql.get(2).databaseType == DatabaseType.POSTGRES
    schema.otherSql.get(2).order == OtherSqlOrder.BOTTOM
    schema.otherSql.get(2).sql == 'other bottom sql for pgsql 1'
    schema.otherSql.get(3).databaseType == DatabaseType.POSTGRES
    schema.otherSql.get(3).order == OtherSqlOrder.BOTTOM
    schema.otherSql.get(3).sql == 'other bottom sql for pgsql 2'
    schema.otherSql.get(4).databaseType == DatabaseType.SQL_SERVER
    schema.otherSql.get(4).order == OtherSqlOrder.TOP
    schema.otherSql.get(4).sql == 'other top sql for mssql 1'
    schema.otherSql.get(5).databaseType == DatabaseType.SQL_SERVER
    schema.otherSql.get(5).order == OtherSqlOrder.TOP
    schema.otherSql.get(5).sql == 'other top sql for mssql 2'
    schema.otherSql.get(6).databaseType == DatabaseType.SQL_SERVER
    schema.otherSql.get(6).order == OtherSqlOrder.BOTTOM
    schema.otherSql.get(6).sql == 'other bottom sql for mssql 1'
    schema.otherSql.get(7).databaseType == DatabaseType.SQL_SERVER
    schema.otherSql.get(7).order == OtherSqlOrder.BOTTOM
    schema.otherSql.get(7).sql == 'other bottom sql for mssql 2'

    schema.functions.size() == 6
    schema.functions.get(0).schemaName == 'test'
    schema.functions.get(0).name == 'testCustomFunction1'
    schema.functions.get(0).sql == 'test custom function sql for pgsql 1'
    schema.functions.get(1).schemaName == 'test'
    schema.functions.get(1).name == 'testCustomFunction1'
    schema.functions.get(1).sql == 'test custom function sql for mssql 1'
    schema.functions.get(2).schemaName == 'public'
    schema.functions.get(2).name == 'customFunction1'
    schema.functions.get(2).sql == 'custom function sql for pgsql 1'
    schema.functions.get(3).schemaName == 'public'
    schema.functions.get(3).name == 'customFunction1'
    schema.functions.get(3).sql == 'custom function sql for mssql 1'
    schema.functions.get(4).schemaName == 'public'
    schema.functions.get(4).name == 'customFunction2'
    schema.functions.get(4).sql == 'custom function sql for pgsql 2'
    schema.functions.get(5).schemaName == 'public'
    schema.functions.get(5).name == 'customFunction2'
    schema.functions.get(5).sql == 'custom function sql for mssql 2'

    schema.procedures.size() == 6
    schema.procedures.get(0).schemaName == 'test'
    schema.procedures.get(0).name == 'testCustomProcedure1'
    schema.procedures.get(0).sql == 'test custom procedure sql for pgsql 1'
    schema.procedures.get(1).schemaName == 'test'
    schema.procedures.get(1).name == 'testCustomProcedure1'
    schema.procedures.get(1).sql == 'test custom procedure sql for mssql 1'
    schema.procedures.get(2).schemaName == 'public'
    schema.procedures.get(2).name == 'customProcedure1'
    schema.procedures.get(2).sql == 'custom procedure sql for pgsql 1'
    schema.procedures.get(3).schemaName == 'public'
    schema.procedures.get(3).name == 'customProcedure1'
    schema.procedures.get(3).sql == 'custom procedure sql for mssql 1'
    schema.procedures.get(4).schemaName == 'public'
    schema.procedures.get(4).name == 'customProcedure2'
    schema.procedures.get(4).sql == 'custom procedure sql for pgsql 2'
    schema.procedures.get(5).schemaName == 'public'
    schema.procedures.get(5).name == 'customProcedure2'
    schema.procedures.get(5).sql == 'custom procedure sql for mssql 2'
  }

  def "should get a RuntimeIOException if an IOException occurs"() {
    def schemaParser = new SchemaParser()

    when:
    schemaParser.parseSchema(new URI("file:bad-url").toURL())

    then:
    thrown SchemaParserException
  }

  private void verifyParentTable(Table parentTable) {
    assert parentTable.columns.size() == 4
    assert parentTable.columns.get(0).name == 'ID'
    assert parentTable.columns.get(0).type == ColumnType.SEQUENCE
    assert parentTable.columns.get(0).required
    assert parentTable.columns.get(1).name == 'Name'
    assert parentTable.columns.get(1).type == ColumnType.VARCHAR
    assert parentTable.columns.get(1).length == 100
    assert parentTable.columns.get(1).required
    assert parentTable.columns.get(2).name == 'Extra'
    assert parentTable.columns.get(2).type == ColumnType.VARCHAR
    assert parentTable.columns.get(2).length == 200
    assert !parentTable.columns.get(2).required
    assert parentTable.columns.get(3).name == 'Gender'
    assert parentTable.columns.get(3).type == ColumnType.ENUM
    assert parentTable.columns.get(3).enumType == 'GenderType'
    assert !parentTable.columns.get(3).required

    assert parentTable.keys.size() == 2
    assert parentTable.keys.get(0).type == KeyType.PRIMARY
    assert !parentTable.keys.get(0).cluster
    assert !parentTable.keys.get(0).compress
    assert parentTable.keys.get(0).columns.size() == 1
    assert parentTable.keys.get(0).columns.get(0).name == 'ID'
    assert parentTable.keys.get(1).type == KeyType.UNIQUE
    assert parentTable.keys.get(1).cluster
    assert !parentTable.keys.get(1).compress
    assert parentTable.keys.get(1).columns.size() == 2
    assert parentTable.keys.get(1).columns.get(0).name == 'Name'
    assert parentTable.keys.get(1).columns.get(1).name == 'Extra'
    assert parentTable.indexes.size() == 2
    assert parentTable.indexes.get(0).type == KeyType.INDEX
    assert !parentTable.indexes.get(0).cluster
    assert parentTable.indexes.get(0).compress
    assert parentTable.indexes.get(0).columns.size() == 2
    assert parentTable.indexes.get(0).columns.get(0).name == 'Extra'
    assert parentTable.indexes.get(0).columns.get(1).name == 'Name'
    assert parentTable.indexes.get(1).type == KeyType.INDEX
    assert parentTable.indexes.get(1).columns.size() == 3
    assert parentTable.indexes.get(1).columns.get(0).name == 'ID'
    assert parentTable.indexes.get(1).columns.get(1).name == 'Name'
    assert parentTable.indexes.get(1).columns.get(2).name == 'Extra'

    assert parentTable.relations.empty

    assert parentTable.initialData.size() == 4
    assert parentTable.initialData.get(0).sql == "insert into ParentTable (Name,Extra,Gender) values ('AAA','Extra AAA','M')"
    assert parentTable.initialData.get(1).sql == "insert into ParentTable (Name,Extra,Gender) values ('BBB','Extra BBB','F')"
    assert parentTable.initialData.get(2).sql == "insert into ParentTable (Name,Extra,Gender) values ('PGSQL','Extra PGSQL','M')"
    assert parentTable.initialData.get(3).sql == "insert into ParentTable (Name,Extra,Gender) values ('MSSQL','Extra MSSQL','F')"

    assert parentTable.triggers.size() == 4
    assert parentTable.triggers.get(0).databaseType == DatabaseType.POSTGRES
    assert parentTable.triggers.get(0).triggerType == TriggerType.DELETE
    assert parentTable.triggers.get(0).triggerText == 'delete from pgsql'
    assert parentTable.triggers.get(1).databaseType == DatabaseType.SQL_SERVER
    assert parentTable.triggers.get(1).triggerType == TriggerType.DELETE
    assert parentTable.triggers.get(1).triggerText == 'delete from mssql'
    assert parentTable.triggers.get(2).databaseType == DatabaseType.POSTGRES
    assert parentTable.triggers.get(2).triggerType == TriggerType.UPDATE
    assert parentTable.triggers.get(2).triggerText == 'update pgsql'
    assert parentTable.triggers.get(3).databaseType == DatabaseType.SQL_SERVER
    assert parentTable.triggers.get(3).triggerType == TriggerType.UPDATE
    assert parentTable.triggers.get(3).triggerText == 'update mssql'

    assert parentTable.aggregations.size() == 2
    assert parentTable.aggregations.get(0).destinationTable == 'ParentTableAggregation'
    assert parentTable.aggregations.get(0).dateColumn == 'Extra'
    assert parentTable.aggregations.get(0).criteria == 'criteria'
    assert parentTable.aggregations.get(0).timeStampColumn == 'timestamp'
    assert parentTable.aggregations.get(0).aggregationFrequency == AggregationFrequency.DAILY
    assert parentTable.aggregations.get(0).aggregationColumns.size() == 2
    assert parentTable.aggregations.get(0).aggregationColumns.get(0).aggregationType == AggregationType.COUNT
    assert parentTable.aggregations.get(0).aggregationColumns.get(0).sourceColumn == null
    assert parentTable.aggregations.get(0).aggregationColumns.get(0).destinationColumn == 'CountOfData'
    assert parentTable.aggregations.get(0).aggregationColumns.get(1).aggregationType == AggregationType.SUM
    assert parentTable.aggregations.get(0).aggregationColumns.get(1).sourceColumn == 'Name'
    assert parentTable.aggregations.get(0).aggregationColumns.get(1).destinationColumn == 'SumOfData'
    assert parentTable.aggregations.get(0).aggregationGroups.size() == 2
    assert parentTable.aggregations.get(0).aggregationGroups.get(0).source == 'Source1'
    assert parentTable.aggregations.get(0).aggregationGroups.get(0).destination == 'Destination1'
    assert parentTable.aggregations.get(0).aggregationGroups.get(0).sourceDerivedFrom == 'SourceDerivedFrom1'
    assert parentTable.aggregations.get(0).aggregationGroups.get(1).source == 'Source2'
    assert parentTable.aggregations.get(0).aggregationGroups.get(1).destination == 'Destination2'
    assert parentTable.aggregations.get(0).aggregationGroups.get(1).sourceDerivedFrom == 'SourceDerivedFrom2'

    assert parentTable.aggregations.get(1).destinationTable == 'ParentTableAggregation2'
    assert parentTable.aggregations.get(1).dateColumn == 'Extra'
    assert !parentTable.aggregations.get(1).criteria
    assert parentTable.aggregations.get(1).timeStampColumn == 'timestamp'
    assert parentTable.aggregations.get(1).aggregationFrequency == AggregationFrequency.DAILY
    assert parentTable.aggregations.get(1).aggregationColumns.size() == 2
    assert parentTable.aggregations.get(1).aggregationColumns.get(0).aggregationType == AggregationType.COUNT
    assert parentTable.aggregations.get(1).aggregationColumns.get(0).sourceColumn == null
    assert parentTable.aggregations.get(1).aggregationColumns.get(0).destinationColumn == 'CountOfData'
    assert parentTable.aggregations.get(1).aggregationColumns.get(1).aggregationType == AggregationType.SUM
    assert parentTable.aggregations.get(1).aggregationColumns.get(1).sourceColumn == 'Name'
    assert parentTable.aggregations.get(1).aggregationColumns.get(1).destinationColumn == 'SumOfData'
    assert parentTable.aggregations.get(1).aggregationGroups.size() == 2
    assert parentTable.aggregations.get(1).aggregationGroups.get(0).source == 'Source1'
    assert parentTable.aggregations.get(1).aggregationGroups.get(0).destination == 'Destination1'
    assert parentTable.aggregations.get(1).aggregationGroups.get(0).sourceDerivedFrom == 'SourceDerivedFrom1'
    assert parentTable.aggregations.get(1).aggregationGroups.get(1).source == 'Source2'
    assert parentTable.aggregations.get(1).aggregationGroups.get(1).destination == 'Destination2'
    assert parentTable.aggregations.get(1).aggregationGroups.get(1).sourceDerivedFrom == 'SourceDerivedFrom2'
  }

  private void verifyChildTable(Table childTable) {
    assert childTable.relations.size() == 1
    assert childTable.relations.get(0).fromTableName == 'ChildTable'
    assert childTable.relations.get(0).fromColumnName == 'ParentID'
    assert childTable.relations.get(0).toTableName == 'ParentTable'
    assert childTable.relations.get(0).toColumnName == 'ID'
    assert childTable.relations.get(0).type == RelationType.CASCADE
  }

  private void verifyColumnTesterTable(Table columnTesterTable) {
    assert columnTesterTable.name == 'ColumnTesterTable'
    assert columnTesterTable.options.size() == 3
    assert columnTesterTable.options.get(0) == TableOption.NO_EXPORT
    assert columnTesterTable.options.get(1) == TableOption.COMPRESS
    assert columnTesterTable.options.get(2) == TableOption.DATA
    assert columnTesterTable.columns.size() == 22
    assert columnTesterTable.columns.get(0).name == 'sequence'
    assert columnTesterTable.columns.get(0).type == ColumnType.SEQUENCE
    assert columnTesterTable.columns.get(1).name == 'longsequence'
    assert columnTesterTable.columns.get(1).type == ColumnType.LONGSEQUENCE
    assert columnTesterTable.columns.get(2).name == 'byte'
    assert columnTesterTable.columns.get(2).type == ColumnType.BYTE
    assert columnTesterTable.columns.get(3).name == 'short'
    assert columnTesterTable.columns.get(3).type == ColumnType.SHORT
    assert columnTesterTable.columns.get(4).name == 'int'
    assert columnTesterTable.columns.get(4).type == ColumnType.INT
    assert columnTesterTable.columns.get(4).minValue == '1'
    assert columnTesterTable.columns.get(4).maxValue == '500'
    assert columnTesterTable.columns.get(5).name == 'long'
    assert columnTesterTable.columns.get(5).type == ColumnType.LONG
    assert columnTesterTable.columns.get(6).name == 'float'
    assert columnTesterTable.columns.get(6).type == ColumnType.FLOAT
    assert columnTesterTable.columns.get(7).name == 'double'
    assert columnTesterTable.columns.get(7).type == ColumnType.DOUBLE
    assert columnTesterTable.columns.get(8).name == 'decimal'
    assert columnTesterTable.columns.get(8).type == ColumnType.DECIMAL
    assert columnTesterTable.columns.get(8).length == 19
    assert columnTesterTable.columns.get(8).scale == 4
    assert columnTesterTable.columns.get(9).name == 'boolean'
    assert columnTesterTable.columns.get(9).type == ColumnType.BOOLEAN
    assert columnTesterTable.columns.get(10).name == 'date'
    assert columnTesterTable.columns.get(10).type == ColumnType.DATE
    assert columnTesterTable.columns.get(11).name == 'datetime'
    assert columnTesterTable.columns.get(11).type == ColumnType.DATETIME
    assert columnTesterTable.columns.get(12).name == 'time'
    assert columnTesterTable.columns.get(12).type == ColumnType.TIME
    assert columnTesterTable.columns.get(13).name == 'timestamp'
    assert columnTesterTable.columns.get(13).type == ColumnType.TIMESTAMP
    assert columnTesterTable.columns.get(14).name == 'char'
    assert columnTesterTable.columns.get(14).type == ColumnType.CHAR
    assert columnTesterTable.columns.get(14).length == 1
    assert columnTesterTable.columns.get(14).defaultConstraint == "default 'A'"
    assert columnTesterTable.columns.get(15).name == 'varchar'
    assert columnTesterTable.columns.get(15).type == ColumnType.VARCHAR
    assert columnTesterTable.columns.get(15).length == 10
    assert columnTesterTable.columns.get(16).name == 'varcharWithCheck'
    assert columnTesterTable.columns.get(16).type == ColumnType.VARCHAR
    assert columnTesterTable.columns.get(16).length == 6
    assert columnTesterTable.columns.get(16).checkConstraint == "check(varcharWithCheck = 'ABC123')"
    assert columnTesterTable.columns.get(17).name == 'enum'
    assert columnTesterTable.columns.get(17).type == ColumnType.ENUM
    assert columnTesterTable.columns.get(17).enumType == 'TestEnumType'
    assert columnTesterTable.columns.get(18).name == 'text'
    assert columnTesterTable.columns.get(18).type == ColumnType.TEXT
    assert columnTesterTable.columns.get(19).name == 'binary'
    assert columnTesterTable.columns.get(19).type == ColumnType.BINARY
    assert columnTesterTable.columns.get(20).name == 'uuid'
    assert columnTesterTable.columns.get(20).type == ColumnType.UUID
    assert columnTesterTable.columns.get(21).name == 'json'
    assert columnTesterTable.columns.get(21).type == ColumnType.JSON
    assert columnTesterTable.options.size() == 3
    assert columnTesterTable.hasOption(TableOption.COMPRESS)
    assert columnTesterTable.hasOption(TableOption.DATA)
    assert columnTesterTable.hasOption(TableOption.NO_EXPORT)
    assert columnTesterTable.getLockEscalation() == LockEscalation.DISABLE
    assert columnTesterTable.getExportDateColumn() == 'byte'
  }

  private void verifyKBITable(Table kbiTable) {
    assert kbiTable.relations.size() == 3
    assert kbiTable.relations.get(0).fromTableName == 'KBI'
    assert kbiTable.relations.get(0).fromColumnName == 'PropertyID'
    assert kbiTable.relations.get(0).toTableName == 'Property'
    assert kbiTable.relations.get(0).toColumnName == 'ID'
    assert kbiTable.relations.get(0).type == RelationType.CASCADE

    assert kbiTable.relations.get(1).fromTableName == 'KBI'
    assert kbiTable.relations.get(1).fromColumnName == 'UnitID'
    assert kbiTable.relations.get(1).toTableName == 'Unit'
    assert kbiTable.relations.get(1).toColumnName == 'ID'
    assert kbiTable.relations.get(1).type == RelationType.SETNULL

    assert kbiTable.relations.get(2).fromTableName == 'KBI'
    assert kbiTable.relations.get(2).fromColumnName == 'MasterKBICodeID'
    assert kbiTable.relations.get(2).toTableName == 'MasterKBICode'
    assert kbiTable.relations.get(2).toColumnName == 'ID'
    assert kbiTable.relations.get(2).type == RelationType.SETNULL
  }
}

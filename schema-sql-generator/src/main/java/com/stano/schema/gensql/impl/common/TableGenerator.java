package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.InitialData;
import com.stano.schema.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public abstract class TableGenerator extends BaseGenerator {
  private static final Logger LOGGER = LoggerFactory.getLogger(TableGenerator.class);

  protected TableGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }

  public void outputTables() {
    schema.getTables().forEach(this::outputTable);
  }

  protected abstract ColumnGenerator getColumnGenerator();

  protected abstract KeyGenerator getKeyGenerator();

  protected abstract ColumnConstraintGenerator getColumnConstraintGenerator();

  protected abstract TableConstraintGenerator getTableConstraintGenerator();

  protected abstract IndexGenerator getIndexGenerator();

  protected void outputTable(Table table) {
    LOGGER.debug("Generating SQL for table " + getFullyQualifiedTableName(table));

    outputTableHeader(table);
    outputTableDefinition(table);
    outputTableFooter(table);
    outputIndexes(table);
    outputInitialData(table);
  }

  protected void outputTableHeader(Table table) {
    sqlWriter.println(String.format("/* %s */", getFullyQualifiedTableName(table)));
    sqlWriter.println("create table " + getFullyQualifiedTableName(table));
    sqlWriter.println("(");
  }

  protected void outputTableDefinition(Table table) {
    List<String> tableDefinitions = Stream.of(getColumnGenerator().getColumnDefinitions(table),
                                              getKeyGenerator().getKeyConstraints(table),
                                              getColumnConstraintGenerator().getColumnCheckConstraints(table),
                                              getTableConstraintGenerator().getTableCheckConstraints(table))
                                          .flatMap(Collection::stream)
                                          .toList();

    for (int i = 0; i < tableDefinitions.size(); i++) {
      String sql = tableDefinitions.get(i);

      sqlWriter.print(sql);

      if (i < tableDefinitions.size() - 1) {
        sqlWriter.print(",");
      }

      sqlWriter.println();
    }
  }

  protected void outputTableFooter(Table table) {
    sqlWriter.println(")" + statementSeparator);
    sqlWriter.println();
  }

  protected void outputIndexes(Table table) {
    getIndexGenerator().outputIndexes(table);
  }

  protected void outputInitialData(Table table) {
    List<InitialData> initialDataList = table.getInitialData()
                                             .stream()
                                             .filter(it -> it.getDatabaseType() == null || it.getDatabaseType() == databaseType)
                                             .toList();

    if (!initialDataList.isEmpty()) {
      initialDataList.forEach(initialData -> {
        sqlWriter.println(initialData.getSql() + statementSeparator);
      });

      sqlWriter.println();
    }
  }
}

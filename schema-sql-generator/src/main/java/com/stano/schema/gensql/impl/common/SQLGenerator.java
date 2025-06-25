package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.ForeignKeyMode;
import com.stano.schema.model.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.List;

public abstract class SQLGenerator {
  private static final Logger logger = LoggerFactory.getLogger(SQLGenerator.class);

  private final SQLGeneratorOptions sqlGeneratorOptions;

  protected final Schema schema;
  protected final PrintWriter sqlWriter;
  protected final String statementSeparator;
  protected final DatabaseType databaseType;

  protected SQLGenerator(SQLGeneratorOptions sqlGeneratorOptions) {
    this.sqlGeneratorOptions = sqlGeneratorOptions;
    this.schema = sqlGeneratorOptions.getSchema();
    this.sqlWriter = sqlGeneratorOptions.getSqlWriter();
    this.statementSeparator = sqlGeneratorOptions.getStatementSeparator();
    this.databaseType = sqlGeneratorOptions.getDatabaseType();
  }

  public SQLGeneratorOptions getSqlGeneratorOptions() {
    return sqlGeneratorOptions;
  }

  public void generate() {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format("Generating SQL for '%s'", schema.getSchemaURL().toExternalForm()));
    }

    if (schemaIsValid()) {
      outputSQL();
    }
  }

  private boolean schemaIsValid() {
    List<String> errors = schema.validate();

    if (!errors.isEmpty()) {
      if (logger.isErrorEnabled()) {
        for (String error : errors) {
          logger.error(error);
        }
      }

      return false;
    }

    return true;
  }

  private void outputSQL() {
    String currentLineSeparator = System.setProperty("line.separator", "\n");

    try {
      try {
        outputHeader();

        if (sqlGeneratorOptions.getOutputMode() == OutputMode.INDEXES_ONLY) {
          outputIndexes();
        }
        else if (sqlGeneratorOptions.getOutputMode() == OutputMode.TRIGGERS_ONLY) {
          outputTriggers();
        }
        else {
          outputOtherSqlTop();
          outputTables();

          if (sqlGeneratorOptions.getForeignKeyMode() == ForeignKeyMode.RELATIONS) {
            outputRelations();
          }

          outputTriggers();
          outputFunctions();
          outputViews();
          outputProcedures();
          outputOtherSqlBottom();
        }
      }
      finally {
        sqlWriter.close();
      }
    }
    finally {
      System.setProperty("line.separator", currentLineSeparator);
    }
  }

  protected void outputHeader() {
  }

  protected abstract void outputTables();

  protected abstract void outputRelations();

  protected abstract void outputIndexes();

  protected abstract void outputTriggers();

  protected abstract void outputFunctions();

  protected abstract void outputViews();

  protected abstract void outputProcedures();

  protected abstract void outputOtherSqlTop();

  protected abstract void outputOtherSqlBottom();
}

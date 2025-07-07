package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.BooleanMode;
import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.ForeignKeyMode;
import com.stano.schema.model.Schema;

import java.io.PrintWriter;

public class SQLGeneratorOptions {

   private final Schema schema;
   private final PrintWriter sqlWriter;
   private final DatabaseType databaseType;
   private final ForeignKeyMode foreignKeyMode;
   private final BooleanMode booleanMode;
   private final OutputMode outputMode;
   private final String statementSeparator;

   public SQLGeneratorOptions(Schema schema,
                              PrintWriter sqlWriter,
                              DatabaseType databaseType,
                              ForeignKeyMode foreignKeyMode,
                              BooleanMode booleanMode,
                              OutputMode outputMode) {

      this(schema, sqlWriter, databaseType, foreignKeyMode, booleanMode, outputMode, databaseType.getStatementSeparator());
   }

   public SQLGeneratorOptions(Schema schema,
                              PrintWriter sqlWriter,
                              DatabaseType databaseType,
                              ForeignKeyMode foreignKeyMode,
                              BooleanMode booleanMode,
                              OutputMode outputMode,
                              String statementSeparator) {

      this.schema = schema;
      this.sqlWriter = sqlWriter;
      this.databaseType = databaseType;
      this.foreignKeyMode = foreignKeyMode;
      this.booleanMode = booleanMode;
      this.outputMode = outputMode;
      this.statementSeparator = statementSeparator;
   }

   public Schema getSchema() {

      return schema;
   }

   public PrintWriter getSqlWriter() {

      return sqlWriter;
   }

   public DatabaseType getDatabaseType() {

      return databaseType;
   }

   public ForeignKeyMode getForeignKeyMode() {

      if (foreignKeyMode == ForeignKeyMode.TRIGGERS && !databaseType.isSupportsTriggers()) {
         return ForeignKeyMode.RELATIONS;
      }

      return foreignKeyMode;
   }

   public BooleanMode getBooleanMode() {

      return booleanMode;
   }

   public OutputMode getOutputMode() {

      return outputMode;
   }

   public String getStatementSeparator() {

      return statementSeparator;
   }
}

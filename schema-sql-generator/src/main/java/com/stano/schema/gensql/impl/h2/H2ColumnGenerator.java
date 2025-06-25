package com.stano.schema.gensql.impl.h2;

import com.stano.schema.gensql.impl.common.ColumnGenerator;
import com.stano.schema.gensql.impl.common.ColumnTypeGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Column;
import com.stano.schema.model.Table;

class H2ColumnGenerator extends ColumnGenerator {

   private final ColumnTypeGenerator columnTypeGenerator;

   H2ColumnGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);

      this.columnTypeGenerator = new H2ColumnTypeGenerator(sqlGenerator);
   }

   @Override
   protected ColumnTypeGenerator getColumnTypeGenerator() {

      return columnTypeGenerator;
   }

   @Override
   protected String getColumnOptions(Table table, Column column) {

      StringBuilder columnOptions = new StringBuilder();

      String defaultValue = getDefaultValue(table, column);

      if (defaultValue != null) {
         if (columnOptions.length() > 0) {
            columnOptions.append(' ');
         }

         columnOptions.append(createDefaultConstraint(table, column, defaultValue));
      }

      if (column.isRequired()) {
         if (columnOptions.length() > 0) {
            columnOptions.append(' ');
         }

         columnOptions.append("not null");
      }

      return columnOptions.toString().trim();
   }

   @Override
   protected String createDefaultConstraint(Table table, Column column, String defaultValue) {

      return String.format("default %s", defaultValue);
   }
}

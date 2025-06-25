package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.InitialData;
import com.stano.schema.model.Table;

import java.util.List;
import java.util.stream.Collectors;

public class InitialDataGenerator extends BaseGenerator {

   public InitialDataGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   public void outputInitialData() {

      for (Table table : schema.getTables()) {
         List<InitialData> initialDataList = table.getInitialData()
                                                  .stream()
                                                  .filter(it -> it.getDatabaseType() == null || it.getDatabaseType() == databaseType)
                                                  .collect(Collectors.toList());

         if (!initialDataList.isEmpty()) {
            initialDataList.forEach(initialData -> {
               sqlWriter.println(initialData.getSql() + statementSeparator);
            });

            sqlWriter.println();
         }
      }
   }
}

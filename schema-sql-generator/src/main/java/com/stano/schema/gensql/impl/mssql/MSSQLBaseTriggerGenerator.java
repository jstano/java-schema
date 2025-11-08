package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.BaseGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Aggregation;
import com.stano.schema.model.AggregationColumn;
import com.stano.schema.model.AggregationGroup;
import com.stano.schema.model.DatabaseType;
import com.stano.schema.model.Table;
import com.stano.schema.model.Trigger;
import com.stano.schema.model.TriggerType;

class MSSQLBaseTriggerGenerator extends BaseGenerator {

   MSSQLBaseTriggerGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected String getFullyQualifiedTableName(Table table) {

      String schemaName = table.getSchemaName().equalsIgnoreCase("public") ? "dbo" : table.getSchemaName();
      if (table.getName().equalsIgnoreCase("function")) {
         return "[" + schemaName + "]" + "." + "[" + table.getName() +"]";
      }
      else {
         return schemaName + "." + table.getName();
      }
   }

   void outputAdditionalTriggerStatements(Table table, TriggerType triggerType) {

      if (!table.getTriggers().isEmpty()) {
         for (Trigger trigger : table.getTriggers()) {
            if (trigger.getDatabaseType() == DatabaseType.SQL_SERVER && trigger.getTriggerType() == triggerType) {
               String triggerText = trigger.getTriggerText();

               if (!triggerText.startsWith("   ")) {
                  sqlWriter.print("   ");
               }

               sqlWriter.println(triggerText);
            }
         }
      }
   }


   protected String createSourceCTE(Aggregation aggregation, String sourceColumn, String alias) {

      StringBuilder s = new StringBuilder();

      s.append(alias).append(" as (select ");

      for (AggregationColumn column : aggregation.getAggregationColumns()) {
         s.append(column.getSourceColumn() != null && !column.getSourceColumn().trim().isEmpty() ? column.getSourceColumn() : "1")
          .append(" as ")
          .append(column.getDestinationColumn())
          .append(", ");
      }

      for (AggregationGroup group : aggregation.getAggregationGroups()) {

         if (group.getSource().equals(group.getDestination())) {
            s.append(group.getSource()).append(", ");
         }
         else {
            s.append(group.getSource()).append(" as ").append(group.getDestination()).append(", ");
         }
      }

      s.append(createAggregationFrequencyConversion(aggregation))
       .append(" as ")
       .append(aggregation.getDateColumn())
       .append(" from ")
       .append(sourceColumn);

      if (aggregation.getCriteria() != null && !aggregation.getCriteria().trim().isEmpty()) {
         s.append(" where ").append(aggregation.getCriteria());
      }

      return s.append(")").toString();
   }

   protected String createBothCTE(Aggregation aggregation) {

      StringBuilder s = new StringBuilder("BOTH as (select ");

      for (AggregationColumn column : aggregation.getAggregationColumns()) {
         s.append(column.getDestinationColumn()).append(" as Inserted").append(column.getDestinationColumn()).append(", ");
         s.append("0 as Deleted").append(column.getDestinationColumn()).append(", ");
      }

      for (AggregationGroup group : aggregation.getAggregationGroups()) {
         s.append(group.getDestination()).append(", ");
      }

      s.append(aggregation.getDateColumn()).append(" from I union select ");

      for (AggregationColumn column : aggregation.getAggregationColumns()) {
         s.append("0 as Inserted").append(column.getDestinationColumn()).append(", ");
         s.append(column.getDestinationColumn()).append(" as Deleted").append(column.getDestinationColumn()).append(", ");
      }

      for (AggregationGroup group : aggregation.getAggregationGroups()) {
         s.append(group.getDestination()).append(", ");
      }

      return s.append(aggregation.getDateColumn()).append(" from D)").toString();
   }

   protected String createAggregationFrequencyConversion(Aggregation aggregation) {

      StringBuilder s = new StringBuilder();

      switch (aggregation.getAggregationFrequency()) {

         case DAILY:
            return s.append("convert(varchar, ").append(aggregation.getDateColumn()).append(", 110)").toString();
         case WEEKLY:
            return s.append("convert(varchar, DATEADD(d,-DATEPART(dw, ")
                    .append(aggregation.getDateColumn())
                    .append(") + 1, ")
                    .append(aggregation.getDateColumn())
                    .append("), 110)")
                    .toString();
         case MONTHLY:
            return s.append("convert(varchar, DATEADD(d,-DATEPART(d, ")
                    .append(aggregation.getDateColumn())
                    .append(") + 1, ")
                    .append(aggregation.getDateColumn())
                    .append("), 110)")
                    .toString();
         case YEARLY:
            return s.append("convert(varchar, DATEADD(d,-DATEPART(dy, ")
                    .append(aggregation.getDateColumn())
                    .append(") + 1, ")
                    .append(aggregation.getDateColumn())
                    .append("), 110)")
                    .toString();
      }

      return null;
   }
}

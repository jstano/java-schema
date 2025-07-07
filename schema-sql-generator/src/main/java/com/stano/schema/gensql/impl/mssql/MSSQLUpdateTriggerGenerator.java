package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.model.Aggregation;
import com.stano.schema.model.AggregationColumn;
import com.stano.schema.model.AggregationGroup;
import com.stano.schema.model.AggregationType;
import com.stano.schema.model.Relation;
import com.stano.schema.model.RelationType;
import com.stano.schema.model.Table;
import com.stano.schema.model.TriggerType;

import java.util.List;

class MSSQLUpdateTriggerGenerator extends MSSQLBaseTriggerGenerator {

   MSSQLUpdateTriggerGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   void outputUpdateTrigger(Table table, List<Relation> relations) {

      String triggerName = table.getName().toLowerCase() + "_update";

      sqlWriter.println(String.format("/* %s */", triggerName));
      sqlWriter.println("if exists (select name from dbo.sysobjects where name = '" + triggerName + "' and type = 'TR')");
      sqlWriter.println("   drop trigger " + triggerName + statementSeparator);
      sqlWriter.println();

      sqlWriter.println("create trigger " + triggerName + " on " + getFullyQualifiedTableName(table) + " for insert,update as");
      sqlWriter.println("BEGIN");

      // output the enforce statements
      for (Relation relation : relations) {
         if (relation.getType() == RelationType.ENFORCE || relation.getType() == RelationType.CASCADE) {
            sqlWriter.println("   if update(" + relation.getFromColumnName() + ")");
            sqlWriter.println("   begin");
            sqlWriter.println("      if (select count(*) from Inserted where " + relation.getFromColumnName() + " is not null) > 0");
            sqlWriter.println("      begin");
            sqlWriter.println("         if (select count(*) from " + getFullyQualifiedTableName(schema.getTable(relation.getToTableName())) + " p,Inserted i where p." + relation.getToColumnName() + " = i." + relation
                                                                                                                                                                                    .getFromColumnName() + ") = 0");
            sqlWriter.println("         begin");
            sqlWriter.println("            rollback transaction");
            sqlWriter.println("            raiserror ('The " + relation.getFromColumnName() + "''s value doesn''t exist in the " + relation.getToTableName() + " table.', 16, 1)");
            sqlWriter.println("            return");
            sqlWriter.println("         end");
            sqlWriter.println("      end");
            sqlWriter.println("   end;");
            sqlWriter.println();
         }
         else if (relation.getType() == RelationType.SETNULL) {
            sqlWriter.println("   if update(" + relation.getFromColumnName() + ")");
            sqlWriter.println("   begin");
            sqlWriter.println("      if (select count(*) from Inserted where " + relation.getFromColumnName() + " is not null) > 0");
            sqlWriter.println("      begin");
            sqlWriter.println("         if (select count(*) from " + getFullyQualifiedTableName(schema.getTable(relation.getToTableName())) + " p,Inserted i where p." + relation.getToColumnName() + " = i." + relation
                                                                                                                                                                                    .getFromColumnName() + ") = 0");
            sqlWriter.println("         begin");
            sqlWriter.println("            rollback transaction");
            sqlWriter.println("            raiserror ('The " + relation.getFromColumnName() + "''s value doesn''t exist in the " + relation.getToTableName() + " table.', 16, 1)");
            sqlWriter.println("            return");
            sqlWriter.println("         end");
            sqlWriter.println("      end");
            sqlWriter.println("   end;");
            sqlWriter.println();
         }
      }

      if (!table.getAggregations().isEmpty()) {
         sqlWriter.println(";");

         for (Aggregation aggregation : table.getAggregations()) {
            sqlWriter.println(getAggregationUpdateText(aggregation));
         }
      }

      outputAdditionalTriggerStatements(table, TriggerType.UPDATE);

      sqlWriter.println("END" + statementSeparator);
      sqlWriter.println();
   }

   private String getAggregationUpdateText(Aggregation aggregation) {

      return createIfUpdateForCTE(aggregation) +
             "with " +
             createSourceCTE(aggregation, "deleted", "D") +
             ", " +
             createSourceCTE(aggregation, "inserted", "I") +
             ", " +
             createBothCTE(aggregation) +
             ", " +
             createUpdateAggregatedCTE(aggregation) +
             createMergeCTE(aggregation) +
             ";";
   }

   private String createIfUpdateForCTE(Aggregation aggregation) {

      StringBuilder s = new StringBuilder("  if ");

      for (AggregationColumn column : aggregation.getAggregationColumns()) {
         if (!(column.getAggregationType() == AggregationType.COUNT)) {
            s.append("update(").append(column.getSourceColumn()).append(") or ");
         }
      }

      for (AggregationGroup group : aggregation.getAggregationGroups()) {
         if (group.getSourceDerivedFrom() != null && !group.getSourceDerivedFrom().trim().isEmpty()) {
            s.append("update(").append(group.getSourceDerivedFrom()).append(") or ");
         }
         else {
            s.append("update(").append(group.getSource()).append(") or ");
         }
      }

      return s.append("update(").append(aggregation.getDateColumn()).append(") ").toString();
   }

   private String createUpdateAggregatedCTE(Aggregation aggregation) {

      StringBuilder s = new StringBuilder("aggregated as (select ");
      StringBuilder g = new StringBuilder();

      for (AggregationColumn column : aggregation.getAggregationColumns()) {
         switch (column.getAggregationType()) {

            case SUM:
               s.append("SUM(");
               break;
            case COUNT:
               s.append("SUM(");
               break;
         }
         s.append("COALESCE(Inserted")
          .append(column.getDestinationColumn())
          .append(", 0) - coalesce(Deleted")
          .append(column.getDestinationColumn())
          .append(", 0)) as ")
          .append(column.getDestinationColumn())
          .append(", ");
      }

      for (AggregationGroup group : aggregation.getAggregationGroups()) {
         s.append(group.getDestination()).append(", ");
      }

      s.append(aggregation.getDateColumn()).append(" from BOTH group by ");

      for (AggregationGroup group : aggregation.getAggregationGroups()) {
         s.append(group.getDestination()).append(", ");
      }

      return s.append(aggregation.getDateColumn()).append(") ").toString();
   }

   private String createMergeCTE(Aggregation aggregation) {

      StringBuilder s = new StringBuilder("merge ");

      s.append(aggregation.getDestinationTable()).append(" as target using aggregated as source on (");

      for (AggregationGroup group : aggregation.getAggregationGroups()) {
         s.append("target.").append(group.getDestination()).append(" = source.").append(group.getDestination()).append(" and ");
      }

      s.append("target.")
       .append(aggregation.getDateColumn())
       .append(" = source.")
       .append(aggregation.getDateColumn())
       .append(") when matched then update set ");

      for (AggregationColumn column : aggregation.getAggregationColumns()) {
         s.append("target.")
          .append(column.getDestinationColumn())
          .append(" = target.")
          .append(column.getDestinationColumn())
          .append(" + source.")
          .append(column.getDestinationColumn())
          .append(", ");
      }

      s.append(aggregation.getTimeStampColumn())
       .append(" = getDate() when not matched then insert (")
       .append(aggregation.getTimeStampColumn())
       .append(", ")
       .append(aggregation.getDateColumn())
       .append(", ");

      for (AggregationGroup group : aggregation.getAggregationGroups()) {
         s.append(group.getDestination()).append(", ");
      }

      for (AggregationColumn column : aggregation.getAggregationColumns()) {
         s.append(column.getDestinationColumn()).append(", ");
      }

      s.setLength(s.length() - 2);
      s.append(") values (getDate(), Source.").append(aggregation.getDateColumn()).append(", ");

      for (AggregationGroup group : aggregation.getAggregationGroups()) {
         s.append("source.").append(group.getDestination()).append(", ");
      }

      for (AggregationColumn column : aggregation.getAggregationColumns()) {
         s.append("source.").append(column.getDestinationColumn()).append(", ");
      }

      s.setLength(s.length() - 2);
      return s.append(")").toString();
   }
}

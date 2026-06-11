package com.stano.schema.gensql.impl.sqlserver;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TriggerGenerator;
import com.stano.schema.model.Relation;
import com.stano.schema.model.Table;

import java.util.List;

class SQLServerTriggerGenerator extends TriggerGenerator {

   private final SQLServerDeleteTriggerGenerator deleteTriggerGenerator;
   private final SQLServerUpdateTriggerGenerator updateTriggerGenerator;

   SQLServerTriggerGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);

      this.deleteTriggerGenerator = new SQLServerDeleteTriggerGenerator(sqlGenerator);
      this.updateTriggerGenerator = new SQLServerUpdateTriggerGenerator(sqlGenerator);
   }

   @Override
   protected void outputDeleteTrigger(Table table, List<Relation> relations) {

      deleteTriggerGenerator.outputDeleteTrigger(table, relations);
   }

   @Override
   protected void outputUpdateTrigger(Table table, List<Relation> relations) {

      updateTriggerGenerator.outputUpdateTrigger(table, relations);
   }
}

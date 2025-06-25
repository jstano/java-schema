package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TriggerGenerator;
import com.stano.schema.model.Relation;
import com.stano.schema.model.Table;

import java.util.List;

class MSSQLTriggerGenerator extends TriggerGenerator {

   private final MSSQLDeleteTriggerGenerator deleteTriggerGenerator;
   private final MSSQLUpdateTriggerGenerator updateTriggerGenerator;

   MSSQLTriggerGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);

      this.deleteTriggerGenerator = new MSSQLDeleteTriggerGenerator(sqlGenerator);
      this.updateTriggerGenerator = new MSSQLUpdateTriggerGenerator(sqlGenerator);
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

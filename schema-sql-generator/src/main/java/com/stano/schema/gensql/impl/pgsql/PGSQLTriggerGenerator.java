package com.stano.schema.gensql.impl.pgsql;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TriggerGenerator;
import com.stano.schema.model.Relation;
import com.stano.schema.model.Table;

import java.util.List;

public class PGSQLTriggerGenerator extends TriggerGenerator {

   private final PGSQLDeleteTriggerGenerator deleteTriggerGenerator;
   private final PGSQLUpdateTriggerGenerator updateTriggerGenerator;

   protected PGSQLTriggerGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);

      deleteTriggerGenerator = new PGSQLDeleteTriggerGenerator(sqlGenerator);
      updateTriggerGenerator = new PGSQLUpdateTriggerGenerator(sqlGenerator);
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

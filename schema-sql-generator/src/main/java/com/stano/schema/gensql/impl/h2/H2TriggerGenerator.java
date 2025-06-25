package com.stano.schema.gensql.impl.h2;

import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.TriggerGenerator;
import com.stano.schema.model.Relation;
import com.stano.schema.model.Table;

import java.util.List;

class H2TriggerGenerator extends TriggerGenerator {

   H2TriggerGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   @Override
   protected void outputDeleteTrigger(Table table, List<Relation> relations) {

   }

   @Override
   protected void outputUpdateTrigger(Table table, List<Relation> relations) {

   }
}

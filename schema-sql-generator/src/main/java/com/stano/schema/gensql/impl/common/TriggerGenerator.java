package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.ForeignKeyMode;
import com.stano.schema.model.Relation;
import com.stano.schema.model.Table;
import com.stano.schema.model.TriggerType;

import java.util.List;

public abstract class TriggerGenerator extends BaseGenerator {
  protected TriggerGenerator(SQLGenerator sqlGenerator) {
    super(sqlGenerator);
  }

  public void outputTriggers() {
    for (Table table : schema.getTables()) {
      if (shouldOutputDeleteTrigger(table)) {
        outputDeleteTrigger(table, table.getReverseRelations());
      }

      if (shouldOutputUpdateTrigger(table)) {
        outputUpdateTrigger(table, table.getRelations());
      }
    }
  }

  protected abstract void outputDeleteTrigger(Table table, List<Relation> relations);

  protected abstract void outputUpdateTrigger(Table table, List<Relation> relations);

  private boolean shouldOutputDeleteTrigger(Table table) {
    boolean hasDeleteTriggers = table.getTriggers().stream().anyMatch(trigger -> trigger.getTriggerType() == TriggerType.DELETE);

    return hasDeleteTriggers || (!table.getReverseRelations().isEmpty() && sqlGenerator.getSqlGeneratorOptions()
                                                                                       .getForeignKeyMode() == ForeignKeyMode.TRIGGERS) || !table.getAggregations()
                                                                                                                                                 .isEmpty();
  }

  private boolean shouldOutputUpdateTrigger(Table table) {
    boolean hasUpdateTriggers = table.getTriggers().stream().anyMatch(trigger -> trigger.getTriggerType() == TriggerType.UPDATE);

    return hasUpdateTriggers || (!table.getRelations().isEmpty() && sqlGenerator.getSqlGeneratorOptions()
                                                                                .getForeignKeyMode() == ForeignKeyMode.TRIGGERS) || !table.getAggregations()
                                                                                                                                          .isEmpty();
  }
}

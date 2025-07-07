package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProcedureGenerator extends BaseGenerator {

   private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureGenerator.class);

   protected ProcedureGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   public void outputProcedures() {

      schema.getProcedures()
            .stream()
            .filter(procedure -> procedure.getDatabaseType() == databaseType)
            .forEach(procedure -> {
               LOGGER.debug("Generating SQL for procedure " + procedure.getName());

               outputProcedure(procedure);
            });
   }

   protected abstract void outputProcedure(Procedure procedure);
}

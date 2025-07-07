package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FunctionGenerator extends BaseGenerator {

   private static final Logger LOGGER = LoggerFactory.getLogger(FunctionGenerator.class);

   protected FunctionGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   public void outputFunctions() {

      schema.getFunctions()
            .stream()
            .filter(function -> function.getDatabaseType() == databaseType)
            .forEach(function -> {
               LOGGER.debug("Generating SQL for function {}", function.getName());

               outputFunction(function);
            });
   }

   protected abstract void outputFunction(Function function);
}

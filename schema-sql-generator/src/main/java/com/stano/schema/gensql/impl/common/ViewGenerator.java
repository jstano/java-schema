package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ViewGenerator extends BaseGenerator {

   private static final Logger LOGGER = LoggerFactory.getLogger(ViewGenerator.class);

   public ViewGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   public void outputViews() {

      schema.getViews(databaseType)
            .forEach(view -> {

               LOGGER.debug("Generating SQL for view " + getFullyQualifiedViewName(view));

               outputView(view);
            });
   }

   protected abstract void outputView(View view);
}

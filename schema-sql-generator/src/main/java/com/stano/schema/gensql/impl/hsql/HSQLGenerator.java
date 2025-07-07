package com.stano.schema.gensql.impl.hsql;

import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.RelationGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.SQLGeneratorOptions;
import com.stano.schema.gensql.impl.common.TableGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;

public class HSQLGenerator extends SQLGenerator {

   private final TableGenerator tableGenerator;
   private final RelationGenerator relationGenerator;
   private final IndexGenerator indexGenerator;
   private final ViewGenerator viewGenerator;

   public HSQLGenerator(SQLGeneratorOptions sqlGeneratorOptions) {

      super(sqlGeneratorOptions);

      this.tableGenerator = new HSQLTableGenerator(this);
      this.relationGenerator = new HSQLRelationGenerator(this);
      this.indexGenerator = new HSQLIndexGenerator(this);
      this.viewGenerator = new HSQLViewGenerator(this);
   }

   @Override
   protected void outputHeader() {

      sqlWriter.println("set database sql syntax ora true" + statementSeparator);
      sqlWriter.println();
   }

   @Override
   protected void outputTables() {

      tableGenerator.outputTables();
   }

   @Override
   protected void outputRelations() {

      relationGenerator.outputRelations();
   }

   @Override
   protected void outputIndexes() {

      indexGenerator.outputIndexes();
   }

   @Override
   protected void outputOtherSqlTop() {

   }

   @Override
   protected void outputOtherSqlBottom() {

   }

   @Override
   protected void outputTriggers() {

   }

   @Override
   protected void outputFunctions() {

   }

   @Override
   protected void outputViews() {

      viewGenerator.outputViews();
   }

   @Override
   protected void outputProcedures() {

   }
}

package com.stano.schema.gensql.impl.mysql;

import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.RelationGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.SQLGeneratorOptions;
import com.stano.schema.gensql.impl.common.TableGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;

public class MySQLGenerator extends SQLGenerator {

   private final TableGenerator tableGenerator;
   private final IndexGenerator indexGenerator;
   private final RelationGenerator relationGenerator;
   private final ViewGenerator viewGenerator;

   public MySQLGenerator(SQLGeneratorOptions sqlGeneratorOptions) {

      super(sqlGeneratorOptions);

      this.tableGenerator = new MySQLTableGenerator(this);
      this.relationGenerator = new MySQLRelationGenerator(this);
      this.indexGenerator = new MySQLIndexGenerator(this);
      this.viewGenerator = new MySQLViewGenerator(this);
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

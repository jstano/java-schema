package com.stano.schema.gensql.impl.mssql;

import com.stano.schema.gensql.impl.common.FunctionGenerator;
import com.stano.schema.gensql.impl.common.IndexGenerator;
import com.stano.schema.gensql.impl.common.OtherSqlGenerator;
import com.stano.schema.gensql.impl.common.ProcedureGenerator;
import com.stano.schema.gensql.impl.common.RelationGenerator;
import com.stano.schema.gensql.impl.common.SQLGenerator;
import com.stano.schema.gensql.impl.common.SQLGeneratorOptions;
import com.stano.schema.gensql.impl.common.TableGenerator;
import com.stano.schema.gensql.impl.common.TriggerGenerator;
import com.stano.schema.gensql.impl.common.ViewGenerator;

public class MSSQLGenerator extends SQLGenerator {

   private final TableGenerator tableGenerator;
   private final RelationGenerator relationGenerator;
   private final IndexGenerator indexGenerator;
   private final FunctionGenerator functionGenerator;
   private final ViewGenerator viewGenerator;
   private final ProcedureGenerator procedureGenerator;
   private final TriggerGenerator triggerGenerator;
   private final OtherSqlGenerator otherSqlGenerator;

   public MSSQLGenerator(SQLGeneratorOptions sqlGeneratorOptions) {

      super(sqlGeneratorOptions);

      this.tableGenerator = new MSSQLTableGenerator(this);
      this.relationGenerator = new MSSQLRelationGenerator(this);
      this.indexGenerator = new MSSQLIndexGenerator(this);
      this.functionGenerator = new MSSQLFunctionGenerator(this);
      this.viewGenerator = new MSSQLViewGenerator(this);
      this.procedureGenerator = new MSSQLProcedureGenerator(this);
      this.triggerGenerator = new MSSQLTriggerGenerator(this);
      this.otherSqlGenerator = new MSSQLOtherSqlGenerator(this);
   }

   @Override
   protected void outputTables() {

      tableGenerator.outputTables();
   }

   @Override
   protected void outputIndexes() {

      indexGenerator.outputIndexes();
   }

   @Override
   protected void outputRelations() {

      relationGenerator.outputRelations();
   }

   @Override
   protected void outputOtherSqlTop() {

      otherSqlGenerator.outputOtherSqlTop();
   }

   @Override
   protected void outputOtherSqlBottom() {

      otherSqlGenerator.outputOtherSqlBottom();
   }

   @Override
   protected void outputTriggers() {

      triggerGenerator.outputTriggers();
   }

   @Override
   protected void outputFunctions() {

      functionGenerator.outputFunctions();
   }

   @Override
   protected void outputViews() {

      viewGenerator.outputViews();
   }

   @Override
   protected void outputProcedures() {

      procedureGenerator.outputProcedures();
   }
}

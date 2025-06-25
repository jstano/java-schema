package com.stano.schema.gensql.impl.pgsql;

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

public class PGSQLGenerator extends SQLGenerator {

   private final TableGenerator tableGenerator;
   private final RelationGenerator relationGenerator;
   private final IndexGenerator indexGenerator;
   private final FunctionGenerator functionGenerator;
   private final ViewGenerator viewGenerator;
   private final ProcedureGenerator procedureGenerator;
   private final TriggerGenerator triggerGenerator;
   private final OtherSqlGenerator otherSqlGenerator;

   public PGSQLGenerator(SQLGeneratorOptions sqlGeneratorOptions) {

      super(sqlGeneratorOptions);

      this.tableGenerator = new PGSQLTableGenerator(this);
      this.relationGenerator = new PGSQLRelationGenerator(this);
      this.indexGenerator = new PGSQLIndexGenerator(this);
      this.functionGenerator = new PGSQLFunctionGenerator(this);
      this.viewGenerator = new PGSQLViewGenerator(this);
      this.procedureGenerator = new PGSQLProcedureGenerator(this);
      this.triggerGenerator = new PGSQLTriggerGenerator(this);
      this.otherSqlGenerator = new PGSQLOtherSqlGenerator(this);
   }

   @Override
   protected void outputHeader() {

      createUUIDGeneratorFunction();
      createExtensions();
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

   @Override
   protected void outputOtherSqlTop() {

      otherSqlGenerator.outputOtherSqlTop();
   }

   @Override
   protected void outputOtherSqlBottom() {

      otherSqlGenerator.outputOtherSqlBottom();
   }

   private void createUUIDGeneratorFunction() {

      sqlWriter.println("create or replace function generate_uuid() returns uuid language plpgsql parallel safe as $$");
      sqlWriter.println("declare");
      sqlWriter.println("   -- The current UNIX timestamp in milliseconds");
      sqlWriter.println("   unix_time_ms CONSTANT bytea NOT NULL DEFAULT substring(int8send((extract(epoch FROM clock_timestamp()) * 1000)::bigint) from 3);");
      sqlWriter.println();
      sqlWriter.println("   -- The buffer used to create the UUID, starting with the UNIX timestamp and followed by random bytes");
      sqlWriter.println("   buffer bytea not null default unix_time_ms || gen_random_bytes(10);");
      sqlWriter.println("begin");
      sqlWriter.println("   -- Set most significant 4 bits of 7th byte to 7 (for UUID v7), keeping the last 4 bits unchanged");
      sqlWriter.println("   buffer = set_byte(buffer, 6, (b'0111' || get_byte(buffer, 6)::bit(4))::bit(8)::int);");
      sqlWriter.println();
      sqlWriter.println("   -- Set most significant 2 bits of 9th byte to 2 (the UUID variant specified in RFC 4122), keeping the last 6 bits unchanged");
      sqlWriter.println("   buffer = set_byte(buffer, 8, (b'10' || get_byte(buffer, 8)::bit(6))::bit(8)::int);");
      sqlWriter.println();
      sqlWriter.println("   return encode(buffer, 'hex');");
      sqlWriter.println("end");
      sqlWriter.println("$$" + statementSeparator);
      sqlWriter.println();
   }

   private void createExtensions() {

      sqlWriter.println("do $createextensions$");
      sqlWriter.println("begin");
      sqlWriter.println("   if (select usesuper from pg_user where usename = CURRENT_USER) then");
      sqlWriter.println("      create extension if not exists \"uuid-ossp\";");
      sqlWriter.println("      create extension if not exists \"citext\";");
      sqlWriter.println("   else");
      sqlWriter.println("      raise notice 'User % is not a superuser, could not create uuid-ossp or citext extensions.', current_user;");
      sqlWriter.println("   end if;");
      sqlWriter.println("end;");
      sqlWriter.println("$createextensions$" + statementSeparator);
      sqlWriter.println();
   }
}

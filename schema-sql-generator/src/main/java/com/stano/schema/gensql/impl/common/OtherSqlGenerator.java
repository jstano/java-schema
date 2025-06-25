package com.stano.schema.gensql.impl.common;

import com.stano.schema.model.OtherSqlOrder;
import org.apache.commons.lang3.StringUtils;

public abstract class OtherSqlGenerator extends BaseGenerator {

   protected OtherSqlGenerator(SQLGenerator sqlGenerator) {

      super(sqlGenerator);
   }

   public void outputOtherSqlTop() {

      schema.getOtherSql()
            .stream()
            .filter(otherSql -> otherSql.getDatabaseType() == databaseType)
            .filter(otherSql -> otherSql.getOrder() == OtherSqlOrder.TOP)
            .filter(otherSql -> StringUtils.isNotBlank(otherSql.getSql()))
            .forEach(otherSql -> outputOtherSql(otherSql.getSql()));
   }

   public void outputOtherSqlBottom() {

      schema.getOtherSql()
            .stream()
            .filter(otherSql -> otherSql.getDatabaseType() == databaseType)
            .filter(otherSql -> otherSql.getOrder() == OtherSqlOrder.BOTTOM)
            .filter(otherSql -> StringUtils.isNotBlank(otherSql.getSql()))
            .forEach(otherSql -> outputOtherSql(otherSql.getSql()));
   }

   protected abstract void outputOtherSql(String sql);
}

package com.stano.schema.model;

public enum BooleanMode {

   NATIVE,
   YES_NO,
   YN,

   /**
    * @deprecated use YN
    */
   @Deprecated
   TEXT;
}

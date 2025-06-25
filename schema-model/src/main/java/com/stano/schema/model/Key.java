package com.stano.schema.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Key {

   private final KeyType type;
   private final List<String> columns;
   private final boolean cluster;
   private final boolean compress;
   private final String include;

   public Key(KeyType type, List<String> columns, boolean cluster, boolean compress, String include) {

      this.type = type;
      this.columns = Collections.unmodifiableList(new ArrayList<>(columns));
      this.cluster = cluster;
      this.compress = compress;
      this.include = include;
   }

   public Key(KeyType type, List<String> columns) {

      this.type = type;
      this.columns = Collections.unmodifiableList(new ArrayList<>(columns));
      this.cluster = false;
      this.compress = false;
      this.include = null;
   }

   public KeyType getType() {

      return type;
   }

   public List<String> getColumns() {

      return columns;
   }

   public boolean isCluster() {

      return cluster;
   }

   public boolean isCompress() {

      return compress;
   }

   public String getInclude() {

      return include;
   }

   public boolean containsColumn(String columnName) {

      return columns.stream().anyMatch(colName -> colName.equals(columnName));
   }

   public String getColumnsAsString() {

      return String.join(",", columns);
   }
}

package com.stano.schema.installer.liquibase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileServices {
  public File createTempFile(String prefix, String suffix) throws IOException {
    return File.createTempFile(prefix, suffix);
  }

  public FileWriter createFileWriter(File file) throws IOException {
    return new FileWriter(file);
  }
}

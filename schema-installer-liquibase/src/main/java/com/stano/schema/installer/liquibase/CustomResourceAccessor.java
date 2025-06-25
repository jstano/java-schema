package com.stano.schema.installer.liquibase;

import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.PathResource;
import liquibase.resource.Resource;
import liquibase.resource.URIResource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class CustomResourceAccessor extends ClassLoaderResourceAccessor {
  private final ClassLoader classLoader;

  public CustomResourceAccessor(ClassLoader classLoader) {
    super(classLoader);

    this.classLoader = classLoader;
  }

  @Override
  public List<Resource> getAll(String path) throws IOException {
    try {
      if (path.startsWith("file:")) {
        URI uri = new URI(path);
        return Collections.singletonList(new PathResource(path, Paths.get(uri)));
      }
      else if (path.startsWith("jar:file:")) {
        URI uri = new URI(path);
        return Collections.singletonList(new URIResource(path, uri));
      }
      else {
        URI uri = classLoader.getResource(path).toURI();
        return Collections.singletonList(new URIResource(path, uri));
      }
    }
    catch (URISyntaxException x) {
      throw new IOException(x);
    }
  }
}

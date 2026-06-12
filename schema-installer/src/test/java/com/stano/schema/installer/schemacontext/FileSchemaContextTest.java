package com.stano.schema.installer.schemacontext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileSchemaContext")
class FileSchemaContextTest {

  @Mock private File mockFile;

  @Mock private Connection mockConnection;

  @Test
  @DisplayName("the FileSchemaContext should work")
  void theFileSchemaContextShouldWork() throws SQLException, URISyntaxException {
    when(mockFile.toURI()).thenReturn(new URI("file://test"));

    FileSchemaContext fileSchemaContext = new FileSchemaContext(mockFile);

    assertEquals(fileSchemaContext.getSchemaUrl().toString(), "file://test");
    assertNull(fileSchemaContext.getMigrationScriptLocator(mockConnection));
    assertFalse(fileSchemaContext.schemaIsInstalled(mockConnection));
  }
}

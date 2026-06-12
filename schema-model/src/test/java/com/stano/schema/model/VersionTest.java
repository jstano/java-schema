package com.stano.schema.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Version")
class VersionTest {

  @Test
  @DisplayName("constructors should set fields and getters should return them")
  void constructorsShouldSetFields() {
    Version v2 = new Version(1, 2);

    assertEquals(v2.getMajorVersion(), 1);
    assertEquals(v2.getMinorVersion(), 2);
    assertEquals(v2.getPatchVersion(), 0);
    assertFalse(v2.isPreReleaseSuffix());

    Version v3 = new Version(1, 2, 3);

    assertEquals(v3.getMajorVersion(), 1);
    assertEquals(v3.getMinorVersion(), 2);
    assertEquals(v3.getPatchVersion(), 3);
    assertFalse(v3.isPreReleaseSuffix());

    Version v4 = new Version(2, 0, 0, true);

    assertEquals(v4.getMajorVersion(), 2);
    assertEquals(v4.getMinorVersion(), 0);
    assertEquals(v4.getPatchVersion(), 0);
    assertTrue(v4.isPreReleaseSuffix());
  }

  @Test
  @DisplayName("string constructor should parse versions with and without patch and SNAPSHOT")
  void stringConstructorShouldParseVersions() {
    Version a = new Version("01.02");
    assertEquals(a.getMajorVersion(), 1);
    assertEquals(a.getMinorVersion(), 2);
    assertEquals(a.getPatchVersion(), 0);
    assertFalse(a.isPreReleaseSuffix());

    Version b = new Version("01.02.03");
    assertEquals(b.getMajorVersion(), 1);
    assertEquals(b.getMinorVersion(), 2);
    assertEquals(b.getPatchVersion(), 3);
    assertFalse(b.isPreReleaseSuffix());

    Version c = new Version("10.20-SNAPSHOT");
    assertEquals(c.getMajorVersion(), 10);
    assertEquals(c.getMinorVersion(), 20);
    assertEquals(c.getPatchVersion(), 0);
    assertTrue(c.isPreReleaseSuffix());

    Version d = new Version("03.04.00-SNAPSHOT");
    assertEquals(d.getMajorVersion(), 3);
    assertEquals(d.getMinorVersion(), 4);
    assertEquals(d.getPatchVersion(), 0);
    assertTrue(d.isPreReleaseSuffix());
  }

  @ParameterizedTest
  @CsvSource({
    "1,2,0,false,01.02",
    "1,2,0,true,01.02-SNAPSHOT",
    "1,2,3,false,01.02.03",
    "1,2,3,true,01.02.03-SNAPSHOT"
  })
  @DisplayName("toString should zero-pad and include -SNAPSHOT when preReleaseSuffix is true")
  void toStringShouldZeroPadAndIncludeSnapshot(
      int major, int minor, int patch, boolean pre, String expected) {
    Version version = new Version(major, minor, patch, pre);
    assertEquals(version.toString(), expected);
  }

  @Test
  @DisplayName("equals and hashCode should be consistent and depend on all fields")
  void equalsAndHashCodeShouldBeConsistent() {
    Version v1 = new Version(1, 2, 0, false);
    Version v2 = new Version("01.02");
    Version v3 = new Version(1, 2, 0, true);

    assertEquals(v1, v2);
    assertEquals(v1.hashCode(), v2.hashCode());
    assertNotEquals(v1, v3);
  }

  @Test
  @DisplayName(
      "compareTo should order by major, minor, patch, then preRelease (preRelease considered"
          + " greater)")
  void compareToShouldOrderCorrectly() {
    assertTrue(new Version(1, 0).compareTo(new Version(2, 0)) < 0);
    assertTrue(new Version(2, 0).compareTo(new Version(1, 9, 9)) > 0);

    assertTrue(new Version(1, 2).compareTo(new Version(1, 3)) < 0);
    assertTrue(new Version(1, 4).compareTo(new Version(1, 3, 10)) > 0);

    assertTrue(new Version(1, 2, 3).compareTo(new Version(1, 2, 4)) < 0);
    assertTrue(new Version(1, 2, 5).compareTo(new Version(1, 2, 4)) > 0);

    assertTrue(new Version(1, 2, 0, true).compareTo(new Version(1, 2, 0, false)) > 0);
    assertTrue(new Version(1, 2, 3, false).compareTo(new Version(1, 2, 3, true)) < 0);

    assertEquals(new Version(1, 2, 3, true).compareTo(new Version(1, 2, 3, true)), 0);
  }

  @Test
  @DisplayName("Schema should store and return Version via getter/setter")
  void schemaShouldStoreAndReturnVersion() throws MalformedURLException {
    Schema schema = new Schema(new URL("https://example.com/schema.json"));
    Version ver = new Version(1, 2, 3, true);

    schema.setVersion(ver);

    assertEquals(schema.getVersion(), ver);
  }

  @Test
  @DisplayName(
      "equals should handle null, type, and differences across fields; hashCode and contract"
          + " properties")
  void equalsShouldHandleNullAndType() {
    Version a = new Version(1, 2, 0, false);
    Version b = new Version("1.2");
    Version c = new Version(1, 2, 1, false);
    Version d = new Version(1, 3, 0, false);
    Version e = new Version(2, 2, 0, false);
    Version f = new Version(1, 2, 0, true);

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());

    assertNotEquals(a, c);
    assertNotEquals(a, d);
    assertNotEquals(a, e);
    assertNotEquals(a, f);

    assertNotEquals(a, null);
    assertNotEquals(a, "1.2");

    assertEquals(a, b);
    assertEquals(b, a);

    Version b2 = new Version(1, 2, 0, false);
    assertEquals(a, b);
    assertEquals(b, b2);
    assertEquals(a, b2);

    assertEquals(a.hashCode(), a.hashCode());
    assertEquals(b.hashCode(), b2.hashCode());
  }

  @Test
  @DisplayName(
      "compareTo contract: reflexivity, anti-symmetry, transitivity, and consistency with equals")
  void compareToContract() {
    Version v1 = new Version(1, 2);
    Version v1b = new Version("1.2");
    Version v2 = new Version(1, 3);
    Version v3 = new Version(2, 0);

    assertEquals(v1.compareTo(v1), 0);

    assertEquals(v1, v1b);
    assertEquals(v1.compareTo(v1b), 0);

    assertEquals(Integer.signum(v1.compareTo(v2)), -Integer.signum(v2.compareTo(v1)));

    assertTrue(v1.compareTo(v2) < 0);
    assertTrue(v2.compareTo(v3) < 0);
    assertTrue(v1.compareTo(v3) < 0);

    assertEquals(new Version(1, 2).compareTo(new Version(1, 2, 0)), 0);

    assertEquals(
        Integer.signum(new Version(1, 2, 0, true).compareTo(new Version(1, 2, 0, false))), 1);
    assertEquals(
        Integer.signum(new Version(1, 2, 0, false).compareTo(new Version(1, 2, 0, true))), -1);
  }
}

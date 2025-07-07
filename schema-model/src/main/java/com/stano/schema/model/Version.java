package com.stano.schema.model;

public final class Version implements Comparable<Version> {
  private final int majorVersion;
  private final int minorVersion;
  private final int patchVersion;
  private final boolean preReleaseSuffix;

  public Version(int majorVersion, int minorVersion) {
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
    this.patchVersion = 0;
    this.preReleaseSuffix = false;
  }

  public Version(int majorVersion, int minorVersion, int patchVersion) {
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
    this.patchVersion = patchVersion;
    this.preReleaseSuffix = false;
  }

  public Version(int majorVersion, int minorVersion, int patchVersion, boolean preReleaseSuffix) {
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
    this.patchVersion = patchVersion;
    this.preReleaseSuffix = preReleaseSuffix;
  }

  public Version(String versionStr) {
    this.preReleaseSuffix = versionStr.contains("-SNAPSHOT");

    if (this.preReleaseSuffix) {
      versionStr = versionStr.substring(0, versionStr.indexOf("-SNAPSHOT"));
    }

    String[] parts = versionStr.split("\\.");

    this.majorVersion = Integer.parseInt(parts[0]);
    this.minorVersion = Integer.parseInt(parts[1]);

    if (parts.length == 3) {
      this.patchVersion = Integer.parseInt(parts[2]);
    }
    else {
      this.patchVersion = 0;
    }
  }

  public int getMajorVersion() {
    return majorVersion;
  }

  public int getMinorVersion() {
    return minorVersion;
  }

  public int getPatchVersion() {
    return patchVersion;
  }

  public boolean isPreReleaseSuffix() {
    return preReleaseSuffix;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Version that = (Version)o;

    if (majorVersion != that.majorVersion) {
      return false;
    }

    if (minorVersion != that.minorVersion) {
      return false;
    }

    if (patchVersion != that.patchVersion) {
      return false;
    }

    if (preReleaseSuffix != that.preReleaseSuffix) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = majorVersion;
    result = 31 * result + minorVersion;
    result = 31 * result + patchVersion;
    result = 31 * result + (preReleaseSuffix ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    if (preReleaseSuffix) {
      if (patchVersion < 1) {
        return String.format("%02d.%02d-SNAPSHOT", majorVersion, minorVersion);
      }

      return String.format("%02d.%02d.%02d-SNAPSHOT", majorVersion, minorVersion, patchVersion);
    }

    if (patchVersion < 1) {
      return String.format("%02d.%02d", majorVersion, minorVersion);
    }

    return String.format("%02d.%02d.%02d", majorVersion, minorVersion, patchVersion);
  }

  @Override
  public int compareTo(Version version) {
    if (this == version) {
      return 0;
    }

    if (majorVersion < version.majorVersion) {
      return -1;
    }

    if (majorVersion > version.majorVersion) {
      return 1;
    }

    if (minorVersion < version.minorVersion) {
      return -1;
    }

    if (minorVersion > version.minorVersion) {
      return 1;
    }

    if (patchVersion < version.patchVersion) {
      return -1;
    }

    if (patchVersion > version.patchVersion) {
      return 1;
    }

    if (preReleaseSuffix && !version.preReleaseSuffix) {
      return 1;
    }

    if (!preReleaseSuffix && version.preReleaseSuffix) {
      return -1;
    }

    return 0;
  }
}

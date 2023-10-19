package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.saucelabs.saucerest.DataCenter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class DataCenterTest {
  @ParameterizedTest
  @CsvSource({
    "US_WEST,        US_WEST",
    "us_WeSt,        US_WEST",
    "US_EAST,        US_EAST",
    "us_EasT,        US_EAST",
    "EU_CENTRAL,     EU_CENTRAL",
    "Eu_central,     EU_CENTRAL",
  })
  void testFromString(String input, DataCenter expected) {
    assertEquals(expected, DataCenter.fromString(input));
  }

  @Test
  void testInvalidString() {
    assertNull(DataCenter.fromString("unknown"));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  void testEdsServer(DataCenter datacenter) {
    assertTrue(datacenter.edsServer().endsWith("saucelabs.com/v1/eds/"));
  }

  @Test
  void testFromStringNullAndEmpty() {
    assertNull(DataCenter.fromString(null));
    assertNull(DataCenter.fromString(""));
  }

  @Test
  void testFromStringInvalid() {
    assertNull(DataCenter.fromString("unknown"));
    assertNull(DataCenter.fromString("random"));
  }

  @Test
  void testEdsServerFormat() {
    DataCenter dataCenter = DataCenter.US_WEST;
    String edsServerUrl = dataCenter.edsServer();

    assertTrue(edsServerUrl.startsWith("https://api.us-west-1.saucelabs.com/v1/eds/"));
    assertTrue(edsServerUrl.endsWith("/v1/eds/"));
  }

  @Test
  void testEdsServerNonEmpty() {
    for (DataCenter dataCenter : DataCenter.values()) {
      assertNotNull(dataCenter.edsServer());
      assertFalse(dataCenter.edsServer().isEmpty());
    }
  }

  @Test
  void testServerUrls() {
    assertEquals("https://saucelabs.com/", DataCenter.US_WEST.server());
    assertEquals("https://api.us-west-1.saucelabs.com/", DataCenter.US_WEST.apiServer());
    assertEquals("https://app.saucelabs.com/", DataCenter.US_WEST.appServer());

    assertEquals("https://eu-central-1.saucelabs.com/", DataCenter.EU_CENTRAL.server());
    assertEquals("https://api.eu-central-1.saucelabs.com/", DataCenter.EU_CENTRAL.apiServer());
    assertEquals("https://app.eu-central-1.saucelabs.com/", DataCenter.EU_CENTRAL.appServer());
  }
}

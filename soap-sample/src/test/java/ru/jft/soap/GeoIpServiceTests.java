package ru.jft.soap;

import com.lavasoft.GeoIPService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GeoIpServiceTests {

  @Test
  public void testMyIp() {
    String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("84.42.75.126");
    System.out.println(ipLocation);
    Assert.assertEquals(ipLocation, "<GeoIP><Country>RU</Country><State>10</State></GeoIP>");
  }
}
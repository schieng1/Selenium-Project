package com.gdit.toh;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class Node1 {
	
  @Test
  public void testGrid() {
	  try {
			URL url = new URL("http://192.168.1.66:3825/wd/hub");
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setBrowserName("chrome");
			capabilities.setPlatform(Platform.WINDOWS);
			WebDriver driver = new RemoteWebDriver(url, capabilities);
			driver.get("http://www.tasteofhome.com");
			System.out.println("Title is " + driver.getTitle());
			driver.quit();
		} catch (MalformedURLException e) {
			Reporter.log("Error" + e);
			Assert.assertFalse(false, "An error occurred during the test!");
		}
  }
}

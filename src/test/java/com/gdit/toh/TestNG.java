package com.gdit.toh;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.sun.tools.sjavac.Log;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;

public class TestNG {
	
	//private static DatabaseConnection conn = new DatabaseConnection();
	BaseTest bt = new BaseTest();
	public List<String> ingredList;
	WebDriver driver = BaseTest.driver;
	private static Logger logger = LogManager.getLogger();

  @BeforeTest
  public void setDriver() throws InterruptedException {
	  System.setProperty("webdriver.chrome.driver", (System.getProperty("user.dir")) + "\\src\\main\\resources\\chromedriver_win32\\chromedriver.exe");
	  driver = new ChromeDriver();
	  driver.manage().window().maximize();
  }
  
  @Test
	public void f() {
		try {
			URL url = new URL("http://192.168.1.66:7058/wd/hub");
			/*
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setCapability("browserVersion", 84);
			chromeOptions.setCapability("platformName", "Windows ");
			*/
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			cap.setPlatform(Platform.WINDOWS);
			WebDriver driver = new RemoteWebDriver(url, cap);
			driver.get("http://www.google.com");
			System.out.println("Title is " + driver.getTitle());
			driver.quit();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
/*
  @Test(priority = 1)
  public void testSearchWindow() {
	  driver.get("https://www.tasteofhome.com/search/index?search=");
	  WebElement searchBar = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("searchTextRecipe Detail")));
	  WebElement searchButton = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"navbar-top\"]/div/div/div/div/div/div[3]/form/div[1]/div/button")));
	  searchBar.clear();
	  searchBar.sendKeys("dolphin");
	  searchButton.click();
	  boolean noResults = driver.findElements(By.xpath("//*[@id=\"noResultsFound\"]/section/div[2]")).size() > 0;
	  Assert.assertFalse(noResults, "There was no search result for this entry!");
  }
  
  @Test(priority = 2, dataProvider = "DP")
  	public void testRecipe(String name, String GPR) throws SQLException {
  		SoftAssert softAssertion = new SoftAssert();
  		String[] arr = name.split(",");
  		for(String recipe : arr) {
			WebElement searchBar = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("searchTextRecipe Detail")));
			searchBar.clear();
			searchBar.sendKeys(recipe);
  			driver.findElement(By.xpath("//*[@id=\"navbar-top\"]/div/div/div/div/div/div[3]/form/div[1]/div/button")).click();
  			By recipeName = By.partialLinkText(recipe);
  			WebElement waitForLinks = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("searchResultsContainer")));
  			driver.findElement(recipeName).click();
  			boolean isGrandPrize = driver.findElements(By.xpath("/html/body/main/div[2]/section[1]/div/div[2]/div[3]/div[1]/ul/li[3]/i")).size() != 0;
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
  			if(GPR.equalsIgnoreCase("Yes")) {
  				if(isGrandPrize == false) {
  					softAssertion.assertTrue(isGrandPrize);
  				}
  				else {
  					softAssertion.assertTrue(isGrandPrize);
  				}
  			}
  			if(GPR.equalsIgnoreCase("No")) {
  				if(isGrandPrize == false) {
  					softAssertion.assertTrue(isGrandPrize);	
  				}
  				if(isGrandPrize == true) {
  					softAssertion.assertFalse(isGrandPrize);
  				}
  			}
  			driver.navigate().back();
  			driver.navigate().back();
			continue;
			}
  		softAssertion.assertAll();
  	}  

  	@DataProvider(name = "DP")
  	public Object [][] readDB() throws SQLException {
  		List<String[]> arr = new ArrayList<>();
  		boolean failTest = false;
  		Connection con = null;
		Statement stmt = null;
  		try {
  			con = DatabaseConnection.oracleDatabase();
  			stmt = con.createStatement();
  			ResultSet resultSet = stmt.executeQuery("SELECT * FROM TOH_RECIPE");   
			while(resultSet.next()) {
				String[] recipes = {resultSet.getString("RECIPE_NAME"), resultSet.getString("GRAND_PRIZE_RECIP")};
				arr.add(recipes);
			}
	  }
	  catch (Exception e) {
		  failTest = true;
		  Reporter.log("Error" + e);
		  Assert.assertFalse(failTest);
	  }
  	  finally {
  		  stmt.close();
  		  con.close();
  	  }
  	  Object[][] objectArr = new Object [arr.size()][2];
  	  int counter = 0;
  	  for(String[] recipeArray : arr) {
  		  objectArr [counter][0] = recipeArray [0];
  		  objectArr [counter][1] = recipeArray [1];
  		  counter++;
  	  }
  	  return objectArr;
  }
*/
  @AfterTest
  public void closeDriver() {
	  driver.close();
  }
}

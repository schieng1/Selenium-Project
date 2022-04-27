package com.gdit.toh;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import com.google.common.base.Function;


public class SearchPage extends BaseTest {

    private static DatabaseConnection connection = new DatabaseConnection();
    private static RecipePage recipeP = new RecipePage();
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
		BaseTest bt = new BaseTest();
		bt.setUp();
		try {
			enterRecipe(bt.getWebDriver(), connection.c, recipeP.ingredList);
		} catch (SQLException e) {
			logger.error("SQL Error", e);
		}
		bt.cleanUp();
    }
	
	public static void enterRecipe(WebDriver driver, Connection c, List<String> ingredList) throws SQLException {
		By searchBar = By.id("searchTextRecipe Detail");
		try {
			for(String recipe : connection.recipes) {
				logger.info("Searching for recipe {}", recipe);
				waitForSearchBar(driver);
				driver.findElement(searchBar).sendKeys(recipe);
				waitForSearchButton(driver);
				clickSearchButton(driver);
				waitForPage(driver);
				String contest = RecipePage.getInfo(c,recipe);
				RecipePage.getGrandPrizeRecipe(c, recipe, contest);
				RecipePage.waitForIngreds(driver);
				List<String> ingredients = RecipePage.getIngredients(driver);
				List<String> reviews = RecipePage.getReviews(driver);
				double avgRating = RecipePage.getRatings(driver);
				RecipePage.importToSQL(c, driver, ingredients, reviews, avgRating, recipe);
				driver.navigate().back();
				driver.navigate().back();
				waitForSearchBar(driver);
				driver.findElement(searchBar).clear();
			}
		}
		catch(NoSuchElementException e) {
			logger.error("Error", e);
		}
		catch(Exception e) {
			logger.error("Error", e);
		}
		finally {
			c.close();
		}
	}
	
	public static void waitForPage(WebDriver driver) {
		WebDriverWait waitPage = new WebDriverWait(driver, 10);
		waitPage.until(ExpectedConditions.elementToBeClickable(By.id("searchResultsContainer"))); 
	}

	public static void waitForSearchBar(WebDriver driver) {
		WebDriverWait waitBar = new WebDriverWait(driver, 10);
    	waitBar.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"searchTextRecipe Detail\"]")));
	}
	
	public static void waitForSearchButton(WebDriver driver) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(NoSuchElementException.class);
		
		WebElement waitBut = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.xpath("//*[@id=\"navbar-top\"]/div/div/div/div/div/div[3]/form/div[1]/div/button"));
			}
		});
	}
	
	public static void clickSearchButton(WebDriver driver) {
		By searchButton = By.xpath("//*[@id=\"navbar-top\"]/div/div/div/div/div/div[3]/form/div[1]/div/button");
		driver.findElement(searchButton).click();
	}
}
package com.gdit.toh;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RecipePage extends SearchPage {

	public List<String> ingredList = null;
	private static Logger logger = LogManager.getLogger();
	
	public static void main(String[] args) {
		BaseTest bt = new BaseTest();
		recipePage(bt.getWebDriver());
	}
	
	public static void recipePage(WebDriver driver) {
		BaseTest.driver = driver;
	}
	
	public static void waitForIngreds(WebDriver driver) {
		WebDriverWait waitIngreds = new WebDriverWait(driver, 10);
     	waitIngreds.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/div[4]/section[1]/div[1]/div[1]/ul")));			
	}
	
	public static List<String> getIngredients(WebDriver driver) {
		List<WebElement> ingred = driver.findElements(By.xpath("/html/body/main/div[4]/section[1]/div[1]/div[1]/ul"));
		String ingredList = "";
		for(WebElement e : ingred) {
			ingredList += (e.getText());
		}
		String[] arr = ingredList.split("\n");
		List<String> Ingredients = new ArrayList<String>();
		for(String i: arr) {
			Ingredients.add(i);
		}
		return Ingredients;
	}

	public static List<String> getReviews(WebDriver driver) {
		By viewMore = By.id("toh-show-more-reviews");
		driver.findElement(viewMore).click();
		List<WebElement> review = driver.findElements(By.className("recipe-comments__comment"));
		String reviews = "";
		for(WebElement e : review) {
			reviews += (e.getText());
			reviews += "\n";
		}
		String[] arr = reviews.split("\n");
		List<String> reviewList = new ArrayList<String>();
		for(String i: arr) {
			reviewList.add(i);
		}
		return reviewList;
	}

	public static double getRatings(WebDriver driver) {
		WebDriverWait waitPage = new WebDriverWait(driver, 10);
		waitPage.until(ExpectedConditions.visibilityOfElementLocated(By.className("rating")));
		List<WebElement> ratings = driver.findElements(By.xpath("/html/body/main/div[2]/section[1]/div/div[2]/div[3]/div[1]/ul/li[3]/i"));    
		double counter = 0;
		for(WebElement element : ratings) {
			String check = element.getAttribute("class");
			if(check.equals("dashicons dashicons-star-filled")) {
				counter++;
			}
			else if(check.equals("dashicons dashicons-star-half")) {
				counter += 0.5;
			}
			else if(check.equals("dashicons dashicons-star-empty")) {
				counter += 0;
			}
		}
		double avgRating = counter;
		return avgRating;
	}
	
	public static String getInfo(Connection c, String recipe) {
		String contest = "";
		try {
			String queryId = "SELECT RECIPE_ID\n" +
				 		 "FROM TOH_RECIPE\n" +
				 		 "WHERE RECIPE_NAME =" + "'" + recipe + "'";
			CallableStatement cs = c.prepareCall(queryId);
			ResultSet rs = cs.executeQuery();
			int Id = 0;
			while(rs.next()) {
				Id = rs.getInt(1);
			}
			String grandPrizeQuery = ("SELECT GRAND_PRIZE_RECIP \n" +
				  				  "FROM TOH_RECIPE\n" +
								  "WHERE RECIPE_ID = " + Id);
			cs = c.prepareCall(grandPrizeQuery);
			rs = cs.executeQuery();
			while(rs.next()) {
				contest = rs.getString(1);
			}
		}
		catch(SQLException e) {
			logger.error("Error" + e);
		}
		return contest; 
	}


	public static void getGrandPrizeRecipe(Connection c, String recipe, String contest) {
		boolean exit = false; 
		boolean exit2 = false;
		List<WebElement> links = new ArrayList<>();
		while(exit == false) {
			if(contest.equals("Yes") || contest.equals("yes")) {
				By recipeName = By.partialLinkText(recipe);
				WebElement waitRecipe = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"searchResultsContainer\"]")));
				driver.findElement(recipeName).click();
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
				boolean isGrandPrize = driver.findElements(By.xpath("/html/body/main/div[2]/section[1]/div/div[2]/div[3]/div[1]/ul/li[3]/i")).size() != 0;
				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
				if(isGrandPrize == false) {
					driver.navigate().back();
	//				driver.navigate().back();
					links = driver.findElements(By.className("rd_search_result_title"));
					if (links.size() > 0) {
						for(int i = 1; i <= 4; i++) {
							links.remove(1);
						}
					}
					String recipeLinks = "";
					for(WebElement e : links) {
						recipeLinks += e.getText();
						recipeLinks += ("  ");
					}	
					String[] arr = recipeLinks.split("  ");
					while(exit2 == false) {
						for(String nextRecipe: arr) {
							if(nextRecipe.equals("") || nextRecipe.equals(" ")) {
								continue;
							}
							else {
						//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
								WebDriverWait waitForLinks = new WebDriverWait(driver, 10);
								waitForLinks.until(ExpectedConditions.elementToBeClickable(By.className("rd_search_result_title")));
								driver.findElement(By.partialLinkText(nextRecipe)).click();
								boolean exists2 = driver.findElements(By.xpath("/html/body/main/div[2]/section[1]/div/div[2]/div[3]/div[1]/ul/li[3]/i")).size() != 0;
								driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
									if(exists2 == false) {
										driver.navigate().back();
							//			driver.navigate().back();
										continue;
									}
									if(exists2 == true) {
										exit2 = true;
										exit = true;
										break;
									}
							 }
						 }
					}
				}
				if(isGrandPrize == true) {
					exit = true;
				}
			}
			By recipeName = By.partialLinkText(recipe);
			if(contest.equals("No") || contest.equals("no")) {
				WebElement waitRecipe = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"searchResultsContainer\"]")));
				driver.findElement(recipeName).click();
				List<WebElement> waitForGrandPrize = new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/main/div[2]/section[1]/div/div[2]/div[3]/div[1]")));
				boolean isGrandPrize = driver.findElements(By.xpath("/html/body/main/div[2]/section[1]/div/div[2]/div[3]/div[1]/ul/li[3]/i")).size() != 0;
				System.out.println(isGrandPrize);
				if(isGrandPrize == true) {
					driver.navigate().back();
			//		driver.navigate().back();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					links = driver.findElements(By.className("rd_search_result_title"));
					for(int i = 1; i <= 4; i++) {
						links.remove(1);
					}
					String recipeLinks = "";
					for(WebElement e : links) {
						recipeLinks += e.getText();
						recipeLinks += ("  ");
					}	
					String[] arr = recipeLinks.split("  ");
					while(exit2 == false) {
						for(String nextRecipe: arr) {
							if(nextRecipe.equals("") || nextRecipe.equals(" ")) {
								continue;
							}
							else {
								WebElement waitForLinks = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.className("rd_search_result_title")));
								driver.findElement(By.partialLinkText(nextRecipe)).click();
								List<WebElement> waitRecc = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/main/div[2]/section[1]/div/div[2]/div[3]/div[1]")));
								boolean exists2 = driver.findElements(By.xpath("/html/body/main/div[2]/section[1]/div/div[2]/div[3]/div[1]/ul/li[3]/i")).size() != 0;
									if(exists2 == true) {
										driver.navigate().back();
							//			driver.navigate().back();
										continue;
									}
									if(exists2 == false) {
										exit2 = true;
										exit = true;
										break;
									}
							 }
						 }
					}
				}
				if(isGrandPrize == false) {
					exit = true;
				}
			}
		}
	}	
		
	public static void importToSQL(Connection c, WebDriver driver, List<String> ingredList, List<String> reviewList, double avgRating, String recipe) throws SQLException {
		try {
			String queryId = "SELECT RECIPE_ID\n" +
							 "FROM TOH_RECIPE\n" +
							 "WHERE RECIPE_NAME =" + "'" + recipe + "'";
			CallableStatement cs = c.prepareCall(queryId);
			ResultSet rs = cs.executeQuery();
			int Id = 0;
			while(rs.next()) {
				Id = rs.getInt(1);
			}
			for(String temp : ingredList) {
				String ingredQuery = ("INSERT INTO TOH_INGREDIENT (INGRED_ID, INGREDIENTS, RECIPE_ID)\n" + 
				   					  "VALUES (REVIEW_ID_SEQ.nextval, :ingredient, :Id)\n");
				cs = c.prepareCall(ingredQuery);
				cs.setString("ingredient", temp);
				cs.setInt("Id", Id);
				cs.executeUpdate();
			}
			for(String temp2 : reviewList) {
				String reviewQuery = ("INSERT INTO TOH_REVIEW (REVIEW_ID, REVIEW, RECIPE_ID)\n" +
						 			  "VALUES (REVIEW_ID_SEQ.nextval, :review, :Id)\n");	
				cs = c.prepareCall(reviewQuery);
				cs.setString("review", temp2);
				cs.setInt("Id", Id);
				cs.executeUpdate();
			}
			
			String ratingQuery = ("UPDATE TOH_RECIPE\n" + 
								  "SET AVG_RATING = ? \n" +
								  "WHERE RECIPE_ID = ?");
			PreparedStatement stmt = c.prepareCall(ratingQuery);
			stmt.setDouble(1, avgRating);
			stmt.setInt(2, Id);
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			logger.error("Error", e);
		}
	}
}
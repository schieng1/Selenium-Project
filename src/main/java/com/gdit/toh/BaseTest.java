package com.gdit.toh;

import java.io.File;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {
	
    protected static WebDriver driver;
    private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) throws InterruptedException {
        BaseTest page = new BaseTest();
        page.setUp();
        page.cleanUp();
    }
	
	public void setChromeDriverProperty() {
		System.setProperty("webdriver.chrome.driver", (System.getProperty("user.dir")) + "\\src\\main\\resources\\chromedriver_win32\\chromedriver.exe");
	}
	
	public WebDriver getWebDriver() {
		return this.driver;
	}
    
    public void setUp() throws InterruptedException {
    	logger.info("Initalizing chromedriver to open a page in chrome");
    	setChromeDriverProperty();
    	ChromeOptions opts = new ChromeOptions();
    	opts.setPageLoadStrategy(PageLoadStrategy.EAGER);
    	opts.addExtensions(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\Extensions\\gighmmpiobklfepjocnamgkkbiglidom\\4.14.0_0.crx"));
        driver = new ChromeDriver(opts);
        WebDriverWait wait = new WebDriverWait(driver, 10);
    	driver.get("https://www.tasteofhome.com/search/index?search=");
    	String currentWindow = "";
    	logger.info("Removing adBlock tab and switching to TOH tab");
    	for (String windowHandle : driver.getWindowHandles()) { 
			wait.until(ExpectedConditions.or(ExpectedConditions.titleContains("AdBlock is now installed!"),ExpectedConditions.titleContains("Recipes | Taste of Home")));
			String switchedWindowTitle=driver.switchTo().window(windowHandle).getTitle(); 
    	    if(switchedWindowTitle.contentEquals("AdBlock is now installed!")) {
    	    	driver.close();
    	        break;
    	    }
    	    if(switchedWindowTitle.contentEquals("Recipes | Taste of Home")) {
    	    	currentWindow = driver.getWindowHandle();
    	    }
    	}
    	driver.switchTo().window(currentWindow);
    	driver.manage().window().maximize();
    	wait.until(ExpectedConditions.elementToBeClickable(By.id("searchTextRecipe Detail")));
    	driver.findElement(By.id("searchTextRecipe Detail")).clear();
    } 
    
    public void cleanUp() {
    	logger.info("Quit the browser session");
    	driver.quit();
    }
}
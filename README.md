# Recipe Book

Recipe Book is a collection of recipes in a database acquired from the website tasteofhome.com. It is a program that utilizes selenium to obtain these recipes using a chrome webdriver. Specifically, the webdriver opens the browser to the website then it will search recipes from the database and it will click on the recipe name and acquire the average rating, reviews, and the ingredients. 

## Prerequisites

·    Apache Maven

·    Eclipse IDE (4.9)

·    JDBC

·    Oracle SQL Developer

·    TestNG

·    Selenium ChromeDriver



## Installation 

1. For the chromedriver, install the same version as your current chrome browser which can be checked by clicking the 3 little dots in the right hand side, hover over help, and click on about google chrome. 

   ![Check Chrome Version](GoogleChrome.PNG)
   
   
2. Install [Eclipse IDE 2018-019 (4.9)](https://www.eclipse.org/downloads/packages/release/2018-09/r/eclipse-ide-java-ee-developers) and it must be version 2018-2019 in order for testNG to work with it.
   
   
3. Once Eclipse is completely installed then open up Eclipse, click on help and then Eclipse Marketplace. Afterwards search up TestNG and install it. 

   ![Download TestNG plugin](TestNG.PNG)
   

4. Install the latest version of Maven and follow the installation instructions to set up any environment paths if necessary. Maven’s prior requisite is to have JDK 1.7 or above installed first.
   


5. Execute this in the pom.xml directory in the command line to build the project

   > maven build



6. Install [Oracle SQL Developer V19.2.1](https://www.oracle.com/tools/downloads/sqldev-v192-downloads.html).

   > Note: Change the credentials in config.properties to the username, password, and dbURL to access the database you're accessing.



7. Run SearchPage.java in eclipse in order to run the program! 



## Running the tests

The test class is TestNG.java

The first test called testSearchWindow is for testing the search window which means that if the search query doesn't show any results then the test will fail. 

The second test method called test Recipe is testing each of the food table, if there is any problem with obtaining that recipe then it will fail the test. 
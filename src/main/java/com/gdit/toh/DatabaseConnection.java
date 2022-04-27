package com.gdit.toh;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseConnection {

	private static Logger logger = LogManager.getLogger();
	public static List<String> recipes;
	public static Connection c;

	public DatabaseConnection() {
		this.c = oracleDatabase();
		this.recipes = loadRecipes(this.c);
	}

	public static Connection oracleDatabase() {
		Connection conn = null;
		String dbURL = "";
		String username = "";
		String password = "";
		try(InputStream input = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			Properties prop = new Properties();
			prop.load(input);
			dbURL =  prop.getProperty("db.url");
			username = prop.getProperty("db.user");
			password = prop.getProperty("db.password");
		}
		catch(IOException e) {
			logger.error("Error", e);
		}
		try {
			conn = DriverManager.getConnection(dbURL, username, password);
		}
		catch(SQLException e) {
			logger.error("SQL Error", e);
		}
		return conn;
	}

	public static List<String> loadRecipes (Connection conn) { 
		List<String> recipes = new ArrayList<String>();
		String recipe = "";
		try {
			CallableStatement cs = conn.prepareCall("SELECT * FROM TOH_RECIPE");
			ResultSet rs = cs.executeQuery();
			while(rs.next()) {
				recipe = rs.getString(2);
				recipes.add(recipe);
				recipe = "";
			}
		}
		catch(SQLException e) {
			logger.error("SQL Error", e);
		}
		return recipes;
	}
}
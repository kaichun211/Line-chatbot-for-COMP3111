package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
		try {
		Connection connection = getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT id, keyword, response FROM chatbotresponse");
		//stmt.setString(1, text); //or some other variables
		ResultSet rs = stmt.executeQuery();
		while (rs.next() && result == null) {
		System.out.println("ID: " + rs.getInt(1) + "\tKeyword: " + rs.getString(2) +"\tResponse: "+ rs.getString(3));
		 if(text.toLowerCase().equals(rs.getString(2).toLowerCase())){
			result = rs.getString(3);
			break;
			}
		}
		rs.close();
		stmt.close();
		connection.close();
		} catch (Exception e) {
		System.out.println(e);}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}

	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}

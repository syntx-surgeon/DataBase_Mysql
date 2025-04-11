package com.example.demo.newpackage.cframework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * author: Santosh Kumar Subedi
 * createdDate:3/5/25
 * projectName:demo7
 **/
public class DBManager {
	private static String dbUrl="jdbc:mysql://localhost:3306/db";
	private static String dbUsername = "user";
	private static String dbPassword = "p@ssw0rd";


	public Connection getConnection(){
		try {
			return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

package com.example.demo.newpackage.dbcon;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * author: Santosh Kumar Subedi
 * createdDate:3/4/25
 * projectName:demo7
 **/
public class DBConMain {
	private static String dbUrl="jdbc:mysql://localhost:3306/db";
	private static String dbUsername = "user";
	private static String dbPassword = "p@ssw0rd";
	public static void main(String[] args){
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Enter YOur account Number?");
//		String accountNumber = scanner.nextLine();
		try {
			try(Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)){
				String insertQuery = """ 
						insert into transaction_history(account_id, is_debit, message, amount, transaction_date)
						    VALUE (?,?,?,?,?)
						""";
				LocalDateTime localDate = LocalDateTime.now();
				try(PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
					preparedStatement.setInt(1,1);
					preparedStatement.setBoolean(2,true);
					preparedStatement.setString(3,"Debit Message");
					preparedStatement.setDouble(4,500.0);
					preparedStatement.setString(5,localDate.toString());
					var updatedRows = preparedStatement.executeUpdate();
					System.out.println(updatedRows);
				}


//				try(PreparedStatement preparedStatement = connection.prepareStatement("select * from accouts where account_number = ? and balance > ?")){
//					preparedStatement.setString(1,accountNumber);
//					preparedStatement.setDouble(2,0.0);
//					try(ResultSet resultSet = preparedStatement.executeQuery()){
//						while (resultSet.next()){
//							System.out.println(resultSet.getInt("id"));
//							System.out.println(resultSet.getString("account_number"));
//						}
//					}
//				}
			}
		} catch (SQLException e) {
			System.out.println("Failed To Connect To Database.");
			System.out.println(e.getMessage());
		}
	}
}

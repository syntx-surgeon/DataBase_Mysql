package com.example.demo.newpackage.oop.polymerphism;

import com.example.demo.newpackage.cframework.DBManager;
import com.example.demo.newpackage.cframework.TransactionHistory;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SavingAccount implements AccountTransactionService, InterestTransactionService, Serializable {
	private Integer accountId;
	private String accountNumber;
	private Double balance;
	private Double interestRate;
	private List<TransactionHistory> transactionHistories;
	private DBManager dbManager;

	public SavingAccount(String accountNumber, Double balance, Double interestRate,Integer accountId) {
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.interestRate = interestRate;
		transactionHistories = new ArrayList<>();
		dbManager = new DBManager();
		this.accountId = accountId;
	}

	@Override
	public void deposit(Double amount) {
		String updateQuery = """
				update accouts set balance = balance + ? where account_number = ?
				""";
		try {
			try (Connection connection = dbManager.getConnection()) {
				try(PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)){
					preparedStatement.setDouble(1,amount);
					preparedStatement.setString(2,this.accountNumber);
					int updatedRows = preparedStatement.executeUpdate();
					System.out.printf("Updated %s Rows%n",updatedRows);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		balance = this.balance + amount;
	}

	@Override
	public void withdraw(Double amount) {
		try{
			try (Connection connection = dbManager.getConnection()) {
			try(PreparedStatement preparedStatement = connection.prepareStatement("select * from accouts where account_number = ?")){
				preparedStatement.setString(1,accountNumber);
				try(ResultSet resultSet = preparedStatement.executeQuery()){
					if(resultSet.next()) {
						Double balance = resultSet.getDouble("balance");
						if (balance >= amount) {
							String withDrawQuery = """
								update accouts set balance = balance - ? where account_number = ?
								""";
							try(PreparedStatement updateBalanceStatement = connection.prepareStatement(withDrawQuery)){
								updateBalanceStatement.setDouble(1,amount);
								updateBalanceStatement.setString(2,this.accountNumber);
								int updatedRows = updateBalanceStatement.executeUpdate();
								System.out.printf("Updated %s Rows%n",updatedRows);
							}
							balance = balance - amount;
						} else {
							System.out.println("You don't have sufficient balance.");
						}
					}
				}
			}

		}
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
		if (balance >= amount) {
			balance = balance - amount;
		} else {
			System.out.println("You don't have sufficient balance.");
		}
	}

	@Override
	public void deposit(Double amount, String sourceOfFund) {
		this.deposit(amount);
		//Handle source of fund as required.
		System.out.printf("Customer got the money from %s%n", sourceOfFund);
	}

	@Override
	public void displayHistory() {
		System.out.println("AMOUNT               TYPE                Transaction Date                  Message");
		String selectHistoryQUery = """
				select * from transaction_history where account_id = ?
				""";
		try {
			try (Connection connection = dbManager.getConnection()) {
				try(PreparedStatement preparedStatement = connection.prepareStatement(selectHistoryQUery)){
					preparedStatement.setInt(1,accountId);
					try(ResultSet resultSet=preparedStatement.executeQuery()){
						while (resultSet.next()){
							System.out.printf("%s               %s                %s                       %s%n", resultSet.getDouble("amount"), resultSet.getBoolean("is_debit") ? "Deposite" : "Credit", resultSet.getString("transaction_date"),resultSet.getString("message"));
						}
					}
				}
			}
		}catch (SQLException exception){
			System.out.println(exception.getMessage());
		}

	}

	@Override
	public Double calculateInterest() {
		double time = 1 / 12;
		double interest = time * interestRate * balance;
		return interest;
	}

	@Override
	public void processInterest(SavingAccount account) {
		double interest = this.calculateInterest();
		account.deposit(interest);
	}

	@Override
	public void processInterest() {
		double interest = this.calculateInterest();
		this.deposit(interest);
	}


	public void displayBalance() {
		System.out.printf("Your Available Balance is : Rs. %s%n", balance);
	}

	public String getAccountNumber() {
		return this.accountNumber;
	}

	public boolean writeTransactionHistory(TransactionHistory transactionHistory) {
		try {
			try(Connection connection = dbManager.getConnection()){
				String insertQuery = """ 
						insert into transaction_history(account_id, is_debit, message, amount, transaction_date)
						    VALUE (?,?,?,?,?)
						""";
				LocalDateTime localDate = LocalDateTime.now();
				try(PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
					preparedStatement.setInt(1,this.accountId);
					preparedStatement.setBoolean(2,transactionHistory.isDeposit());
					preparedStatement.setString(3,transactionHistory.getOperationMessage());
					preparedStatement.setDouble(4,transactionHistory.getAmount());
					preparedStatement.setString(5,localDate.toString());
					var updatedRows = preparedStatement.executeUpdate();
					System.out.println(updatedRows);
				}
			}
		} catch (SQLException e) {
			System.out.println("Failed To Connect To Database.");
			System.out.println(e.getMessage());
		}
		return true;
	}
}
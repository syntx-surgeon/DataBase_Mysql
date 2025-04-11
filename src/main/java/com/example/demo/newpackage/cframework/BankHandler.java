package com.example.demo.newpackage.cframework;

import com.example.demo.newpackage.oop.polymerphism.SavingAccount;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankHandler {
	// NOTES: https://notes.io/wZYqq
	private DBManager dbManager;

	public BankHandler() {
		dbManager = new DBManager();
	}


	public void start() {
		System.out.println("!!! Welcome To Bank Of Nepal !!!");
		System.out.println("Enter Your Account Number");
		SavingAccount account = getAccount();
		while (true){
			Integer operation = validateOperations();
			boolean operationExecution = this.handleOperation(operation, account);
			//TODO Ask User if they want to continue
		}

	}

	public SavingAccount getAccount() {
		while (true) {
			Scanner scanner = new Scanner(System.in);
			String accountNumber = scanner.nextLine();
			try{
			try(Connection conenction = dbManager.getConnection()){
				try(PreparedStatement preparedStatement = conenction.prepareStatement("select * from accouts where account_number = ?")){
					preparedStatement.setString(1,accountNumber);
					try(ResultSet resultSet = preparedStatement.executeQuery()){
						if(resultSet.next()){
							Double amount = resultSet.getDouble("balance");
							Double interestRate = resultSet.getDouble("interest_rate");
							Integer id = resultSet.getInt("id");
							SavingAccount savingAccount = new SavingAccount(accountNumber,amount,interestRate,id);
							return savingAccount;

						}
					}
				}
			}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			System.out.println("Invalid Account Number");
		}
	}

	public Integer validateOperations() {
		System.out.println("Insert Operation");
		System.out.println("1.Read Balance");
		System.out.println("2.Deposit");
		System.out.println("3.Credit");
		System.out.println("4.Calculate Interest");
		System.out.println("5.Credit Transfer");
		System.out.println("6.Read History");
		while (true) {
			Scanner scanner = new Scanner(System.in);
			try {
				int operation = scanner.nextInt();
				if (operation > 0 && operation < 7) {
					return operation;
				} else {
					System.out.println("Invalid Operation Try Again");
				}
			} catch (Exception e) {
				System.out.println("Invalid Operation Try Again");
			}
		}
	}

	public boolean handleOperation(Integer operationCode, SavingAccount account) {
		switch (operationCode) {
			case 1: {
				readBalance(account);
				// Read Balance Operation
				break;
			}
			case 2: {
				// Deposit
				handleDeposit(account);
				break;
			}
			case 3: {
				handleWithdraw(account);
				break;
			}
			case 4: {
				handleInterestCalculation(account);
				// Interest
				break;
			}
			case 5: {
				handleFundTransfer(account);
				// Transfer
				break;
			}
			case 6: {
				handleAccountHistory(account);
			}
			default: {
				System.out.println("Invalid Operation");
				return false;
			}
		}
		return true;
	}

	private void handleAccountHistory(SavingAccount account) {
		account.displayHistory();
	}

	public void readBalance(SavingAccount account) {
		account.displayBalance();
	}

	public boolean handleDeposit(SavingAccount account) {
		System.out.println("Please enter amount to deposit.");
		Double amount = validateAmount();
		account.deposit(amount);
		System.out.printf("Amount Rs. %s is deposited into your account.%n", amount);
		account.displayBalance();
		TransactionHistory transactionHistory = new TransactionHistory(true,amount, "Self Deposit to Account");
		account.writeTransactionHistory(transactionHistory);
		return true;
	}

	public boolean handleWithdraw(SavingAccount account) {
		System.out.println("Please enter amount to Withdraw.");
		Double amount = validateAmount();
		account.withdraw(amount);
		System.out.printf("Amount Rs. %s is withdrawn into your account.%n", amount);
		account.displayBalance();
		TransactionHistory transactionHistory = new TransactionHistory(false,amount,"Self Withdraw from account.");
		account.writeTransactionHistory(transactionHistory);
		return true;
	}

	public void handleFundTransfer(SavingAccount account) {
		System.out.println("Please Enter Amount to be transfered.");
		Double amount = validateAmount();
		System.out.println("Please Provide Receiver Account Number.");
		SavingAccount receiverAccount = getAccount();
		account.withdraw(amount);
		receiverAccount.deposit(amount);
		TransactionHistory withdrawHistory = new TransactionHistory(false,amount,String.format("Amount Transferred to account %s",receiverAccount.getAccountNumber()));
		TransactionHistory depositHistory = new TransactionHistory(false,amount,String.format("Amount Transferred from account %s",account.getAccountNumber()));
		account.writeTransactionHistory(withdrawHistory);
		receiverAccount.writeTransactionHistory(depositHistory);
	}

	public void handleInterestCalculation(SavingAccount account){
		System.out.println("Please Provide Nominee Account Number");
		SavingAccount nomineeAccount = getAccount();
		account.processInterest(nomineeAccount);
	}

	public Double validateAmount() {
		while (true) {
			Scanner scanner = new Scanner(System.in);
			try {
				Double amount = scanner.nextDouble();
				return amount;
			} catch (Exception e) {
				System.out.println("Invalid Amount Try Again");
			}
		}
	}

}
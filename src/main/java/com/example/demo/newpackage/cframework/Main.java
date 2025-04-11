package com.example.demo.newpackage.cframework;

import com.example.demo.newpackage.oop.polymerphism.SavingAccount;

import java.util.*;

/**
 * author: Santosh Kumar Subedi
 * createdDate:2/20/25
 * projectName:demo7
 **/
public class Main {

	public static void main(String[] args) {
		BankHandler bankHandler = new BankHandler();
		bankHandler.start();
//		System.out.println("Welcome To Bank Of Nepal");
//		Map<String, SavingAccount> accounts = new HashMap<>();
//		Main main = new Main();
//		main.loadAccountInformation(accounts);
//
//		while(true){
//			Scanner scanner = new Scanner(System.in);
//			System.out.println("Insert Operation");
//			System.out.println("1.Read Balance");
//			System.out.println("2.Deposit");
//			System.out.println("3.Credit");
//			System.out.println("4.Calculate Interest");
//			System.out.println("5.Credit Transfer");
//			int operation = scanner.nextInt();
//			switch (operation){
//				case 1: {
//
//				}
//				case 2:{
//
//				}
//				case 3: {
//
//				}
//				case 4:{
//
//				}
//				case 5:{
//
//				}
//				default:{
//
//				}
//			}
//		}

	}


	public String validateAccountNumber(Map<String,SavingAccount> accounts){
		while (true) {
			System.out.println("Enter Your Account Number");
			Scanner scanner = new Scanner(System.in);
			String accountNumber = scanner.nextLine();
			if (!accounts.containsKey(accountNumber)) {
				System.out.println("Invalid Account Number");
				continue;
			}
		}

	}

}

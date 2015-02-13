package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseGenerator {

	// Date
	private static final int year = 1950;
	private static final int month = 0;
	private static final int day = 1;	
	
	// Expense expenseName
	private static final String expenseName = "Expense";
	
	// Category
	private static final String category = "Category";
	
	// Amount
	private static final String currency = "USD";
	private static final Float amount = 49.99f;
	
	// Receipt?
	private static boolean receiptYN = false;
	
	public Expense generateExpense(int i){
		
		// Date
		// 	Date's deprecated; maybe fix it later;
		Date date = new Date(year+i, month, day);

		// Description
		String expenseName = ExpenseGenerator.expenseName + " " + i;		

		// Description
		String category = ExpenseGenerator.category + " " + i;		
		
		// Amount
		String currency = ExpenseGenerator.currency;
		Float amount = ExpenseGenerator.amount * i;
		
		// Approver name
		boolean receiptYN = ExpenseGenerator.receiptYN;

		// Create claim
		Expense expense = new Expense(expenseName);
		
		// Populate expense
		expense.setExpenseName(expenseName);
		expense.setdate(date);
		expense.setCategory(category);
		expense.setCurrency(currency);
		expense.setAmount(amount);
		expense.setReciptYN(receiptYN);
		
		return expense;	
	}

	public String getExpenseName(int i) {
		return expenseName + " " + i;
	}

	public Date getDate(int i) {
		Date date = new Date(year+i, month, day);
		return date;
	}
	
	public String getCategory(int i) {
		return category + " " + i;
	}

	public String getCurrency() {
		return currency;
	}

	public Float getAmount(int i) {
		return amount * i;
	}
	
	public boolean getReceiptYN(){
		return receiptYN;
	}

}

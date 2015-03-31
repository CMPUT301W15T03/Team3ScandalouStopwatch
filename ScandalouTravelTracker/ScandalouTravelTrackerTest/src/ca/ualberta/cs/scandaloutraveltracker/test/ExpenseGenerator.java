/*

Copyright 2015 Team3ScandalouStopwatch

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.Date;

import ca.ualberta.cs.scandaloutraveltracker.models.Expense;

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
	private static final Double amount = 49.99d;
	
	// Receipt?
	//private static boolean receiptYN = false;
	
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
		Double amount = ExpenseGenerator.amount * i;
		
		// Approver name
		//boolean receiptYN = ExpenseGenerator.receiptYN;

		// Create claim
		Expense expense = new Expense();
		
		// Populate expense
		expense.setDescription(expenseName);
		expense.setDate(date);
		expense.setCategory(category);
		expense.setCurrencyType(currency);
		expense.setCost(amount);
		//expense.setReciptYN(receiptYN);
		
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

	public Double getAmount(int i) {
		return amount * i;
	}
	
	public boolean getReceiptYN(){
		// TODO: Change this later when expense has proper method
		return true;
	}

}

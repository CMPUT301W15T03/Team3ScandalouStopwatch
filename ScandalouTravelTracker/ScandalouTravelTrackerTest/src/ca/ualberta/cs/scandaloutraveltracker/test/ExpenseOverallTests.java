package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

public class ExpenseOverallTests extends TestCase {
	//use case UC 04.01.01 
	public void testCreateExpense() {
		//can change date type later since Date is deprecated
		Date date = new Date(2014,01,01);
		String category = "Air Fare";
		String description = "Flight to YEG";
		String description2 = "Flight from YEG";
		//can change type of amount later 
		double cost = 566;
		String currencyType = "CAD";
		
		Claim testClaim = new Claim();
		Expense testExpense = new Expense(date, category, description, cost, currencyType);
		Expense testExpense2 = new Expense(date, category, description2, cost, currencyType);
		testClaim.addExpenseItem(testExpense);
		assertTrue("Expense not added to claim", (testClaim.getExpenses.length() == 1));
		testClaim.addExpenseItem(testExpense2);
		assertTrue("Both expenses not added to claim", (testClaim.getExpenses.length() == 2));
		
	}
	
	//use case UC 04.02.01 
	public void testExpenseCategories() {
		List<String> categoryList = 
				Arrays.asList("Air Fare", "Ground Transport", "Vehicle Rental", 
						"Private Automobile", "Fuel", "Parking", "Registration", 
								"Accommodation", "Meal", "Supplies");
		//can change date type later since Date is deprecated
		Date date = new Date(2014,01,01);
		String category = "Air Fare";
		String description = "Flight to YEG";
		String description2 = "Flight from YEG";
		//can change type of amount later 
		double cost = 566;
		String currencyType = "CAD";
		
		Expense testExpense = new Expense(date, category, description, cost, currencyType);
		assertTrue("category not one previously defined", categoryList.contains(testExpense.getCategory()));
	}
	
	//use case UC 04.03.01 
	public void testExpenseCurrency() {
		List<String> currencyList = Arrays.asList("CAD", "USD", "EUR", "GBP", "CHF", "JPY", "CNY");
		//can change date type later since Date is deprecated
		Date date = new Date(2014,01,01);
		String category = "Air Fare";
		String description = "Flight to YEG";
		String description2 = "Flight from YEG";
		//can change type of amount later 
		double cost = 566;
		String currencyType = "CAD";
		
		Expense testExpense = new Expense(date, category, description, cost, currencyType);
		assertTrue("currency not one previously defined", currencyList.contains(testExpense.getCurrency()));
	}
	
	//use case UC 04.04.01 
	public void testExpenseFlag() {
		List<String> currencyList = Arrays.asList("CAD", "USD", "EUR", "GBP", "CHF", "JPY", "CNY");
		//can change date type later since Date is deprecated
		Date date = new Date(2014,01,01);
		String category = "Air Fare";
		String description = "Flight to YEG";
		//can change type of amount later 
		double cost = 566;
		String currencyType = "CAD";
		
		Expense testExpense = new Expense(date, category, description, cost, currencyType);
		assertTrue("Flag isn't set to unflagged automatically", "unflagged".equals(testExpense.getFlag()));
		testExpense.setFlag(true);
		assertTrue("Flag set isn't successful", "flagged".equals(testExpense.getFlag()));
		
	}
	
	//use case UC 04.05.01 - 04.05.02
	public void testExpenseDetails() {
		Date date = new Date(2014,01,01);
		String category = "Air Fare";
		String description = "Flight to YEG";
		//can change type of amount later 
		double cost = 566;
		String currencyType = "CAD";
		
		Expense testExpense = new Expense(date, category, description, cost, currencyType);
		//should return string of testExpense that will display all info for expense list
		String details = testExpense.getDetails();
		//can change format
		String actualDetails = "Air Fare - 01/01/2014" + "\nFlight to YEG" + "\n566 CAD";
		assertTrue("The details aren't being returned properly for displaying", details.equals(actualDetails));
	}
	
	//use case UC 04.06.01
	public void testExpenseEdit() {
		Date date = new Date(2014,01,01);
		String category = "Air Fare";
		String description = "Flight to YEG";
		//can change type of amount later 
		double cost = 566;
		String currencyType = "CAD";
		
		Claim testClaim = new Claim();
		Expense testExpense = new Expense(date, category, description, cost, currencyType);
		testClaim.addExpenseItem(testExpense);
		assertTrue("change state doesn't default to allowed", testClaim.getStatus().equals("ALLOWED"));
		testExpense.setDate(new Date(2014,01,02));
		testExpense.setCategory = "Fuel";
		testExpense.setDescription = "Feul for trip";
		testExpense.setCost = 40;
		testExpense.setCurrency = "USD";
		assertTrue("Date edit unsuccessful", testExpense.getDate().toString().equals("2014/01/02"));
		assertTrue("Category edit unsuccessful", testExpense.getCategory.equals("Fuel"));
		assertTrue("Description edit unsuccessful", testExpense.getDescription.equals("Feul for trip"));
		assertTrue("Cost edit unsuccessful", (testExpense.getCost() == 40);
		assertTrue("Currency edit unsuccessful", testExpense.getCurrency().equals("USD"));
	}
	
	//use case UC 04.07.01 
	public void testExpenseDelete() {
		Date date = new Date(2014,01,01);
		String category = "Air Fare";
		String description = "Flight to YEG";
		//can change type of amount later 
		double cost = 566;
		String currencyType = "CAD";
		
		Claim testClaim = new Claim();
		Expense testExpense = new Expense(date, category, description, cost, currencyType);
		testClaim.addExpenseItem(testExpense);
		assertTrue("change state doesn't default to allowed", testClaim.getStatus().equals("ALLOWED"));
		testClaim.deleteExpenseitem(0);
		assertTrue("Expense deletion unsuccessful", (testClaim.getExpenses() == 0));
	}
	
	//use case UC 04.08.01
	public void testExpenseNavigation() {
		
	}
}

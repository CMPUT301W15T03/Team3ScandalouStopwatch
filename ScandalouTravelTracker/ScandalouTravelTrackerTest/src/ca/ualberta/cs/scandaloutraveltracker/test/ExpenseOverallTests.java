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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

//JUnit tests for User Cases 4
public class ExpenseOverallTests extends TestCase {
	//Test for AddExpense
	//Test if expenses can be added to a claim successfully
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
	
	//Test for AddExpenseCategory and EditExpenseCategory
	//Test to see if expense category is one of the specified categories
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
		
		testExpense.setCategory("Fuel");
		assertTrue("category not one previously defined", categoryList.contains(testExpense.getCategory()));
		assertTrue("category not edited correctly", testExpense.getCategory().equals("Fuel"));
	}
	
	//Test for AddExpenseCurrency and EditExpenseCurrency
	//Test to see if expense currency is one of specified categories
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
		
		testExpense.setCurrency("CHF");
		assertTrue("currency not one previously defined", currencyList.contains(testExpense.getCategory()));
		assertTrue("currency not edited correctly", testExpense.getCurrency().equals("CHF"));
	}
	
	//Test for FlagExpense
	//Test to see if flag setting works correctly
	public void testExpenseFlag() {
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
	
	//Test for ListExpenseDetails and SpecificTestDetails
	//Test to see if expense details are converted directly to be displayed
	public void testExpenseDetails() {
		Date date = new Date(2014,01,01);
		String category = "Air Fare";
		String description = "Flight to YEG";
		//can change type of amount later 
		double cost = 566;
		String currencyType = "CAD";
		
		Expense testExpense = new Expense(date, category, description, cost, currencyType);
		//should return string of testExpense that will display all info for expense list
		String details = testExpense.toString();
		//can change format
		String actualDetails = "Air Fare - 01/01/2014" + "\nFlight to YEG" + "\n566 CAD";
		assertTrue("The details aren't being returned properly for displaying", details.equals(actualDetails));
	}
	
	//Test for EditExpense
	//Test if editing expense elements work correctly
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
	
	//Test for DeleteExpense
	//Test if deleting an expense from a claim works
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
	
	//Test for MinNavigationAddExpense
	//Test if navigation to add an expense is 2-3 screen/clicks
	public void testExpenseNavigation() {
		//manually test if expense add screen can be added in 2-3 screens/clicks when app starts up
		//when UI made will make intent test
	}
}

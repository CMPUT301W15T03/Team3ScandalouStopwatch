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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.ExpenseListActivity;
import ca.ualberta.cs.scandaloutraveltracker.ExpenseListAdapter;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;

public class ExpenseListTest extends
		ActivityInstrumentationTestCase2<ExpenseListActivity> {

	ExpenseListActivity expenseListActivity;
	Instrumentation instrumentation;
	ListView expenseLV;
	int newClaimId;
	
	public ExpenseListTest() {
		super(ExpenseListActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		// Create claim and inject mock intent with claimId
		Intent mockIntent = new Intent();
		mockIntent.putExtra(Constants.claimIdLabel, 0);
		setActivityIntent(mockIntent);
		
		instrumentation = getInstrumentation();
		expenseListActivity = getActivity();
		
		// Create mock claim with start and end dates 03/01/2014 - 03/03/2014
		// Mock claim also contains one expense to test with
		newClaimId = createMockClaim();
		
		expenseListActivity.finish();
		setActivity(null);
		mockIntent.putExtra(Constants.claimIdLabel, newClaimId);
		setActivityIntent(mockIntent);
		expenseListActivity = getActivity();
		
		// Views
		expenseLV = (ListView) expenseListActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.expenselistView);
	}
	
	// The test flags an unflagged expense and then unflags it. Asserts that
	// the flagged status has changed 
	// US04.04.01
	public void testFlagExpense() {
		AlertDialog currentAlert;
		ExpenseListAdapter adapter = expenseListActivity.getAdapter();
		assertFalse(adapter.getFlag());
		
		// Flagging the expense
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				expenseLV.performItemClick(expenseLV, 0, 0);
			}
		});
		currentAlert = expenseListActivity.getAlert();
		assertTrue(currentAlert.isShowing());
		
		performClick(currentAlert.getButton(DialogInterface.BUTTON_NEUTRAL));
		Expense expense = (Expense) expenseLV.getItemAtPosition(0);
		adapter = expenseListActivity.getAdapter();
		assertTrue(expense.getFlag());
		assertTrue(adapter.getFlag());
		
		// Unflagging the expense
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				expenseLV.performItemClick(expenseLV, 0, 0);
			}
		});
		currentAlert = expenseListActivity.getAlert();
		assertTrue(currentAlert.isShowing());
		
		performClick(currentAlert.getButton(DialogInterface.BUTTON_NEUTRAL));
		expense = (Expense) expenseLV.getItemAtPosition(0);
		adapter = expenseListActivity.getAdapter();
		assertFalse(expense.getFlag());
		assertFalse(adapter.getFlag());
	}
	
	private int createMockClaim() throws UserInputException {
		// Create user and add to the list
		UserListController ulc = new UserListController();
		int userId = ulc.createUser("User1");
		ulc.addUser(new User(userId));
		
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = new ArrayList<String>();
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense newExpense = new Expense();
		newExpense.setDate(createDate(2,2,2014));
		newExpense.setCurrencyType("CAD");
		newExpense.setCost(1.00);
		newExpense.setDescription("Test Expense");
		expenses.add(newExpense);
		
		// Month - Day - Year
		Date startDate = createDate(2, 1, 2014);
		Date endDate = createDate(2, 3, 2014);
		
		// Create the claim
		newClaimId = clc.createClaim("a1", startDate, endDate, "d1", destinations, 
				tagsList, status, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
		
		return newClaimId;
	}
	
	private Date createDate(int month, int day, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();
		
		return date;
	}
	
	private void performClick(final Button button) {
		try {
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {
					button.performClick();
				}
			});
		} catch (Throwable e) {
			fail();
		}
		getInstrumentation().waitForIdleSync();
	}

	/*
	// Test UC 05.01.01
	public void testExpenseDisplayed() {
	    Date date = new Date(123);
	    String cat = "Category1";
	    String des = "Description";
	    double spent = 45.23;
	    String cur = "CAD";
	    boolean complete = false;
	    boolean reciept = false;

	    ExpenseListActivity activity = startWithExpense(date, cat, des, spent,
	                                                            cur, complete, reciept);
	    
	    View allViews = activity.getWindow().getDecorView();
	    TextView categoryView = (TextView) activity.findViewById(R.id.expenseCategoryExpenseListTV);
	    TextView descripView = (TextView) activity.findViewById(R.id.expenseDescriptionTV);
	    TextView dateView = (TextView) activity.findViewById(R.id.expenseDateExpenseListTV);
	    TextView spentView = (TextView) activity.findViewById(R.id.expenseTotalsExpenseListTV);
	    TextView currencyView = (TextView) activity.findViewById(R.id.expenseTotalsExpenseListTV);
	    //TextView completeView = (TextView) activity.findViewById(R.id.);
	    TextView recieptView = (TextView) activity.findViewById(R.id.expenseReceiptIndicator);

	    ViewAsserts.assertOnScreen(allViews, (View) categoryView);
	    ViewAsserts.assertOnScreen(allViews, (View) descripView);
	    ViewAsserts.assertOnScreen(allViews, (View) dateView);
	    ViewAsserts.assertOnScreen(allViews, (View) spentView);
	    ViewAsserts.assertOnScreen(allViews, (View) currencyView);
	    //ViewAsserts.assertOnScreen(allViews, (View) completeView);
	    ViewAsserts.assertOnScreen(allViews, (View) recieptView);
	    
	}
	
	public ExpenseListActivity startWithExpense(Date date, String category, String description, Double spent,
								   String currency, boolean complete, boolean reciept)
	{
		//return (ExpenseListActivity)getActivity();
		return null;
		
	}
	*/
}

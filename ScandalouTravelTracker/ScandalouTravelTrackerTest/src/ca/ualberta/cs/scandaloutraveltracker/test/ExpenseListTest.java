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

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.ExpenseListAdapter;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.views.ExpenseListActivity;

public class ExpenseListTest extends
		ActivityInstrumentationTestCase2<ExpenseListActivity> {

	ExpenseListActivity expenseListActivity;
	Instrumentation instrumentation;
	ListView expenseLV;
	int newClaimId;
	ClaimGenerator cg;
	
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
		cg = new ClaimGenerator();
		newClaimId = cg.createMockClaim(true, true, false, false);
		
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
	
	// Tests that if the claim can be edited, then an expense can be deleted
	// US04.07.01
	public void testCanDeleteExpense() {
		AlertDialog alert;
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				expenseLV.performItemClick(expenseLV, 0, 0);
			}
		});
		
		alert = expenseListActivity.getAlert();
		
		performClick(alert.getButton(DialogInterface.BUTTON_POSITIVE));
		
		ClaimController cc = new ClaimController(new Claim(newClaimId));
		assertEquals(0, cc.getExpenseList().size());
	}
	
	// Tests that if the claim can't be edited, then an expense can't be deleted
	// US04.07.01
	public void testCantDeleteExpense() {
		// Re-create mock claim but with false for canEdit
		Intent mockIntent = new Intent();
		AlertDialog alert;
		
		try {
			newClaimId = cg.createMockClaim(false, true, false, false);
		} catch (UserInputException e) {
			fail();
		}
		
		expenseListActivity.finish();
		setActivity(null);
		mockIntent.putExtra(Constants.claimIdLabel, newClaimId);
		setActivityIntent(mockIntent);
		expenseListActivity = getActivity();
		
		// Get all the views again after closing and re-injecting activity
		expenseLV = (ListView) expenseListActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.expenselistView);
		
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				expenseLV.performItemClick(expenseLV, 0, 0);
			}
		});
		alert = expenseListActivity.getAlert();
		
		performClick(alert.getButton(DialogInterface.BUTTON_POSITIVE));
		
		ClaimController cc = new ClaimController(new Claim(newClaimId));
		assertEquals(1, cc.getExpenseList().size());
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
}

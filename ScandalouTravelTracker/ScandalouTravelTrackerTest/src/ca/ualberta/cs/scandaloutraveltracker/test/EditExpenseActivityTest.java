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

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.StateSpinner;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.views.EditExpenseActivity;

public class EditExpenseActivityTest extends
		ActivityInstrumentationTestCase2<EditExpenseActivity> {
	
	EditExpenseActivity editExpenseActivity;
	Button saveEdits;
	Instrumentation instrumentation;
	EditText description;
	EditText date;
	EditText cost;
	TextView addReceiptText;
	ImageButton imageButton;
	ImageButton deleteReceiptButton;
	StateSpinner category;
	StateSpinner currencyType;
	int newClaimId;
	ClaimGenerator cg;
	
	public EditExpenseActivityTest() {
		super(EditExpenseActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Intent mockIntent = new Intent();
		mockIntent.putExtra(Constants.claimIdLabel, 0);
		mockIntent.putExtra("expenseId", 0);
		setActivityIntent(mockIntent);
		editExpenseActivity = getActivity();
		
		cg = new ClaimGenerator();
		
		newClaimId = cg.createMockClaim(true, true, false, false);
		editExpenseActivity.finish();
		setActivity(null);
		mockIntent = new Intent();
		Long expenseId = (long) 0;
		mockIntent.putExtra(Constants.claimIdLabel, newClaimId);
		mockIntent.putExtra("expenseId", expenseId);
		setActivityIntent(mockIntent);
		
		editExpenseActivity = getActivity();
		instrumentation = getInstrumentation();
		
		description = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.description);
		date = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.date_expense);
		cost = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.amount);
		category = (StateSpinner) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.catspinner);
		currencyType = (StateSpinner) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.currencyspinner);
		saveEdits = (Button) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.edit_expense_button);
		imageButton = (ImageButton) editExpenseActivity.findViewById(R.id.edit_expense_receipt_thumbnail);
		addReceiptText = (TextView) editExpenseActivity.findViewById(R.id.edit_expense_add_receipt_text);
		deleteReceiptButton = (ImageButton) editExpenseActivity.findViewById(R.id.edit_expense_delete_receipt);
	}
	
	// Tests that all of the passed expense data is shown on the screen
	// US04.05.01
	public void testExpenseDataShown() {
		assertTrue(category.getSelectedItem().toString().equals("Parking"));
		assertTrue(cost.getText().toString().equals("2.25"));
		assertTrue(currencyType.getSelectedItem().toString().equals("CAD"));
		assertTrue(date.getText().toString().equals("03/02/2014"));
		assertTrue(description.getText().toString().equals("Test Expense"));
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Tests that the details of an expense can be opened and that none
	// of the views are inaccessable to the user
	// US 04.06.01
	public void testCanEditExpense() {
		TouchUtils.clickView(this, category);
		assertTrue(category.hasBeenOpened());
		TouchUtils.clickView(this, currencyType);
		TouchUtils.clickView(this, currencyType);
		assertTrue(currencyType.hasBeenOpened());
		TouchUtils.clickView(this, description);
		TouchUtils.clickView(this, description);
		TouchUtils.clickView(this, cost);
		TouchUtils.clickView(this, date);
		assertEquals(0, editExpenseActivity.getToastCount());
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				// Ground transport selection
				category.setSelection(2);
				// USD Curency
				currencyType.setSelection(2);
				description.setText("New description");
				cost.setText("2.10");
				date.setText("03/17/2014");
				saveEdits.performClick();	
			}
		});
		instrumentation.waitForIdleSync();
		
		Log.d("TAG", description.getText().toString());
		Log.d("TAG", date.getText().toString());
		
		assertTrue(cost.getText().toString().equals("2.10"));
		assertTrue(description.getText().toString().equals("New description"));
		assertTrue(date.getText().toString().equals("03/17/2014"));
		assertTrue(currencyType.getSelectedItem().toString().equals("USD"));
		assertTrue(category.getSelectedItem().toString().equals("Ground Transport"));
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Tests that if the claim is submitted that the expense cannot be edited and
	// that the appropriate toasts show for clicked views or that the views are 
	// actually inaccessible to the user
	// US04.06.01
	public void testCantEditExpense() {
		// Reset the activity with a claim that cant be edited
		Intent mockIntent = new Intent();
		try {
			newClaimId = cg.createMockClaim(false, true, false, false);
		} catch (UserInputException e) {
			fail();
		}
		editExpenseActivity.finish();
		setActivity(null);
		mockIntent = new Intent();
		Long expenseId = (long) 0;
		mockIntent.putExtra(Constants.claimIdLabel, newClaimId);
		mockIntent.putExtra("expenseId", expenseId);
		setActivityIntent(mockIntent);
		editExpenseActivity = getActivity();

		// Need to get all the views again since we closed the activity and reopened it
		description = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.description);
		date = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.date_expense);
		cost = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.amount);
		category = (StateSpinner) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.catspinner);
		currencyType = (StateSpinner) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.currencyspinner);
		saveEdits = (Button) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.edit_expense_button);
		
		
		TouchUtils.clickView(this, category);
		assertFalse(category.hasBeenOpened());
		TouchUtils.clickView(this, currencyType);
		TouchUtils.clickView(this, currencyType);
		assertFalse(category.hasBeenOpened());
		TouchUtils.clickView(this, description);
		TouchUtils.clickView(this, date);
		TouchUtils.clickView(this, cost);
		assertEquals(3, editExpenseActivity.getToastCount());
		assertFalse(saveEdits.isShown());
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Asserts that the buttons for taking a receipt photo is shown and
	// is clickable. Clicking on the button can't be tested as it opens
	// up the camera and the test method loses control of app
	// Same settings for the approver. Error handling is done in UI when
	// trying to push the buttons.
	// US06.01.01, US06.02.01, US06.03.01
	// US08.06.01
	public void testTakeViewDeleteReceiptPicture() {
		Menu menu = editExpenseActivity.getOptionsMenu();
		MenuItem photoItem = menu.findItem(R.id.action_take_photo);
		assertTrue(photoItem.isVisible());
		assertTrue(photoItem.isEnabled());
		assertTrue(imageButton.isShown());
		assertTrue(imageButton.isClickable());
		cg.resetState(ClaimApplication.getContext());
	}
	
}

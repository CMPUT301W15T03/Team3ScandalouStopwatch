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

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.User;
import ca.ualberta.cs.scandaloutraveltracker.views.ClaimListActivity;
import ca.ualberta.cs.scandaloutraveltracker.views.ExpenseListActivity;

public class ClaimListActivityApproverTest extends
		ActivityInstrumentationTestCase2<ClaimListActivity> {

	ClaimListActivity claimListActivity;
	int newUserId;
	int newUserId2;
	ClaimGenerator cg;
	ListView claimsListView;
	ListView expenseListView;
	Instrumentation instrumentation;
	
	public ClaimListActivityApproverTest() {
		super(ClaimListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		
		// Launch activity to get context
		Intent mockIntent = new Intent();
		mockIntent.putExtra("userId", 0);
		claimListActivity = (ClaimListActivity) getActivity();
		cg = new ClaimGenerator();
		
		// Create 2 mock users
		// newUserId will have 3 non submitted claim
		// newUserId2 will have 2 submitted claims
		newUserId = createMockUser();
		newUserId2 = createMockUser();
		mockIntent = new Intent();
		mockIntent.putExtra("userId", newUserId);
		
		cg.createClaims_Tagged(newUserId);
		Date startDate = cg.createDate(0, 14, 2015);
		Date endDate = cg.createDate(0, 15, 2015);
		cg.createSubmittedClaim(newUserId2, startDate, endDate);
		startDate = cg.createDate(0, 14, 2013);
		endDate = cg.createDate(0, 15, 2013);
		cg.createSubmittedClaim(newUserId2, startDate, endDate);
		claimListActivity.finish();
		setActivity(null);
		
		// Inject activity with mock intent
		setActivityIntent(mockIntent);
		claimListActivity = (ClaimListActivity) getActivity();
		
		claimsListView = (ListView) claimListActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListActivityList);
		
		instrumentation = getInstrumentation();
	}
	
	// Starts on the user with 3 non-submitted claim and asserts that the 3 claims
	// are shown. We then switch modes to approver mode and assert that the listview
	// has been updated and has the 2 submitted claims for the other user.
	// US08.01.01
	public void testViewSubmittedClaims() {
		// Assert correct user's claims being shown
		assertEquals(3, claimsListView.getChildCount());
		switchToApproverMode();
		assertEquals(2, claimsListView.getChildCount());
		cg.resetState(ClaimApplication.getContext());
	}
	
	// The submitted claims that the approver sees should be in order from the oldest
	// submitted claim to the newest submitted claim (bottom)
	// US08.02.01
	public void testSubmittedClaimsOrder() {
		switchToApproverMode();
		Claim firstClaim = (Claim) claimsListView.getItemAtPosition(0);
		Claim secondClaim = (Claim) claimsListView.getItemAtPosition(1);
		Date fcd = firstClaim.getStartDate();
		Date scd = secondClaim.getStartDate();
		
		// compareto returns less than 0 if fcd is before scd
		assertTrue(fcd.compareTo(scd) < 0);
		cg.resetState(ClaimApplication.getContext());
	}
	
	
	// Switches the user mode to approver, and then checks if the first submitted claim
	// in the list has the correct information being shown.
	// US08.03.01
	public void testApproverViewDetails() {
		switchToApproverMode();
		
		View claimView = claimsListView.getChildAt(0);
		assertTrue(claimView.isShown());
		TextView claimUpperDateTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListUpperDateTV);
		TextView claimLowerDateTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListLowerDateTV);
		TextView claimDestinationTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListDestinationsTV);
		TextView claimStatusTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListStatusTV);
		TextView claimTotalTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListTotalsTV);
		TextView claimTagsTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListTagsTV);
		
		assertTrue(claimUpperDateTV.getText().toString().equals("1/14/2013 - "));
		assertTrue(claimLowerDateTV.getText().toString().equals("1/15/2013    "));
		assertTrue(claimDestinationTV.getText().toString().equals("Brooklyn"));
		assertTrue(claimStatusTV.getText().toString().equals("Status: Submitted"));
		assertTrue(claimTotalTV.getText().toString().equals("USD 5.00"));
		assertTrue(claimTagsTV.getText().toString().equals(" #NY"));
		
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Switches user to approver mode and then clicks the view expenses belonging
	// to the first claim. Asserts that the correct activity is shown and that expense
	// information is also shown
	// US08.04.01
	public void testViewExpenseInformation() {
		switchToApproverMode();
		
		// Registers next activity to be monitored
		ActivityMonitor am = getInstrumentation().addMonitor(ExpenseListActivity.class.getName(), null, false);
		
		// Run a click on listview in current activity
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				claimsListView.performItemClick(claimsListView, 0, 0);
			}
		});
		
		AlertDialog claimAlert = claimListActivity.getClaimOptionsDialog();
		final ListView claimOptions = claimAlert.getListView();
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claimOptions.performItemClick(claimOptions, 1, 0);
			}
		});
		getInstrumentation().waitForIdleSync();

		// Test that next activity was launched
		ExpenseListActivity nextActivity = (ExpenseListActivity) getInstrumentation().waitForMonitorWithTimeout(am, 2500);
		assertNotNull(nextActivity);
		
		expenseListView = (ListView) nextActivity.findViewById(R.id.expenselistView);
		assertTrue(expenseListView.isShown());
		View expenseDetails = expenseListView.getChildAt(0);
		getInstrumentation().waitForIdleSync();
		
		TextView expenseCategory = (TextView) expenseDetails.findViewById(R.id.expenseCategoryExpenseListTV); 
		TextView expenseDate = (TextView) expenseDetails.findViewById(R.id.expenseDateExpenseListTV);
		TextView expenseDescription = (TextView) expenseDetails.findViewById(R.id.expenseDescriptionTV);
		TextView expenseTotal = (TextView) expenseDetails.findViewById(R.id.expenseTotalsExpenseListTV);
		ImageView expenseLocation = (ImageView) expenseDetails.findViewById(R.id.expenseLocationIcon);
		ImageView expenseReceipt = (ImageView) expenseDetails.findViewById(R.id.expensePictureIcon);
		ImageView expenseFlag = (ImageView) expenseDetails.findViewById(R.id.expenseFlagIcon);
		
		assertTrue(expenseDate.getText().toString().equals("01/14/2013"));
		assertTrue(expenseCategory.getText().toString().equals("Parking"));
		assertTrue(expenseDescription.getText().toString().equals("Parking is hella expensive"));
		assertTrue(expenseTotal.getText().toString().equals("Cost: 5.00 USD"));
		assertTrue(expenseLocation.isShown());
		assertTrue(expenseReceipt.isShown());
		assertTrue(expenseFlag.isShown());
		
		nextActivity.finish();
		cg.resetState(ClaimApplication.getContext());
	}
	
	protected void performClick(final Button button) {
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
	
	private void switchToApproverMode() {
		// Click the menu option
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(claimListActivity, 
												  ca.ualberta.cs.scandaloutraveltracker.R.id.action_role, 0);
		AlertDialog userMode = claimListActivity.getUserModeDialog();
		assertTrue(userMode.isShowing());
		
		final ListView choices = userMode.getListView();
		
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				choices.performItemClick(choices, 1, 0);
			}
		});
		
		performClick(userMode.getButton(DialogInterface.BUTTON_NEGATIVE));
		getInstrumentation().waitForIdleSync();
	}
	
	private int createMockUser() {
		// Create mock user
		UserListController userListController = new UserListController();
		int newUserId = userListController.createUser("Test User");
		UserController uc = new UserController(new User(newUserId));
		Location l1 = new Location("Mock Location");
		l1.setLatitude(20);
		l1.setLongitude(-20);
		uc.setCurrentLocation(l1);
		
		return newUserId;
	}

}

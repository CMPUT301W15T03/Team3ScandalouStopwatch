package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.views.ClaimListActivity;
import ca.ualberta.cs.scandaloutraveltracker.views.NewExpenseActivity;

public class ClaimListActivityTest extends
		ActivityInstrumentationTestCase2<ClaimListActivity> {

	ClaimListActivity claimListActivity; 
	Instrumentation instrumentation;
	ListView claimsListView; 
	int newUserId;
	int totalClicks;
	ClaimGenerator cg;

	public ClaimListActivityTest() {
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
		
		// Create mock user
		UserListController userListController = new UserListController();
		newUserId = userListController.createUser("Test User");
		mockIntent = new Intent();
		mockIntent.putExtra("userId", newUserId);
		
		// Create 4 Claims with a total of 5 different tags
		// Also create 1 submitted claim
		// The list will have the first claim as submitted and the rest as in progress
		cg = new ClaimGenerator();
		cg.createClaims_Tagged(newUserId);
		Date startDate = cg.createDate(0, 14, 2015);
		Date endDate = cg.createDate(0, 15, 2015);
		cg.createSubmittedClaim(newUserId, startDate, endDate);
		claimListActivity.finish();
		setActivity(null);
		
		// Inject activity with mock intent
		setActivityIntent(mockIntent);
		claimListActivity = (ClaimListActivity) getActivity();
		
		claimsListView = (ListView) claimListActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListActivityList);
		
		instrumentation = getInstrumentation();
	} 
	
	// Adds three claims with a total of 5 tags and selects tag1 and tag2
	// to filter. The final list has 2 of the 3 claidims. Finally, the last
	// assert is to ensure the claim list can be restored to it's original state.
	// US03.03.01
	public void testFilterClaims() {
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(claimListActivity, 
				ca.ualberta.cs.scandaloutraveltracker.R.id.action_filter_claims, 0);
		getInstrumentation().waitForIdleSync();
		
		AlertDialog alert = claimListActivity.getTagDialog();
		
		// Assert the alert is showing and has 6 tags to choose from
		assertTrue(alert.isShowing());
		final ListView lv = alert.getListView();
		assertEquals(6, lv.getCount());
		
		// Selects Tag1 and Tag2 from the list
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				lv.performItemClick(lv, 0, 0);
				lv.performItemClick(lv, 1, 0);
			}

		});
		
		ArrayList<String> selectedTags = claimListActivity.getSelectedTags();
		assertEquals(2, selectedTags.size());
		
		// Click search
		try {
			performClick((alert.getButton(DialogInterface.BUTTON_POSITIVE)));
		} catch (Throwable e) {
			new Throwable(e);
		}
		
		assertEquals(2, claimsListView.getCount());

		// Test will now check original list can be restored
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(claimListActivity, 
				ca.ualberta.cs.scandaloutraveltracker.R.id.action_restore_claims, 0);
		getInstrumentation().waitForIdleSync();
		
		assertEquals(4, claimsListView.getCount());
		
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Tests that you can't delete a claim that has been submitted
	// US01.04.01
	public void testCantDeleteClaim() {
		// Select first claim in list
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claimsListView.performItemClick(claimsListView, 0, 0);
			}
		});
		getInstrumentation().waitForIdleSync();
		
		AlertDialog claimAlert = claimListActivity.getClaimOptionsDialog();
		final ListView claimOptions = claimAlert.getListView();
		
		// Click on the delete option
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claimOptions.performItemClick(claimOptions, 3, 0);
			}
		});
		getInstrumentation().waitForIdleSync();
		
		final AlertDialog deleteAlert = claimListActivity.getDeleteDialog();
		
		// Try to delete claim
		try {
			performClick(deleteAlert.getButton(DialogInterface.BUTTON_NEGATIVE));
		} catch (Throwable e) {
			new Throwable(e);
		}
		getInstrumentation().waitForIdleSync();
		
		// Assert listview size has not decreased by 1
		assertEquals(4, claimsListView.getCount());
		
		cg.resetState(ClaimApplication.getContext());
	}	
	
	// Tests that you can delete a claim that has not been submitted
	// US01.05.01
	public void testCanDeleteClaim() {
		// Select second claim in list
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claimsListView.performItemClick(claimsListView, 1, 1);
			}
		});
		getInstrumentation().waitForIdleSync();
		
		// Assert that the claim options alert is showing
		AlertDialog claimAlert = claimListActivity.getClaimOptionsDialog();
		assertTrue(claimAlert.isShowing());
		final ListView claimOptions = claimAlert.getListView();
		
		// Click on the delete option
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claimOptions.performItemClick(claimOptions, 3, 0);
			}
		});
		getInstrumentation().waitForIdleSync();
		
		// Assert that the delete warning is showing
		final AlertDialog deleteAlert = claimListActivity.getDeleteDialog();
		assertTrue(deleteAlert.isShowing());
		
		// Try to delete claim
		try {
			performClick(deleteAlert.getButton(DialogInterface.BUTTON_NEGATIVE));
		} catch (Throwable e) {
			new Throwable(e);
		}
		getInstrumentation().waitForIdleSync();
		
		// Assert listview size has decreased by 1
		assertEquals(3, claimsListView.getCount());
		
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Tests that a claim in the listview has the proper data shown
	// US02.01.01
	public void testClaimInformationShown() {
		View claimView = claimsListView.getChildAt(0);
		assertTrue(claimView.isShown());
		TextView claimDateTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListDateTV);
		TextView claimDestinationTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListDestinationsTV);
		TextView claimStatusTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListStatusTV);
		TextView claimTotalTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListTotalsTV);
		TextView claimTagsTV = (TextView) claimView.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListTagsTV);
		
		assertTrue(claimDateTV.getText().toString().equals("1/14/2015 - 1/15/2015"));
		assertTrue(claimDestinationTV.getText().toString().equals("Destinations: Brooklyn"));
		assertTrue(claimStatusTV.getText().toString().equals("Status: Submitted"));
		assertTrue(claimTotalTV.getText().toString().equals("USD 5.00"));
		assertTrue(claimTagsTV.getText().toString().equals(" #NY"));
		
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Tests that the claims are sorted from most recent claim to the oldest claim
	// US02.02.01
	public void testClaimsSorted() {
		Claim currentClaim = null;
		Claim previousClaim = null;
		
		for (int i = 0; i < claimsListView.getChildCount(); i++) {
			if (currentClaim == null) {
				currentClaim = (Claim) claimsListView.getItemAtPosition(0);
			} else {
				currentClaim = (Claim) claimsListView.getItemAtPosition(i);
				previousClaim = (Claim) claimsListView.getItemAtPosition(i-1);
			
				// If using currentClaimStartDate.compareTo(previousClaimStartDate), then compareTo
				// will return less than 0 if the currentClaim is before the previous claim
				assertTrue(currentClaim.getStartDate().compareTo(previousClaim.getStartDate()) < 0);
			}
		}
		
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Want the entry of an expense to have minimal required navigation
	// Tests that the NewExpenseActivity is opened within 2 clicks of logging in
	// US04.08.01
	public void testMinimalClicksToCreateExpense() {
		AlertDialog alert;
		final ListView claimOptions;
		totalClicks = 0;
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claimsListView.performItemClick(claimsListView, 1, 1);
				totalClicks++;
			}
		});
		getInstrumentation().waitForIdleSync();
		alert = claimListActivity.getClaimOptionsDialog();
		claimOptions = alert.getListView();
		
		// Registers next activity to be monitored
		ActivityMonitor am = getInstrumentation().addMonitor(NewExpenseActivity.class.getName(), null, false);
		
		// Add expense option
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claimOptions.performItemClick(claimOptions, 2, 0);
				totalClicks++;
			}
		});		

		// Test that next activity was launched
		NewExpenseActivity nextActivity = (NewExpenseActivity) getInstrumentation().waitForMonitorWithTimeout(am, 10000);
		assertNotNull(nextActivity);
		assertEquals(2, totalClicks);
		
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

}

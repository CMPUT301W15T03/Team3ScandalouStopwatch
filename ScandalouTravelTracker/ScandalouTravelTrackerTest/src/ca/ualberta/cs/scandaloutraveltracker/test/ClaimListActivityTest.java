package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListActivity;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;

public class ClaimListActivityTest extends
		ActivityInstrumentationTestCase2<ClaimListActivity> {
	
	ClaimListActivity claimListActivity; 
	Instrumentation instrumentation;
	ListView claimsListView; 
	int newUserId;

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
		claimListActivity = getActivity();
		
		// Create mock user
		UserListController userListController = new UserListController();
		newUserId = userListController.createUser("Test User");
		mockIntent = new Intent();
		mockIntent.putExtra("userId", newUserId);
		
		// Create 4 Claims with a total of 5 different tags
		// Also create 1 submitted claim
		// The list will have the first claim as submitted and the rest as in progress
		createClaims_Tagged(newUserId);
		Date startDate = createDate(0, 14, 2015);
		Date endDate = createDate(0, 15, 2015);
		createSubmittedClaim(newUserId, startDate, endDate);
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
	}
	
	// Tests that you can delete a claim that has not been submitted
	// US01.05.01
	public void testDeletingClaim() {
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
		assertTrue(claimTotalTV.getText().toString().equals("GBP 5.00"));
		assertTrue(claimTagsTV.getText().toString().equals(" #NY"));
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
		
	}
	
	private void createClaimWithTags(int userId, ArrayList<String> tags, Date startDate, Date endDate) throws UserInputException {
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		ArrayList<String> tagsList = tags;
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		// Create the claim
		int newClaimId = clc.createClaim("a1", startDate, endDate, "d1", destinations, 
				tagsList, Constants.statusInProgress, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
	}
	
	private void createSubmittedClaim(int userId, Date startDate, Date endDate) {
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		ArrayList<String> tagsList = new ArrayList<String>();
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		int newClaimId = 0;
		tagsList.add("");
		tagsList.add("#NY");
		destinations.add(new Destination("Brooklyn", "Meet up with Jay"));
		Expense newExpense = new Expense();
		newExpense.setCurrencyType("GBP");
		newExpense.setCost(5.00);
		expenses.add(newExpense);
		
		// Create the claim
		try {
			newClaimId = clc.createClaim("a1", startDate, endDate, "d1", destinations, 
					tagsList, Constants.statusSubmitted, false, expenses, new User(userId));
		} catch (UserInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
	}
	
	private void createClaims_Tagged(int newUserId) throws UserInputException {
		Date startDate;
		Date endDate;
		
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("#tag1");
		tags.add("#tag2");
		startDate = createDate(0, 1, 2014);
		endDate= createDate(0, 2, 2014);
		createClaimWithTags(newUserId, tags, startDate, endDate);
		
		tags = new ArrayList<String>();
		tags.add("#tag1");
		tags.add("#tag3");
		tags.add("#tag4");
		startDate = createDate(0, 1, 2014);
		endDate= createDate(0, 2, 2014);
		createClaimWithTags(newUserId, tags, startDate, endDate);
		
		tags = new ArrayList<String>();
		tags.add("#tag5");
		startDate = createDate(0, 1, 2014);
		endDate= createDate(0, 2, 2014);
		createClaimWithTags(newUserId, tags, startDate, endDate);
	}
	
	private void performClick(final Button button) throws Throwable {
		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				button.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
	}
	
	private Date createDate(int month, int day, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();
		
		return date;
	}
}

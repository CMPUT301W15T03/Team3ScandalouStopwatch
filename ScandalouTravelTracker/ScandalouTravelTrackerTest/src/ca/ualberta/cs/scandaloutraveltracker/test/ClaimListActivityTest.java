package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;
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

	public ClaimListActivityTest() {
		super(ClaimListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		// Create mock user
		UserListController userListController = new UserListController();
		int newUserId = userListController.createUser("Test User");
		Intent mockIntent = new Intent();
		mockIntent.putExtra("userId", newUserId);
		
		// Create 3 Claims with a total of 5 different tags
		createClaims_Tagged(newUserId);
		
		// Inject activity with mock intent
		setActivityIntent(mockIntent);
		claimListActivity = (ClaimListActivity) getActivity();
		
		claimsListView = (ListView) claimListActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListActivityList);
		
		instrumentation = getInstrumentation();
		
	} 
	
	// Adds three claims with a total of 5 tags and selects tag1 and tag2
	// to filter. The final list has 2 of the 3 claims. Finally, the last
	// assert is to ensure the claim list can be restored to it's original state.
	public void testFilterClaims() {
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(claimListActivity, 
				ca.ualberta.cs.scandaloutraveltracker.R.id.action_filter_claims, 0);
		getInstrumentation().waitForIdleSync();
		
		AlertDialog alert = claimListActivity.getTagDialog();
		
		// Assert the alert is showing and has 5 tags to choose from
		assertTrue(alert.isShowing());
		final ListView lv = alert.getListView();
		assertEquals(5, lv.getCount());
		
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
		
		assertEquals(3, claimsListView.getCount());
	}
	
	private void createClaimWithTags(int userId, ArrayList<String> tags) throws UserInputException {
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = tags;
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		// Create the claim
		int newClaimId = clc.createClaim("a1", new Date(), new Date(), "d1", destinations, 
				tagsList, status, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
	}
	
	private void createClaims_Tagged(int newUserId) throws UserInputException {
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("#tag1");
		tags.add("#tag2");
		createClaimWithTags(newUserId, tags);
		
		tags = new ArrayList<String>();
		tags.add("#tag1");
		tags.add("#tag3");
		tags.add("#tag4");
		createClaimWithTags(newUserId, tags);
		
		tags = new ArrayList<String>();
		tags.add("#tag5");
		createClaimWithTags(newUserId, tags);
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
}

package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListActivity;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;

public class ClaimListActivityTest extends
		ActivityInstrumentationTestCase2<ClaimListActivity> {
	
	ClaimListActivity claimListActivity; 

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
		
	} 
	
	public void testFilterClaims() {
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(claimListActivity, 
				ca.ualberta.cs.scandaloutraveltracker.R.id.action_filter_claims, 0);
		getInstrumentation().waitForIdleSync();
		
		AlertDialog alert = claimListActivity.getTagDialog();
		ArrayList<String> tags = claimListActivity.getAllTagsList();
		
		assertTrue(alert.isShowing());
		assertEquals(5, tags.size());
	}
	
	public void createClaimWithTags(int userId, ArrayList<String> tags) {
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
	
	public void createClaims_Tagged(int newUserId) {
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
}

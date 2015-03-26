package ca.ualberta.cs.scandaloutraveltracker.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListActivity;
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
		
		// Inject activity with mock intent
		setActivityIntent(mockIntent);
		claimListActivity = (ClaimListActivity) getActivity();
		
	} 
	
	public void testFilterClaims() {
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(claimListActivity, 
				ca.ualberta.cs.scandaloutraveltracker.R.id.action_view_all_tags, 0);
		
		AlertDialog alert = claimListActivity.getTagDialog();
		
		assertTrue(alert.isShowing());
	}
}

package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.User;
import ca.ualberta.cs.scandaloutraveltracker.views.ClaimListActivity;

public class ClaimListActivityApproverTest extends
		ActivityInstrumentationTestCase2<ClaimListActivity> {

	ClaimListActivity claimListActivity;
	int newUserId;
	int newUserId2;
	ClaimGenerator cg;
	ListView claimsListView;
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

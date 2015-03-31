package ca.ualberta.cs.scandaloutraveltracker.test;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.ClaimList;
import ca.ualberta.cs.scandaloutraveltracker.models.User;
import ca.ualberta.cs.scandaloutraveltracker.views.ClaimListActivity;
import ca.ualberta.cs.scandaloutraveltracker.views.UserSelectActivity;

public class UserSelectActivityTest extends ActivityInstrumentationTestCase2<UserSelectActivity> {
	UserSelectActivity userSelectActivity;
	Button newUserButton;
	ListView usersLV;
	EditText userNameET;
	UserListController ulc;
	UserController uc;
	Instrumentation instrumentation;
	User testUser;
	ClaimGenerator cg;
	
	
	public UserSelectActivityTest() {
		super(UserSelectActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		 super.setUp();
		 setActivityInitialTouchMode(true);
		 
		 // Get activity
		 userSelectActivity = getActivity();
		 
		 instrumentation = getInstrumentation();
		 ulc = new UserListController();
		 cg = new ClaimGenerator();

		 // Get UI components
		 newUserButton = (Button) userSelectActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.userSelectCreateUserButton);
		 usersLV = (ListView) userSelectActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.userSelectUsersLV);
	
		 cg.clearUL();
	}
	
	public void testAddUserButton() {
		final View decorView = userSelectActivity.getWindow().getDecorView();
		
		// Assert button is actually on screen
		ViewAsserts.assertOnScreen(decorView, newUserButton);
		
		// Assert that the layout parmeters of the button are not null
		final ViewGroup.LayoutParams layoutParams = newUserButton.getLayoutParams();
		assertNotNull(layoutParams);
		cg.resetState(ClaimApplication.getContext());
	}
	
	// http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium 03/23/2015
	public void testSelectUser() {
		// Add user for test to select
		ulc = new UserListController();
		int newUserId = ulc.createUser("New User");
		ulc.addUser(new User(newUserId));
		
		// Registers next activity to be monitored
		ActivityMonitor am = getInstrumentation().addMonitor(ClaimListActivity.class.getName(), null, false);
		
		// Run a click on listview in current activity
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				usersLV.performItemClick(usersLV, 0, 0);
			}
		});
		
		userSelectActivity.onDialogPositiveClick(userSelectActivity.getUserDialog());

		// Test that next activity was launched
		ClaimListActivity nextActivity = (ClaimListActivity) getInstrumentation().waitForMonitorWithTimeout(am, 10000);
		assertNotNull(nextActivity);
		
		// Test that the user in application is set to the new user
		ClaimApplication app = (ClaimApplication) userSelectActivity.getApplicationContext();
		User currentUser = app.getUser();
		nextActivity.finish();
		assertEquals("New User", currentUser.getName());
		cg.resetState(ClaimApplication.getContext());
	}
	
	public void testAddingUser() {		
		TouchUtils.clickView(this, newUserButton);
		AlertDialog dialog = userSelectActivity.getDialog();
		assertTrue(dialog.isShowing());
		assertEquals(0, ulc.getUserList().getCount());
		
		// Type in the AlertDialog EditText: "New User"
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync("New User");
		getInstrumentation().waitForIdleSync();
		
		// Try to press the add new user button
		try {
			performClick(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
		} catch (Throwable e) {
			new Throwable(e);
		}

		// Assert that user list size increased
		assertEquals(1, ulc.getUserList().getCount());
		
		// Assert that user in list has the same name as one entered
		String name = ulc.getUserList().getUser(0).getName();
		assertEquals(name, "New User");
		cg.resetState(ClaimApplication.getContext());
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				userSelectActivity.update();
			}
			
		});
	}
	
	// The user has a default userLocationSet which is where they last were
	// This location is set when the user is created. Need to send the location
	// first if using the emulator before running the app.
	// US01.07.01
	public void testUserLocationSet() {
		TouchUtils.clickView(this, newUserButton);
		AlertDialog dialog = userSelectActivity.getDialog();
		assertTrue(dialog.isShowing());
		assertEquals(0, ulc.getUserList().getCount());
		
		// Type in the AlertDialog EditText: "New User"
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync("New User");
		getInstrumentation().waitForIdleSync();
		
		// Try to press the add new user button
		try {
			performClick(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
			usersLV.getChildAt(0).performLongClick();
		} catch (Throwable e) {
			new Throwable(e);
		}
		getInstrumentation().waitForIdleSync();
		
		final ContextMenu contextMenu = userSelectActivity.getContextMenu();
		assertTrue(contextMenu.hasVisibleItems());
		
		// Click on add user location 
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				contextMenu.performIdentifierAction(ca.ualberta.cs.scandaloutraveltracker.R.id.user_context_add_location_gps, 0);
			}
			
		});
		
		// Get only user in list
		User user = ulc.getUser(0);
		
		// Get the location
		Location location = userSelectActivity.getLocation();
		
		assertEquals(location.getLongitude(), user.getHomeLocation().getLongitude());
		assertEquals(location.getLatitude(), user.getHomeLocation().getLatitude());
		usersLV.getChildAt(0).performLongClick();
		cg.resetState(ClaimApplication.getContext());
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				userSelectActivity.update();
			}
			
		});
	}
	
	public void testIsUsersClaims() throws UserInputException {		
		// Create two users and add them to the list
		cg.makeTwoUsersWithClaims();
		assertEquals(2, ulc.getCount());
		
		// Run a click on listview in current activity
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				usersLV.performItemClick(usersLV, 0, 0);
			}
		});
		
		userSelectActivity.onDialogPositiveClick(userSelectActivity.getUserDialog());
		
		// Registers next activity to be monitored
		ActivityMonitor am = getInstrumentation().addMonitor(ClaimListActivity.class.getName(), null, false);
		
		// Test that next activity was launched
		ClaimListActivity nextActivity = (ClaimListActivity) getInstrumentation().waitForMonitorWithTimeout(am, 10000);
		ClaimList currentClaimList = nextActivity.getCurrentClaimList();
		nextActivity.update();
		nextActivity.finish();
		
		Claim currentClaim = currentClaimList.getClaim(0);
		assertTrue(currentClaim.getUser().getName().equals("User1"));
		assertEquals(1, currentClaimList.getCount());
		
		cg.resetState(ClaimApplication.getContext());
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				userSelectActivity.update();
			}
		});
		getInstrumentation().waitForIdleSync();
	}
	
	// http://stackoverflow.com/questions/17526005/how-to-test-an-alertdialog-in-android 03/23/2015
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

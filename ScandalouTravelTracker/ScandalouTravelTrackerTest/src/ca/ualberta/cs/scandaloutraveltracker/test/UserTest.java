package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.ClaimList;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListActivity;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserController;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.UserSelectActivity;

public class UserTest extends ActivityInstrumentationTestCase2<UserSelectActivity> {
	UserSelectActivity userSelectActivity;
	Button newUserButton;
	ListView usersLV;
	EditText userNameET;
	UserListController ulc;
	UserController uc;
	Instrumentation instrumentation;
	User testUser;
	
	public UserTest() {
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

		 // Get UI components
		 newUserButton = (Button) userSelectActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.userSelectCreateUserButton);
		 usersLV = (ListView) userSelectActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.userSelectUsersLV);
		 
		 // Start with empty user and claim list
		 clearUL();
		 clearCL();
	}
	
	public void testAddUserButton() {
		final View decorView = userSelectActivity.getWindow().getDecorView();
		
		// Assert button is actually on screen
		ViewAsserts.assertOnScreen(decorView, newUserButton);
		
		// Assert that the layout parmeters of the button are not null
		final ViewGroup.LayoutParams layoutParams = newUserButton.getLayoutParams();
		assertNotNull(layoutParams);
	}
	
	// http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium 03/23/2015
	public void testSelectUser() {
		// Start with fresh list
		clearUL();
		
		// Add user for test to select
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

		// Test that next activity was launched
		ClaimListActivity nextActivity = (ClaimListActivity) getInstrumentation().waitForMonitorWithTimeout(am, 10000);
		assertNotNull(nextActivity);
		
		// Test that the user in application is set to the new user
		ClaimApplication app = (ClaimApplication) userSelectActivity.getApplicationContext();
		User currentUser = app.getUser();
		assertEquals("New User", currentUser.getName());
		
		nextActivity.finish();
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
	}
	
	public void testIsUsersClaims() {
		 // Start with empty list
		 clearUL();
		 
		 // New userSelectActivity
		 userSelectActivity = getActivity();
		
		// Create two users and add them to the list
		makeTwoUsersWithClaims();
		assertEquals(2, ulc.getCount());
		
		// Run a click on listview in current activity
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				usersLV.performItemClick(usersLV, 0, 0);
			}
		});
		
		// Registers next activity to be monitored
		ActivityMonitor am = getInstrumentation().addMonitor(ClaimListActivity.class.getName(), null, false);
		
		// Test that next activity was launched
		ClaimListActivity nextActivity = (ClaimListActivity) getInstrumentation().waitForMonitorWithTimeout(am, 10000);
		ClaimList currentClaimList = nextActivity.getCurrentClaimList();
		nextActivity.update();
		nextActivity.finish();
		
		Claim currentClaim = currentClaimList.getClaim(0);
		assertEquals("a1", currentClaim.getName());
		assertEquals(1, currentClaimList.getCount());
	}
	
	public void testCanDeleteUser() {
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				
			}
			
		});
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
	
	// User to start the tests with an empty claim list
	private void clearCL() {
		ClaimListController clc = new ClaimListController();
		ArrayList<Claim> claims = clc.getClaimList().getClaims();
		Iterator<Claim> iterator = claims.iterator();
		
		while (iterator.hasNext()) {
			Claim currentClaim = iterator.next();
			int id = currentClaim.getId();
			clc.deleteClaim(id);
			iterator.remove();
		}
	}
	
	// Used to start the tests with an empty user list
	private void clearUL() {
		// Initialize UserListController
		ulc = new UserListController();
		ArrayList<User> users = ulc.getUserList().getUsers();
		Iterator<User> iterator = users.iterator();
		
		while (iterator.hasNext()) {
			User user = iterator.next();
			int id = user.getId();
			ulc.deleteUser(id);
			iterator.remove();
		}
	}
	
	// Used to create two users and have one claim associated with each
	private void makeTwoUsersWithClaims() {
		// Create two users and add them to the list
		int userId = ulc.createUser("User1");
		ulc.addUser(new User(userId));
		int userId2 = ulc.createUser("User2");
		ulc.addUser(new User(userId2));
		
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = new ArrayList<String>();
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		// Create the claim
		int newClaimId = clc.createClaim("a1", new Date(), new Date(), "d1", destinations, 
				tagsList, status, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
		
		// Create another ClaimList associated with user2
		newClaimId = clc.createClaim("a2", new Date(), new Date(), "d2", destinations, 
				tagsList, status, canEdit, expenses, new User(userId2));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
	}
	
}

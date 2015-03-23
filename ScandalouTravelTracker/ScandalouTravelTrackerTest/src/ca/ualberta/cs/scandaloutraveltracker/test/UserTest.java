package ca.ualberta.cs.scandaloutraveltracker.test;

import android.app.AlertDialog;
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
import ca.ualberta.cs.scandaloutraveltracker.ClaimListActivity;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserController;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.UserMapper;
import ca.ualberta.cs.scandaloutraveltracker.UserSelectActivity;

public class UserTest extends ActivityInstrumentationTestCase2<UserSelectActivity> {
	UserSelectActivity userSelectActivity;
	Button newUserButton;
	ListView usersLV;
	EditText userNameET;
	UserListController ulc;
	UserController uc;
	
	
	public UserTest() {
		super(UserSelectActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		 super.setUp();
		 setActivityInitialTouchMode(true);
		 
		 // Get activity
		 userSelectActivity = getActivity();
		 
		 // Get UI components
		 newUserButton = (Button) userSelectActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.userSelectCreateUserButton);
		 usersLV = (ListView) userSelectActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.userSelectUsersLV);
		 
		 // Start with empty list
		 clearUL();
	}
	
	public void testAddUserButton() {
		final View decorView = userSelectActivity.getWindow().getDecorView();
		
		// Assert button is actually on screen
		ViewAsserts.assertOnScreen(decorView, newUserButton);
		
		// Assert that the layout parmeters of the button are not null
		final ViewGroup.LayoutParams layoutParams = newUserButton.getLayoutParams();
		assertNotNull(layoutParams);
	}
	
	public void testSelectUser() {
		 // Start with empty list
		 clearUL();
		
		// Add user for test to select
		int newUserId = ulc.createUser("New User");
		ulc.addUser(new User(newUserId));
		
		// Registers next activity to be monitored
		ActivityMonitor am = getInstrumentation().addMonitor(ClaimListActivity.class.getName(), null, false);
		
		// Run a click on listview in current activity
		userSelectActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				usersLV.performItemClick(usersLV, 0, 0);
			}
		});

		ClaimListActivity nextActivity = (ClaimListActivity) getInstrumentation().waitForMonitorWithTimeout(am, 10000);
		assertNotNull(nextActivity);
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
		
		String name = ulc.getUserList().getUser(0).getName();
		
		// Assert that user in list has the same name as one entered
		assertEquals(name, "New User");
	
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
	
	// Used to start the tests with an empty user list
	private void clearUL() {
		// Initialize UserListController
		ulc = new UserListController();
		
		for (User currentUser : ulc.getUserList().getUsers()) {
			int currentId = currentUser.getId();
			ulc.deleteUser(currentId);
			ulc.removeUser(currentId);
		}
	}
	
}

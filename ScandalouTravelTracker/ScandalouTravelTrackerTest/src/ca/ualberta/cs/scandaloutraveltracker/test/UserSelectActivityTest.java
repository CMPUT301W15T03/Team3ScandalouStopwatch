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

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
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
		 
		 // Reset MockLocationProvider
		 try {
			 MockLocationProvider mlp = new MockLocationProvider("Mock Provider", getActivity());
			 mlp.shutdown();
		 } catch (IllegalArgumentException e) {
			 LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			 lm.removeTestProvider("Mock Provider");
		 }
		 
		 // Get UI components
		 newUserButton = (Button) userSelectActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.userSelectCreateUserButton);
		 usersLV = (ListView) userSelectActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.userSelectUsersLV);
		 cg.resetState(ClaimApplication.getContext());
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
	public void testSelectUser() throws UserInputException {
		cg.makeTwoUsersWithClaims();
		
		// Create a mock location
		MockLocationProvider mlp = new MockLocationProvider("Mock Provider", getActivity());
		mlp.pushLocation(50.2, -12.8);
		LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Location lastLocation = lm.getLastKnownLocation("Mock Provider");
		
		UserListController ulc = new UserListController();
		int newUserId = ( (User) usersLV.getItemAtPosition(0)).getId();
		ulc.removeUser(newUserId);
		UserController uc = new UserController(new User(newUserId));
		uc.setCurrentLocation(lastLocation);
		uc = new UserController(new User(newUserId));
		ulc.addUser(new User(newUserId));
		
		// Registers next activity to be monitored
		ActivityMonitor am = getInstrumentation().addMonitor(ClaimListActivity.class.getName(), null, false);
		
		// Run a click on listview in current activity
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				getActivity().update();
				usersLV.performItemClick(usersLV, 0, 0);
			}
		});
		
		userSelectActivity.onDialogPositiveClick(userSelectActivity.getUserDialog());

		// Test that next activity was launched
		ClaimListActivity nextActivity = (ClaimListActivity) getInstrumentation().waitForMonitorWithTimeout(am, 2500);
		assertNotNull(nextActivity);
		
		// Test that the user in application is set to the new user
		ClaimApplication app = (ClaimApplication) userSelectActivity.getApplicationContext();
		User currentUser = app.getUser();
		nextActivity.finish();
		assertEquals("User1", currentUser.getName());
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
		} catch (Throwable e) {
			new Throwable(e);
		}
		getInstrumentation().waitForIdleSync();
		
		// Create a mock location
		MockLocationProvider mlp = new MockLocationProvider("Mock Provider", getActivity());
		mlp.pushLocation(50.2, -12.8);
		LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Location lastLocation = lm.getLastKnownLocation("Mock Provider");
		
		UserListController ulc = new UserListController();
		int userId = ((User) usersLV.getItemAtPosition(0)).getId();
		ulc.removeUser(userId);
		UserController uc = new UserController(new User(userId));
		uc.setCurrentLocation(lastLocation);
		uc = new UserController(new User(userId));
		ulc.addUser(new User(userId));
		
		// Get only user in list
		User user = ulc.getUser(0);
		
		assertEquals(lastLocation.getLongitude(), user.getHomeLocation().getLongitude());
		assertEquals(lastLocation.getLatitude(), user.getHomeLocation().getLatitude());
	}
	
	public void testIsUsersClaims() throws UserInputException {		
		// Create two users and add them to the list
		cg.makeTwoUsersWithClaims();
		assertEquals(2, ulc.getCount());
		
		// Create a mock location
		MockLocationProvider mlp = new MockLocationProvider("Mock Provider", getActivity());
		mlp.pushLocation(50.2, -12.8);
		LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Location lastLocation = lm.getLastKnownLocation("Mock Provider");
		
		UserListController ulc = new UserListController();
		int userId = ((User) usersLV.getItemAtPosition(0)).getId();
		ulc.removeUser(userId);
		UserController uc = new UserController(new User(userId));
		uc.setCurrentLocation(lastLocation);
		uc = new UserController(new User(userId));
		ulc.addUser(new User(userId));
		
		// Run a click on listview in current activity
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				getActivity().update();
				usersLV.performItemClick(usersLV, 0, 0);
			}
		});
		getInstrumentation().waitForIdleSync();
		
		userSelectActivity.onDialogPositiveClick(userSelectActivity.getUserDialog());
		
		// Registers next activity to be monitored
		ActivityMonitor am = getInstrumentation().addMonitor(ClaimListActivity.class.getName(), null, false);
		
		// Test that next activity was launched
		ClaimListActivity nextActivity = (ClaimListActivity) getInstrumentation().waitForMonitorWithTimeout(am, 2500);
		ClaimList currentClaimList = nextActivity.getCurrentClaimList();
		nextActivity.update();
		nextActivity.finish();
		
		Claim currentClaim = currentClaimList.getClaim(0);
		assertTrue(currentClaim.getUser().getName().equals("User1"));
		assertEquals(1, currentClaimList.getCount());
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

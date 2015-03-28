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

import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.NewClaimActivity;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;

public class ClaimTest extends ActivityInstrumentationTestCase2<NewClaimActivity> {
	
	private NewClaimActivity newClaimActivity;
	private EditText startDateET;
	private EditText endDateET;
	private EditText descriptionET;
	private TextView tagsTV;
	private ListView destinationsList;
	private Button submitButton;
	private ImageButton destinationButton;
	private int userId;
	private Instrumentation instrumentation;
	
	public ClaimTest() {
		super(NewClaimActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		newClaimActivity = getActivity();
		
		// Create fake user and set application 
		UserListController ulc = new UserListController();
		userId = ulc.createUser("Tester");
		ClaimApplication app = (ClaimApplication) newClaimActivity.getApplicationContext();
		app.setUser(new User(userId));
		instrumentation = getInstrumentation();
		
		// Get UI elements
		descriptionET = (EditText) newClaimActivity.findViewById(R.id.edit_claim_descr);
		tagsTV = (TextView) newClaimActivity.findViewById(R.id.edit_claim_tags);
		startDateET = (EditText) newClaimActivity.findViewById(R.id.start_date);
	    endDateET = (EditText) newClaimActivity.findViewById(R.id.end_date);
	    submitButton = (Button) newClaimActivity.findViewById(R.id.claim_ok_button);
	    destinationButton = (ImageButton) newClaimActivity.findViewById(R.id.add_dest_button);
	    destinationsList = (ListView) newClaimActivity.findViewById(R.id.destinations_lv);
	}
	
	// Tests adding a new claim
	// US01.01.01
	public void testNewClaim() throws Throwable {
		newClaimActivity.setStartDate(new Date());
		newClaimActivity.setEndDate(new Date());
		
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				startDateET.setText("filler");
				endDateET.setText("filler");
			}			
		});
		
		getInstrumentation().waitForIdleSync();
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {				
				submitButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		ClaimListController clc = new ClaimListController(new User(userId));
		assertEquals(1, clc.getClaimList().getCount());
	}
	
	// Tests adding a claim with a destination
	// US01.02.01
	public void testAddDestination() throws Throwable {
		
		assertEquals(0, newClaimActivity.getDestinationsList().size());
		
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				destinationButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		// Set the destination values
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				newClaimActivity.setDestinationName("Harlem");
				newClaimActivity.setDestinationReason("Dat WORK vid shoot");
			}
		});
		
		final AlertDialog dialog = newClaimActivity.getDestinationDialog();
		
		// Clicks the add new destination button
		try {
			performClick(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
		} catch (Throwable e) {
			throw new Throwable(e);
		}
		
		assertEquals(1, newClaimActivity.getDestinationsList().size());
		assertEquals(1, destinationsList.getCount());
	}
	
	/*

	// Test UC 01.03.01
	public void testClaimDisplayed() {
	    View allViews = newClaimActivity.getWindow().getDecorView();
	    ViewAsserts.assertOnScreen(allViews, (View) startDateET);
	    ViewAsserts.assertOnScreen(allViews, (View) endDateET);
	    ViewAsserts.assertOnScreen(allViews, (View) descriptionET);
	    ViewAsserts.assertOnScreen(allViews, (View) tagsTV);
	}
	
	// Test UC 01.04.01
	public void testCantEditClaim() {
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    String diffName = "tester";
	    Date diffSDate = new Date(789);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    newClaim.setCanEdit(false);
	    newClaim.setName(diffName);
	    newClaim.setStartDate(diffSDate);
	    assertTrue("Name should not have changed", newClaim.getName().equals(name));
	    assertTrue("Start date should not have changed", 
	                newClaim.getStartDate().equals(sDate));
	    assertTrue("End date should not have changed", 
	                newClaim.getEndDate().equals(eDate));
	}

	// Test UC 01.04.01
	public void testCanEditClaim() {
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    String diffName = "tester";
	    Date diffSDate = new Date(789);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    newClaim.setCanEdit(true);
	    newClaim.setName(diffName);
	    newClaim.setStartDate(diffSDate);
	    assertTrue("Name should have changed", newClaim.getName().equals(diffName));
	    assertTrue("Start date should have changed", 
	                newClaim.getStartDate().equals(diffSDate));
	    assertTrue("End date should not have changed", 
	                newClaim.getEndDate().equals(eDate));
	}

	// Test UC 01.05.01	
	public void testCantDeleteClaim() {
	    ClaimList claimsList = new ClaimList(true);		
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    newClaim.setCanEdit(false);
	    claimsList.addClaim(newClaim);	    
	    claimsList.removeClaim(1);
	    assertEquals("Count should still be one", 1, claimsList.getCount());
	}

	// Test UC 01.05.01
	public void testCanDeleteClaim() {
		ClaimList claimsList = new ClaimList(true);
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    newClaim.setCanEdit(true);
	    claimsList.addClaim(newClaim);
	    claimsList.removeClaim(0);
	    assertEquals("Count should be zero", 0, claimsList.getCount());
	}
	
	// Test UC 01.06.01
	public void testSavedData() throws UserInputException {
		UserListController ulc = new UserListController();
		int newUserId = ulc.createUser("User1");
	    String name = "Justin";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    String description = "description";
	    ArrayList<Destination> destinations = new ArrayList<Destination>();
	    ArrayList<String> tagsList = new ArrayList<String>();
	    String status = Constants.statusInProgress;
	    boolean canEdit = true;
	    ArrayList<Expense> expenses = new ArrayList<Expense>();
	    Claim testClaim = new Claim(2, name, description, sDate, eDate, destinations, tagsList,
	    		status, expenses, canEdit);
	    ClaimList claims1 = new ClaimList(true);
	    claims1.addClaim(testClaim);
	    claims1.createClaim(name, sDate, eDate, description, destinations, 
				tagsList, status, canEdit, expenses, new User(newUserId));

	    ClaimList claims2 = new ClaimList();

	    assertEquals("Id should be equal", claims1.getClaims().get(0).getId(), 
	    		claims2.getClaims().get(0).getId());
	    assertEquals("Names should be equal", claims1.getClaims().get(0).getName(), 
	    		claims2.getClaims().get(0).getName());
	    assertEquals("Start dates should be equal", claims1.getClaims().get(0).getStartDate(), 
	    		claims2.getClaims().get(0).getStartDate());
	    assertEquals("End dates should be equal", claims1.getClaims().get(0).getEndDate(), 
	    		claims2.getClaims().get(0).getEndDate());
	    assertEquals("Description should be equal", claims1.getClaims().get(0).getDescription(), 
	    		claims2.getClaims().get(0).getDescription());
	    assertEquals("Destinations should be equal", claims1.getClaims().get(0).getDestinations(), 
	    		claims2.getClaims().get(0).getDestinations());
	    assertEquals("Tags should be equal", claims1.getClaims().get(0).getTags(), 
	    		claims2.getClaims().get(0).getTags());
	    assertEquals("Status should be equal", claims1.getClaims().get(0).getStatus(), 
	    		claims2.getClaims().get(0).getStatus());
	    assertEquals("Expenses should be equal", claims1.getClaims().get(0).getExpenses(), 
	    		claims2.getClaims().get(0).getExpenses());
	    assertEquals("CanEdit flag should be equal", claims1.getClaims().get(0).getCanEdit(), 
	    		claims2.getClaims().get(0).getCanEdit());
	}
	
	*/
	
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

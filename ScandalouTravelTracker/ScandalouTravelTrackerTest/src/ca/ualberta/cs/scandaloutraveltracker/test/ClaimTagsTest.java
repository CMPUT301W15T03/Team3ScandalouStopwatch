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

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.EditClaimActivity;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;

public class ClaimTagsTest extends ActivityInstrumentationTestCase2<EditClaimActivity> {
	EditClaimActivity editClaimActivity;
	Instrumentation instrumentation;
	TextView tagsDisplay;
	EditText tagsInput;
	Button addTagsButton;
	ClaimController claimController;
	UserListController ulc;
	ClaimListController clc;
	int newClaimId;
	
	public ClaimTagsTest() {
		super(EditClaimActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		ulc = new UserListController();
		clc = new ClaimListController();
		
		// Create mock objects needed to test activity
		Intent mockIntent = makeMockIntent();
		setActivityIntent(mockIntent);
		
		editClaimActivity = getActivity();
		instrumentation = getInstrumentation();
		
		// Get UI components
		tagsDisplay = (TextView) editClaimActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.edit_claim_tags);
		addTagsButton = (Button) editClaimActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.edit_claim_add_tag);
	}

	// US03.01.01
	// Tests that user can have a claim with 0 or more tags associated
	// with it
	public void testTaggingNewClaim() {
		assertEquals(0, claimController.getTags().size());
		
		// Testing add first tag
		addTag("Test1");
		getInstrumentation().waitForIdleSync();
		claimController = new ClaimController(new Claim(newClaimId));
		assertEquals(1, claimController.getTags().size());
		
		// Testing add second tag
		addTag("Test2");
		getInstrumentation().waitForIdleSync();
		claimController = new ClaimController(new Claim(newClaimId));
		assertEquals(2, claimController.getTags().size());
	}
	
	// UC03.03.01
	// Tests that the user can search for claims that have the same
	// tag as the one used to search
	public void testSearchClaims() {
		// Creates 5 Claims, 3 of them have the tag: "TAGGED"
		
		// Click on Tagged and click Search
	}
	
	private void addTag(String tagName) {
		AlertDialog alert;
		
		// Click on add tag button, enter tag, and add
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				addTagsButton.performClick();
			}
			
		});
		
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync(tagName);
		getInstrumentation().waitForIdleSync();
		alert = editClaimActivity.getAlertDialog();
		
		// Try to add
		try {
			performClick(alert.getButton(DialogInterface.BUTTON_POSITIVE));
		} catch (Throwable e) {
			new Throwable(e);
		}
	}
	
	/*// Test UC 03.03.01
	public void testFilterClaims() {
		Claim claim1 = new Claim();
		Claim claim2 = new Claim();
		ArrayList<String> tags1 = new ArrayList<String>();
		ArrayList<String> tags2 = new ArrayList<String>();
		tags1.add("Tag1");
		tags2.add("Tag2");
		claim1.setTags(tags1);
		claim2.setTags(tags2);
		ClaimList claimList = new ClaimList();
		ClaimList.addClaim(claim1);
		ClaimList.addClaim(claim2);
		ArrayList<Claim> filteredList = claimList.searchTag("Tag1");
		assertTrue("Filter failed", (filteredList.size() == 1));
		assertTrue("Wrong claim", (filteredList.get(0) == claim1));
	}
*/
	
	// Used to create a mock intent to give to the activity
	private Intent makeMockIntent() {
		
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = new ArrayList<String>();
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		int userId = ulc.createUser("User1");
		ulc.addUser(new User(userId));
		
		// Create the claim
		newClaimId = clc.createClaim("a1", new Date(), new Date(), "d1", destinations, 
				tagsList, status, canEdit, expenses, new User(userId));	
		
		Intent mockIntent = new Intent();
		mockIntent.putExtra(Constants.claimIdLabel, newClaimId);
		
		// Set ClaimController
		claimController = new ClaimController(new Claim(newClaimId));
		
		return mockIntent;
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
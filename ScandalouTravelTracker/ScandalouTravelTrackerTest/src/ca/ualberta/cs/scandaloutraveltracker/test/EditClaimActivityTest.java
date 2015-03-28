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
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.EditClaimActivity;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;

public class EditClaimActivityTest extends ActivityInstrumentationTestCase2<EditClaimActivity> {

	Instrumentation instrumentation;
	EditClaimActivity activity;
	ClaimController cc;
	Button subButton;
	Button updateButton;
	ImageButton addDestButton;
	Toast testToast;
	EditText startDateET;
	EditText endDateET;
	EditText descriptionET;
	ListView destinationsLV;
	AlertDialog alert;
	int newClaimId;
	
	public EditClaimActivityTest() {
		super(EditClaimActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(true);
		
		Intent intent = new Intent();
		int newId = createMockClaim();
		intent.putExtra(Constants.claimIdLabel, newId);
	    setActivityIntent(intent);
	    
		instrumentation = getInstrumentation();
		activity = getActivity();
		
		subButton = (Button) activity.findViewById(R.id.edit_claim_send);
		startDateET = (EditText) activity.findViewById(R.id.edit_claim_start_date);
		endDateET = (EditText) activity.findViewById(R.id.edit_claim_end_date);
		destinationsLV = (ListView) activity.findViewById(R.id.edit_claim_destinations);
		descriptionET = (EditText) activity.findViewById(R.id.edit_claim_descr);
		updateButton = (Button) activity.findViewById(R.id.edit_claim_update);
		addDestButton = (ImageButton) activity.findViewById(R.id.edit_claim_new_destination);
	}
	
	// Tests that the mock claim has all its data properly displayed in the
	// UI elements. Also tests that a newly created claim has its data saved
	// for when passed to this screen. 
	// (Tested by creating a new mock claim which gets saved and then loaded)
	// US01.03.01
	// US01.06.01
	public void testClaimDataDisplayed() throws UserInputException {
		SpannableString span = activity.getSpannableString();
		ClickableSpan[] tags = span.getSpans(0, span.length(), ClickableSpan.class);
		
		assertEquals(2, destinationsLV.getCount());
		assertEquals(2, tags.length);
		assertTrue(startDateET.getText().toString().equals("3/15/2014"));
		assertTrue(endDateET.getText().toString().equals("3/17/2014"));
		assertTrue(descriptionET.getText().toString().equals("d1"));
	}
	
	// Tests that the mock claim can be edited while it has not been submitted
	// US01.04.01
	public void testClaimEditable() throws UserInputException {
		ArrayList<Destination> newDest = new ArrayList<Destination>();
		newDest.add(new Destination("Compton", "Video shoot"));
		Date newStart = createDate(2, 16, 2015);
		Date newEnd = createDate(2, 17, 2015);
		activity.editClaim(newStart, newEnd, newDest);
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				descriptionET.setText("new one");
				updateButton.performClick();
			}
		});
		
		assertEquals(1, destinationsLV.getCount());
		assertTrue(startDateET.getText().toString().equals("3/16/2015"));
		assertTrue(endDateET.getText().toString().equals("3/17/2015"));
		assertTrue(descriptionET.getText().toString().equals("new one"));
	}
	
	// Tests that UI elements disappear and you can't edit the claim after submitting
	// US01.04.01
	public void testClaimNotEditable() throws UserInputException {
		// Submit the claim created
		ClaimController claimController = new ClaimController(new Claim(newClaimId));
		claimController.submitClaim(Constants.statusSubmitted, false);
		
		ClaimListController claimListController = new ClaimListController();
		claimListController.removeClaim(newClaimId);
		claimListController.addClaim(new Claim(newClaimId));
		
		// Pack the intent with the new submitted claimID and
		// restart the activity and get new instrumentation
		Intent intent = new Intent();
		intent.putExtra(Constants.claimIdLabel, newClaimId);
	    activity.finish();
	    setActivity(null);
	    setActivityIntent(intent);
	    activity = getActivity();
	    getInstrumentation().callActivityOnRestart(activity);
	    
	    // Set all the old view references to new ones
		startDateET = (EditText) activity.findViewById(R.id.edit_claim_start_date);
		endDateET = (EditText) activity.findViewById(R.id.edit_claim_end_date);
		descriptionET = (EditText) activity.findViewById(R.id.edit_claim_descr);
		subButton = (Button) activity.findViewById(R.id.edit_claim_send);
		updateButton = (Button) activity.findViewById(R.id.edit_claim_update);
		addDestButton = (ImageButton) activity.findViewById(R.id.edit_claim_new_destination);
	    
		// Attempt to change data (toasts display if cant)
	    getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				startDateET.performClick();
				endDateET.performClick();
				descriptionET.performClick();
			}
	    	
	    });
	    getInstrumentation().waitForIdleSync();
	    
		assertEquals(3, activity.getToastCount());
		assertFalse(updateButton.isShown());
		assertFalse(addDestButton.isShown());
		assertFalse(subButton.isShown());
	}

	private int createMockClaim() throws UserInputException {
		// Create two users and add them to the list
		UserListController ulc = new UserListController();
		int userId = ulc.createUser("User1");
		ulc.addUser(new User(userId));
		
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = new ArrayList<String>();
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		// Setting claim test data
		Destination testDestination = new Destination("Brooklyn", "Gotta see Jay");
		destinations.add(testDestination);
		testDestination = new Destination("Brookyln", "Meet up with Rocky");
		destinations.add(testDestination);
		
		String testTag = "";
		tagsList.add(testTag);
		testTag = "#stoked";
		tagsList.add(testTag);
		testTag = "#NY";
		tagsList.add(testTag);
		
		Expense testExpense = new Expense();
		expenses.add(testExpense);
		
		// Month - Day - Year
		Date startDate = createDate(2, 15, 2014);
		Date endDate = createDate(2, 17, 2014);
		
		// Create the claim
		newClaimId = clc.createClaim("a1", startDate, endDate, "d1", destinations, 
				tagsList, status, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
		
		return newClaimId;
	}

	private Date createDate(int month, int day, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();
		
		return date;
	}

	/*
	// Test UC 07.01.01
	// test default claim status
	public void testClaimStatus(){
		Claim claim1 = new Claim();
		Expense expense1 = new Expense();
		claim1.addExpense(expense1);
		claim1.getStatus();
		assertTrue("Claim has wrong status", claim1.getStatus()=="In Progress");
	}
	
	// Test UC 07.01.01
	// test the status of the claim changed to submitted
	public void testSubmitClaim(){
		Claim claim1 = new Claim();
		Expense expense1 = new Expense();		
		claim1.addExpense(expense1);
		String status1 = "Submitted";
		claim1.setStatus(status1);
		assertTrue("Claim is submitted", claim1.getStatus()=="Submitted");
	}

	// Test UC 07.02.01
	@UiThreadTest
	public void testWarningPopup(){
		//test if pressing button when claim is empty will show a toast indicator
		activity.runOnUiThread(new Runnable() {
			@Override
		    public void run() {
		      subButton.performClick();
		      testToast.show();
		    }
		  });
	      boolean show = testToast.getView().isShown();
	      assertFalse("toast is not shown", show);
	      //assertTrue("toast worked", show);
	}

	// Test UC 07.03.01
	public void testClaimNotApproved(){
		
		String approverName1 = "Dick T. Approver";
		
		// Create submitted claims
		Claim claim1 = new Claim();
		String status1 = "Submitted";		
		claim1.setStatus(status1);
		
		// Perform actions
		claim1.returnClaim(approverName1);
		
		// Carry out tests
		assertTrue("testclaim should have status1", claim1.getStatus().equals(status1));
		assertTrue("testclaim should have approverName equal to approverName1.", claim1.getApproverName().equals(approverName1));
		
		
	}

	// Test UC 07.04.01
	public void testClaimApproved(){
		
		String approverName1 = "Dick T. Approver";
		
		// Create submitted claims
		Claim claim1 = new Claim();
		String status1 = "Submitted";		
		claim1.setStatus(status1);
		
		// Perform actions
		claim1.approveClaim(approverName1);
		
		// Carry out tests
		assertTrue("testclaim should have status1", claim1.getStatus().equals(status1));
		assertTrue("testclaim should have approverName equal to approverName1.", claim1.getApproverName().equals(approverName1));
		
	}

	// Test UC 07.05.01
	public void testApproverInfo(){
		
		String approverName1 = "Dick T. Approver";
		String comment = "hello jim";
		
		// Create submitted claim
		Claim claim1 = new Claim();
		String status1 = "Submitted";		
		claim1.setStatus(status1);
		claim1.setApproverComment(comment);
		
		// Perform actions
		claim1.returnClaim(approverName1);
		
		// Carry out tests
		assertTrue("approver name should be Dick T. Approver",claim1.getApproverName().equals(approverName1));
		assertTrue("comments should say Hello jim", claim1.getApproverComment().equals(comment));
	}
	*/

}

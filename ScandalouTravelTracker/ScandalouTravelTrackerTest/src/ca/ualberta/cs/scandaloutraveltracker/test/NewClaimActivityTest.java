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
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.views.NewClaimActivity;

public class NewClaimActivityTest extends ActivityInstrumentationTestCase2<NewClaimActivity> {
	
	NewClaimActivity newClaimActivity;
	EditText startDateET;
	EditText endDateET;
	EditText descriptionET;
	TextView tagsTV;
	ListView destinationsList;
	Button submitButton;
	Button addTagsButton;
	ImageButton destinationButton;
	int userId;
	Instrumentation instrumentation;
	ListView alertChoices;
	AlertDialog dialog;
	EditText descriptionNameET;
	EditText descriptionReasonET;
	
	public NewClaimActivityTest() {
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
		tagsTV = (TextView) newClaimActivity.findViewById(R.id.new_claim_tags_tv);
		startDateET = (EditText) newClaimActivity.findViewById(R.id.start_date);
	    endDateET = (EditText) newClaimActivity.findViewById(R.id.end_date);
	    submitButton = (Button) newClaimActivity.findViewById(R.id.claim_ok_button);
	    destinationButton = (ImageButton) newClaimActivity.findViewById(R.id.add_dest_button);
	    destinationsList = (ListView) newClaimActivity.findViewById(R.id.destinations_lv);
	    addTagsButton = (Button) newClaimActivity.findViewById(R.id.new_claim_add_tag);
	}
	
	// Tests adding a new claim
	// Also tests that claims can have zero expenses attaached to them
	// US01.01.01 and US04.01.01
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
		performClick(submitButton);
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
		
		View newDestView = newClaimActivity.getNewDestView();
		descriptionNameET = (EditText) newDestView.findViewById(R.id.edit_destination_name);
		descriptionReasonET = (EditText) newDestView.findViewById(R.id.edit_destination_description);
		
		// Set the destination values)
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				descriptionNameET.setText("Harlem");
				descriptionReasonET.setText("Dat WORK vid shoot");
			}
		});
		
		dialog = newClaimActivity.getDestinationDialog();
		
		// Clicks the add new destination button
		try {
			performClick(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
		} catch (Throwable e) {
			throw new Throwable(e);
		}
		
		Destination onlyDest = (Destination) destinationsList.getItemAtPosition(0);
		
		assertEquals(1, newClaimActivity.getDestinationsList().size());
		assertEquals(1, destinationsList.getCount());
		assertTrue(onlyDest.getName().equals("Harlem"));
		assertTrue(onlyDest.getDescription().equals("Dat WORK vid shoot"));
	}
	
	// This test starts with an empty claim, adds a tag, renames the tag, and
	// then deletes the tag. Assertions on list size and tag name.
	// US03.02.01
	public void testDeleteAddRenameTags() {
		AlertDialog alert;
		ArrayList<String> currentTags;
		SpannableString ss;
		
		performClick(addTagsButton);
		alert = newClaimActivity.getAlert();
		assertTrue(alert.isShowing());
		
		instrumentation.waitForIdleSync();
		instrumentation.sendStringSync("TestTag");
		instrumentation.waitForIdleSync();
		performClick(alert.getButton(DialogInterface.BUTTON_POSITIVE));
		
		// Asserts for adding
		currentTags = newClaimActivity.getTagsList();
		ss = newClaimActivity.getSpannableString();
		Log.d("TAG", ss.toString());
		assertTrue("#TestTag".equals(ss.toString()));
		assertEquals(1, currentTags.size());
		
		TouchUtils.clickView(this, tagsTV);
		alert = newClaimActivity.getAlert();
		assertTrue(alert.isShowing());
		alertChoices = alert.getListView();
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				alertChoices.performItemClick(alertChoices, 0, 0);
			}
		});
		alert = newClaimActivity.getAlert();
		assertTrue(alert.isShowing());
		
		instrumentation.waitForIdleSync();
		instrumentation.sendStringSync("Tag2");
		instrumentation.waitForIdleSync();
		
		performClick(alert.getButton(DialogInterface.BUTTON_POSITIVE));

		// Asserts for renaming
		currentTags = newClaimActivity.getTagsList();
		ss = newClaimActivity.getSpannableString();
		assertEquals(1, currentTags.size());
		assertTrue("#Tag2".equals(ss.toString()));
		
		TouchUtils.clickView(this, tagsTV);
		alert = newClaimActivity.getAlert();
		assertTrue(alert.isShowing());
		alertChoices = alert.getListView();
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				alertChoices.performItemClick(alertChoices, 1, 0);
			}
		});
		
		ss = newClaimActivity.getSpannableString();
		currentTags = newClaimActivity.getTagsList();
		assertTrue("".equals(ss.toString()));
		assertEquals(0, currentTags.size());
		
	}
	
	private void performClick(final Button button) {
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
}

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
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.views.EditClaimActivity;

public class EditClaimActivityTest extends ActivityInstrumentationTestCase2<EditClaimActivity> {

	Instrumentation instrumentation;
	EditClaimActivity activity;
	ClaimController cc;
	Button updateButton;
	ImageButton addTagButton;
	ImageButton addDestButton;
	Toast testToast;
	TextView tagsTV;
	EditText startDateET;
	EditText endDateET;
	EditText descriptionET;
	ListView destinationsLV;
	ListView alertChoices;
	AlertDialog alert;
	int newClaimId;
	SpannableString spannableString;
	ClaimGenerator cg;
	
	public EditClaimActivityTest() {
		super(EditClaimActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(true);
		
		
		Intent intent = new Intent();
		intent.putExtra(Constants.claimIdLabel, 0);
		
		cg = new ClaimGenerator();
		
		activity = getActivity();
		
		newClaimId = cg.createMockClaim(true, true, true, true);
		activity.finish();
		setActivity(null);

		intent.putExtra(Constants.claimIdLabel, newClaimId);
	    setActivityIntent(intent);
	    
		instrumentation = getInstrumentation();
		activity = getActivity();
		
		startDateET = (EditText) activity.findViewById(R.id.appr_edit_claim_start_date);
		endDateET = (EditText) activity.findViewById(R.id.edit_claim_end_date);
		destinationsLV = (ListView) activity.findViewById(R.id.edit_claim_destinations);
		descriptionET = (EditText) activity.findViewById(R.id.edit_claim_descr);
		updateButton = (Button) activity.findViewById(R.id.edit_claim_update);
		addDestButton = (ImageButton) activity.findViewById(R.id.edit_claim_new_destination);
		tagsTV = (TextView) activity.findViewById(R.id.edit_claim_tags);
		addTagButton = (ImageButton)activity.findViewById(R.id.edit_claim_add_tag);
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
		assertTrue(startDateET.getText().toString().equals("3/1/2014"));
		assertTrue(endDateET.getText().toString().equals("3/3/2014"));
		assertTrue(descriptionET.getText().toString().equals("d1"));
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Tests that UI elements disappear and you can't edit the claim after submitting
	// US01.04.01
	public void testClaimNotEditable() throws UserInputException {
		submitClaim();
		attemptClicks();
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Tests that when a claim is submitted, it has the submit status and that
	// you can still freely edit the tags
	// US07.01.01
	public void testSubmitClaimEditTags() {
		submitClaim();
		assertTrue(new Claim(newClaimId).getStatus().equals(Constants.statusSubmitted));
		deleteAddRenameTags();
		attemptClicks();
		cg.resetState(ClaimApplication.getContext());
	}
	
	// This test starts with a claim that has two tags. It deletes the two tags,
	// adds one new tag, and then renames that tag and has assertions for the
	// size of the list and for the name of the tag after being renamed.
	// US03.02.01
	public void testDeleteAddRenameTags() {
		spannableString = activity.getSpannableString();
		final ClickableSpan[] spans = spannableString.getSpans(0, spannableString.length()-1, ClickableSpan.class);
		int tagsSize = spans.length;
		
		assertEquals(2, tagsSize);
		
		deleteAddRenameTags();
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Tests that the alert box warning appears for when the user tries to submit
	// a claim that has a flagged expense
	// US07.02.01
	public void testFlagWarning() {
		instrumentation.invokeMenuActionSync(activity, 
				ca.ualberta.cs.scandaloutraveltracker.R.id.action_send_claim, 0);
		getInstrumentation().waitForIdleSync();
		
		AlertDialog alert = activity.getAlertDialog();
		assertTrue(alert.isShowing());
		
		performClick(alert.getButton(DialogInterface.BUTTON_POSITIVE));
		alert = activity.getAlertDialog();
		assertTrue(alert.isShowing());
		
		cg.resetState(ClaimApplication.getContext());
	}
	
	// Attempt clicks is used for testing if a claim has been properly submitted
	// If it has, 3 toasts will show when attempting the performClick actions
	private void attemptClicks() {
	    getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				startDateET.performClick();
				endDateET.performClick();
				descriptionET.performClick();
			}
	    	
	    });
	    getInstrumentation().waitForIdleSync();
	    
	    Menu menu = activity.getOptionsMenu();
	    MenuItem send = menu.findItem(R.id.action_send_claim);
	    
		assertEquals(3, activity.getToastCount());
		assertFalse(updateButton.isShown());
		assertFalse(addDestButton.isShown());
		assertFalse(send.isVisible());
		cg.resetState(ClaimApplication.getContext());
	}
	
	// This method deletes the two tags that are initially given to the activity,
	// adds one, and renames the one it added
	private void deleteAddRenameTags() {
		// Deleting two tags and asserting that the size of tags list
		// decreases upon each deletion
		for (int i = 0; i < 2; i++) {
			TouchUtils.clickView(this, tagsTV);
			alert = activity.getAlertDialog();
			assertTrue(alert.isShowing());
			
			// Delete the last tag in the list twice
			alertChoices = alert.getListView();
			
			getInstrumentation().runOnMainSync(new Runnable() {
				@Override
				public void run() {
					alertChoices.performItemClick(alertChoices, 1, 0);	
				}
			});
			getInstrumentation().waitForIdleSync();
			cc = new ClaimController(new Claim(newClaimId));
			assertEquals(2-(i+1), cc.getTags().size());
		}
		
		// Adding a tag to the list and asserting the size increased
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				addTagButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		alert = activity.getAlertDialog();
		assertTrue(alert.isShowing());
		
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync("NewYork");
		getInstrumentation().waitForIdleSync();
		
		performClick(alert.getButton(DialogInterface.BUTTON_POSITIVE));
		
		cc = new ClaimController(new Claim(newClaimId));
		assertEquals(1, cc.getTags().size());
		
		
		// Click on the tag and change its name
		// Assert that the only spannable string is the new name
		TouchUtils.clickView(this, tagsTV);
		alert = activity.getAlertDialog();
		alertChoices = alert.getListView();
		assertTrue(alert.isShowing());
		
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				alertChoices.performItemClick(alertChoices, 0, 0);
			}
		});
		
		alert = activity.getAlertDialog();
		assertTrue(alert.isShowing());
		
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync("NewTag");
		getInstrumentation().waitForIdleSync();
		
		performClick(alert.getButton(DialogInterface.BUTTON_POSITIVE));
		
		spannableString = activity.getSpannableString();
		cc = new ClaimController(new Claim(newClaimId));
		assertTrue("#NewTag".equals(spannableString.toString()));
		assertEquals(1, cc.getTags().size());
		cg.resetState(ClaimApplication.getContext());
	}
	
	// This method will submit the claim that was created and then reload the activity
	// and all the views with the new references
	private void submitClaim() {
		// Submit the claim created
		ClaimController claimController = new ClaimController(new Claim(newClaimId));
		claimController.submitClaim();
		
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
		startDateET = (EditText) activity.findViewById(R.id.appr_edit_claim_start_date);
		endDateET = (EditText) activity.findViewById(R.id.edit_claim_end_date);
		descriptionET = (EditText) activity.findViewById(R.id.edit_claim_descr);
		updateButton = (Button) activity.findViewById(R.id.edit_claim_update);
		addDestButton = (ImageButton) activity.findViewById(R.id.edit_claim_new_destination);
		tagsTV = (TextView) activity.findViewById(R.id.edit_claim_tags);
		addTagButton = (ImageButton)activity.findViewById(R.id.edit_claim_add_tag);
		cg.resetState(ClaimApplication.getContext());
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

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.EditClaimActivity;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.R;

public class ClaimStatusTest extends ActivityInstrumentationTestCase2<EditClaimActivity> {

	Instrumentation instrumentation;
	Activity activity;
	ClaimController cc;
	Button subButton;
	
	public ClaimStatusTest() {
		super(EditClaimActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.putExtra("ca.ualberta.cs.scandaloutraveltracker.claimId", "");
	    setActivityIntent(intent);
		instrumentation = getInstrumentation();
		cc = new ClaimController(new Claim());
		cc.setEndDate(new Date());
		cc.setEndDate(new Date());
		activity = getActivity();
		subButton = (Button) activity.findViewById(R.id.edit_claim_send);
	}

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
		
		//setUp();
		final Activity submitTest = startWithClaim();
		subButton.performClick();
		Toast testToast = Toast.makeText(submitTest, "test", Toast.LENGTH_LONG);
		boolean isShown = testToast.getView().isShown();
		assertFalse("toast is not shown", isShown);
		assertTrue("toast worked", isShown);
		
		submitTest.runOnUiThread(new Runnable() {
			@Override
		    public void run() {
		      subButton.performClick();
		      Toast testToast = new Toast(null);
		      testToast = Toast.makeText(submitTest, "test", Toast.LENGTH_LONG);
		      boolean isShown = testToast.getView().isShown();
		      assertTrue("toast worked", isShown);
		      assertFalse("toast is not shown", isShown);
		    }
		  });
	}
	
	private EditClaimActivity startWithClaim() {
		String name = "test";
		Date sDate = new Date(123);
		Date eDate = new Date(456);
		Claim testClaim = new Claim(name, sDate, eDate);
		EditClaimActivity activity = getActivity();

	    TextView nameDisplay = (TextView) activity.findViewById(R.id.edit_claim_claimant_name);
	    TextView sDateDisplay = (TextView) activity.findViewById(R.id.edit_claim_start_date);
	    TextView eDateDisplay = (TextView) activity.findViewById(R.id.edit_claim_end_date);
	    
	    nameDisplay.setText(testClaim.getName());
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
	    sDateDisplay.setText(sdf.format(testClaim.getStartDate()));
	    eDateDisplay.setText(sdf.format(testClaim.getStartDate()));
	    
		return activity;
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
}

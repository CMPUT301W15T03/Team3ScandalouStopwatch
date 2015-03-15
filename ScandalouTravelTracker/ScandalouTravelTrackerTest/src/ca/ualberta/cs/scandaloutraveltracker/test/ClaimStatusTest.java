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

import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import junit.framework.TestCase;

public class ClaimStatusTest extends TestCase {

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
	public void testWarningPopup(){
		Claim claim1 = new Claim();		
		String status1 = "Submitted";
		claim1.setStatus(status1);
		String warning1 = "Warning 1";
		//claim1.raiseWarning(warning1);
		//assertTrue("Warning is raised", claim1.getWarning() == warning1);
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
		
		// Perform actions
		claim1.returnClaim(approverName1);
		
		// Carry out tests
		assertTrue("approver name should be Dick T. Approver",claim1.getApproverName().equals(approverName1));
		assertTrue("comments should say Hello jim", claim1.getApproverComment().equals(comment));
	}
}

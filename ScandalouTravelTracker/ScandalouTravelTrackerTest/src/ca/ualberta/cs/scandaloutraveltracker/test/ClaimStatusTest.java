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
		Claim testclaim= new Claim();
		Expense testexpense=new Expense();
		testclaim.addExpenseitem(testexpense);
		testclaim.get().claimStatus();
		assertTrue("claim has wrong status", testclaim.get().claimStatus()=="pending");
	}
	
	// Test UC 07.01.01
	// test the status of the claim changed to submitted
	public void testSubmitClaim(){
		testclaim.addExpenseitem(testexpense);
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		Claim claim1 = new Claim(claimName1);
		claim1.setStatus(claimStatus1);
		assertTrue("Claim is Submitted", claim1.get().claimStatus()=="Submitted");
	}

	// Test UC 07.02.01
	public void testWarningPopup(){
		
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		Claim testclaim= new Claim(claimName1);
		testclaim.raiseWarning();
		assertTrue("Warning is raised",testclaim.getWarning()==testclaim.raiseWarning());
		
	}

	// Test UC 07.03.01
	public void testClaimNotApproved(){
		String approverName1 = "Dick T. Approver";
		
		// Create submitted claims
		
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		Claim testclaim = new Claim(claimName1);
		testclaim.setStatus(claimStatus1);
		// Perform actions
		
		testclaim.returnClaim();
		
		// Carry out tests
		assertTrue("testclaim should have status1", claim1.getStatus().equals(status1));
		assertTrue("testclaim should have approverName equal to approverName1.", claim1.getApproverName().equals(approverName1));
		
		
	}

	// Test UC 07.04.01
	public void testClaimApproved(){
		String approverName1 = "Dick T. Approver";
		
		// Create submitted claims
		
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		Claim testclaim = new Claim(claimName1);
		testclaim.setStatus(claimStatus1);
		// Perform actions
		
		testclaim.approveClaim();
		
		// Carry out tests
		assertTrue("testclaim should have status1", testclaim.getStatus().equals(claimStatus1));
		assertTrue("testclaim should have approverName equal to approverName1.", testclaim.getApproverName().equals(approverName1));
		
	}

	// Test UC 07.05.01
	public void testApproverInfo(){
		String approverName1 = "Dick T. Approver";
		String comment="hello jim";
		// Create submitted claims
		
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		Claim testclaim = new Claim(claimName1);
		testclaim.setStatus(claimStatus1);
		// Perform actions
		
		testclaim.returnClaim();
		
		// Carry out tests
		assertTrue("approver name should be Dick T. Approver",testclaim.getApproverName().equals(approverName1));
		assertTrue("comments should say Hello jim", testclaim.getApproverComment().equals(comment));
		;
	}
}

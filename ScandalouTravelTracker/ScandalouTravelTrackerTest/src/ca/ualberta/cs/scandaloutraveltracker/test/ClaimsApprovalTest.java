/*

Copyright 2015 ScandalouStopwatch

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
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class ClaimsApprovalTest extends TestCase {

	public void testEmptyClaimList(){
		
		ClaimList claimList1 = new ClaimList();
		assertTrue("The claim list should be empty.", claimList1.size() == 0);
	}
	
	// Test UC 08.01.01
	public void testGetSubmittedClaims(){
		
		// Create a claim list
		ClaimList claimList1 = new ClaimList();
		
		// Create claims
		ClaimGenerator claimGenerator = new ClaimGenerator();
		Claim claim1 = claimGenerator.generateClaim(1, "Submitted");
		Claim claim2 = claimGenerator.generateClaim(2, "In Progress");
		
		// Populate the claim list
		claimList1.addClaim(claim1);		
		claimList1.addClaim(claim2);		
		
		// Carry out tests
		ArrayList<Claim> claims1 = claimList1.getSubmittedClaims();
		assertTrue("claims1 should contain 1 claim.", claims1.size() == 1);
		assertTrue("claims1 should contain claim1", claims1.contains(claim1));
	}
	
	// Test UC 08.01.02
	public void testShowClaimDetailsInList(){
		
		// Create a claim list
		ClaimList claimList1 = new ClaimList();

		// Create a claim
		ClaimGenerator claimGenerator = new ClaimGenerator();
		Claim claim1 = claimGenerator.generateClaim(1, "Submitted");
		
		// Populate the claim list
		claimList1.addClaim(claim1);
		
		// Carry out the tests
		assertTrue("claim1 should have the generated claimant name.", 
				claim1.getClaimantName().equals(claimGenerator.getClaimantName(1)));
		assertTrue("claim1 should have the generated start date.", 
				claim1.getStartMDY().equals(claimGenerator.getStartDate(1)));
		assertTrue("claim1 should have two destinations.", 
				claim1.getDestinations.size() == 2);
		assertTrue("claim1's first destination should be the generated destination1.", 
				claim1.getDestinations().get(0).equals(claimGenerator.getDestination1(1)));
		assertTrue("claim1's second destination should be the generated destination2.", 
				claim1.getDestinations().get(1).equals(claimGenerator.getDestination2(1)));
		assertTrue("claim1 should have the generated status", 
				claim1.getStatus().equals(claimGenerator.getStatus()));
		assertTrue("claim1 should have two currency amounts.", 
				claim1.getTotals().size() == 2);
		assertTrue("claim1 should have the first generated currency amount", 
				claim1.getTotals.get(claimGenerator.getCurrency1()) == claimGenerator.getAmount1(1));
		assertTrue("claim1 should have the second generated currency amount", 
				claim1.getTotals.get(claimGenerator.getCurrency2()) == claimGenerator.getAmount2(1));
		assertTrue("claim1 should have the generated approver name.", 
				claim1.getApproverName().equals(claimGenerator.getApproverName()));
		
	}
	
	// Test UC 08.02.01
	public void testClaimListOrder(){
		
		// Create a claim list
		ClaimList claimList1 = new ClaimList();
		
		// Create claims
		ClaimGenerator claimGenerator = new ClaimGenerator();
		Claim claim2 = claimGenerator.generateClaim(2, "Submitted");
		Claim claim1 = claimGenerator.generateClaim(1, "Submitted");
		
		// Populate the claim list
		claimList1.addClaim(claim2);
		claimList1.addClaim(claim1);
		
		// Perform actions
		claimList1.sortByStartDate();
		
		// Carry out tests
		ArrayList<Claim> claims1 = claimList1.getClaims();
		assertTrue("claimName of the first claim in claims1 should be claim1's name", 
				claims1.get(0).getName().equals(claim1.getName()));
	}
	
	// Test UC 08.03.01
	public void testShowClaim(){

		// Create a claim
		ClaimGenerator claimGenerator = new ClaimGenerator();
		Claim claim1 = claimGenerator.generateClaim(1);
		
		// Carry out the tests
		assertTrue("claim1 should have the generated claimant name.", 
				claim1.getClaimantName().equals(claimGenerator.getClaimantName(1)));
		assertTrue("claim1 should have the generated start date.", 
				claim1.getStartMDY().equals(claimGenerator.getStartDate(1)));
		assertTrue("claim1 should have the generated end date.", 
				claim1.getStartMDY().equals(claimGenerator.getEndDate(1)));		
		assertTrue("claim1 should have two destinations.", 
				claim1.getDestinations.size() == 2);
		assertTrue("claim1's first destination should be the generated destination1.", 
				claim1.getDestinations().get(0).equals(claimGenerator.getDestination1(1)));
		assertTrue("claim1's second destination should be the generated destination2.", 
				claim1.getDestinations().get(1).equals(claimGenerator.getDestination2(1)));
		assertTrue("claim1 should have the generated status", 
				claim1.getStatus().equals(claimGenerator.getStatus()));
		assertTrue("claim1 should have two currency amounts.", 
				claim1.getTotals().size() == 2);
		assertTrue("claim1 should have the first generated currency amount", 
				claim1.getTotals.get(claimGenerator.getCurrency1()) == claimGenerator.getAmount1(1));
		assertTrue("claim1 should have the second generated currency amount", 
				claim1.getTotals.get(claimGenerator.getCurrency2()) == claimGenerator.getAmount2(1));
		assertTrue("claim1 should have the generated approver name.", 
				claim1.getApproverName().equals(claimGenerator.getApproverName()));
		
	}
	
	// Test UC 08.04.01
	public void testGetExpenses(){

		// Create a expense list
		ExpenseList expenseList1 = new ExpenseList();
		
		// Create expenses
		ExpenseGenerator expenseGenerator = new ExpenseGenerator();
		Expense expense1 = expenseGenerator.generateExpense(1);
		Expense expense2 = expenseGenerator.generateExpense(2);
		
		// Populate the expense list
		expenseList1.addExpense(expense1);		
		expenseList1.addExpense(expense2);		
		
		// Carry out tests
		ArrayList<Expense> expenses1 = expenseList1.getExpenses();
		assertTrue("expenses1 should contain 2 expense.", expenses1.size() == 2);
		assertTrue("expenses1 should contain expense1", expenses1.contains(expense1));
		assertTrue("expenses1 should contain expense2", expenses1.contains(expense2));		
		
		// Carry out tests
		assertTrue("expenseName of the first expense in expenses1 should be expense2's name", 
				expenses1.get(0).getName().equals(expense2.getName()));	
		
	}
	
	// Test UC 08.04.02
	public void testShowExpenseDetailsInList(){

		// Create a expense list
		ExpenseList expenseList1 = new ExpenseList();

		// Create a expense
		ExpenseGenerator expenseGenerator = new ExpenseGenerator();
		Expense expense1 = expenseGenerator.generateExpense(1);
		
		// Populate the expense list
		expenseList1.addExpense(expense1);
		
		// Carry out the tests
		assertTrue("expense1 should have the generated expense name.", 
				expenseList1.get(0).getExpenseName().equals(expenseGenerator.getExpenseName(1)));
		assertTrue("expense1 should have the generated date.", 
				expenseList1.get(0).getDate().equals(expenseGenerator.getDate(1)));
		assertTrue("expense1 should have the generated category.", 
				expenseList1.get(0).getCategory().equals(expenseGenerator.getCategory(1)));
		assertTrue("expense1 should have the generated currency and amount.", 
				expenseList1.get(0).getTotals.get(expenseGenerator.getCurrency()) == expenseGenerator.getAmount(1));
		assertTrue("expense1 should have the given receipt status.", 
				expenseList1.get(0).getReceiptYN().equals(expenseGenerator.getReceiptYN()));		
	}
	
	// Test UC 08.05.01
	public void testShowReceipt(){
		// Will add an intent test when we make the UI	
	}	
	
	// Test UC 08.06.01
	public void testAddApproverComment(){
		
		// Create a claim
		ClaimGenerator claimGenerator = new ClaimGenerator();
		Claim claim1 = claimGenerator.generateClaim(1, "Submitted");
		
		// Set approver comment
		String comment1 = "Approver comment";
		
		// Perform action
		claim1.writeComment(comment1);	
		
		// Carry out tests
		assertTrue("claim1 should have comment1.", claim1.getComment().equals(comment1));
		
	}
	
	// Test UC 08.06.02
	public void testEditApproverComment(){
		
		// Create a claim
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		String comment1 = "Approver comment 1";
		String comment2 = "Approver comment 2";		
		Claim claim1 = new Claim();
		
		// Perform actions
		claim1.writeComment(comment1);
		
		// Carry out tests
		assertTrue("claim1 should have comment1.", claim1.getComment().equals(comment1));
		
		// Perform actions
		claim1.writeComment(comment2);
		
		// Carry out tests
		assertTrue("claim1 should have comment2.", claim1.getComment().equals(comment2));		
		
	}
	
	// Test UC 08.06.01, UC 08.06.02
	public void testCommentMyClaim(){
		
		// Create a claim
		ClaimGenerator claimGenerator = new ClaimGenerator();
		Claim claim1 = claimGenerator.generateClaim(1, "Submitted");

		// Set approver name to claimant name; set target claim status
		String approverName1 = claim1.getClaimantName(1);	
		String comment1 = "Approver comment";
		String targetComment1 = "";	
		
		// Perform actions
		claim1.writeComment(comment1);
		
		// Carry out tests
		assertTrue("claim1 should have comment equal to targetComment1.", claim1.getComment().equals(targetComment1));
		
	}	
	
	// Test UC 08.07.01, UC 08.08.01
	public void testApproveOrReturnClaim(){
		
		// Create claims
		ClaimGenerator claimGenerator = new ClaimGenerator();
		Claim claim1 = claimGenerator.generateClaim(1, "Submitted");
		Claim claim2 = claimGenerator.generateClaim(2, "Submitted");	
		
		// Set approver names and target claim statuses
		String approverName1 = "Approver 1";
		String approverName2 = "Approver 2";
		String targetStatus1 = "Approved.";	
		String targetStatus2 = "returned";		
		
		// Perform actions
		claim1.approveClaim(approverName1);
		claim2.returnClaim(approverName2);
		
		// Carry out tests
		assertTrue("claim1 should have approverName1.", claim1.getApproverName().equals(approverName1));
		assertTrue("claim2 should have approverName2.", claim1.getApproverName().equals(approverName2));		
		assertTrue("claim1 should be approved", claim1.getStatus().equals(targetStatus1));
		assertTrue("claim2 should be returned.", claim2.getStatus().equals(targetStatus2));
		
	}
	
	// Test UC 08.07.01, UC 08.08.01	
	public void testApproverOrReturnMyClaim(){

		// Create a claim
		ClaimGenerator claimGenerator = new ClaimGenerator();
		Claim claim1 = claimGenerator.generateClaim(1, "Submitted");	
		
		// Set approver name to claimant name; set target claim status
		String approverName1 = claim1.getClaimantName(1);	
		String targetStatus = "Submitted";		
		
		// Perform actions
		claim1.approveClaim(approverName1);

		// Carry out tests
		assertTrue("claim1 should not be approved.", claim1.getStatus().equals(targetStatus));	

		// Perform actions
		claim1.returnClaim(approverName1);

		// Carry out tests
		assertTrue("claim1 should not be returned.", claim1.getStatus().equals(targetStatus));
		
	}
	
}

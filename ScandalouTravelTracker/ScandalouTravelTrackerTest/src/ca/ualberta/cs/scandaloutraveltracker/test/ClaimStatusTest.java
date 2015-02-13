package ca.ualberta.cs.scandaloutraveltracker.test;

import junit.framework.TestCase;

public class ClaimStatusTest extends TestCase {
	//Tests for US07.01.01
	//test default claim status
	public void testClaimStatus(){
		Claim testclaim= new Claim();
		Expense testexpense=new Expense();
		testclaim.addExpenseitem(testexpense);
		testclaim.get().claimStatus();
		assertTrue("claim has wrong status", testclaim.get().claimStatus()=="pending");
	}
	
	//US07.01.01
	//test the status of the claim changed to submitted
	public void testSubmitClaim(){
		testclaim.addExpenseitem(testexpense);
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		Claim claim1 = new Claim(claimName1);
		claim1.setStatus(claimStatus1);
		assertTrue("Claim is Submitted", claim1.get().claimStatus()=="Submitted");
	}
	//US07.02.01
	public void testWarningPopup(){
		
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		Claim testclaim= new Claim(claimName1);
		testclaim.raiseWarning();
		assertTrue("Warning is raised",testclaim.getWarning()==testclaim.raiseWarning());
		
	}
	//US07.03.01
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
	//US07.04.01
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
	//US07.05.01
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

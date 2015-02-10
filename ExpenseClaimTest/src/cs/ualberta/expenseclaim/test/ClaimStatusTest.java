package cs.ualberta.expenseclaim.test;

import junit.framework.TestCase;

public class ClaimStatusTest extends TestCase {
	public void testClaimStatus(){
		Claim testclaim= new Claim();
		Expense testexpense=new Expense();
		testclaim.addExpenseitem(testexpense);
		testclaim.get().claimstatus();
		assertTrue("claim has wrong status", testclaim.get().claimstatus()=="pending");
	}
	
	public void testSubmitClaim(){
		Claim testclaim= new Claim();
		Expense testexpense=new Expense();
		testclaim.addExpenseitem(testexpense);
		
	}
	public void testWarningPopup(){
		Claim testclaim= new Claim();
		Expense testexpense=new Expense();
		testclaim.addExpenseitem(testexpense);
	}
	
	public void testClaimNotApproved(){
		Claim testclaim= new Claim();
		Expense testexpense=new Expense();
		testclaim.addExpenseitem(testexpense);
	}
	public void testClaimApproved(){
		Claim testclaim= new Claim();
		Expense testexpense=new Expense();
		testclaim.addExpenseitem(testexpense);
	}
	public void testApproverInfo(){
		Claim testclaim= new Claim();
		Expense testexpense=new Expense();
		testclaim.addExpenseitem(testexpense);
	}
}

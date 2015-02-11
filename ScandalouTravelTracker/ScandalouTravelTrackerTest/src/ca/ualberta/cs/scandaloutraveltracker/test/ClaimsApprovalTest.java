package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;

import junit.framework.TestCase;

public class ClaimsApprovalTest extends TestCase {

	public void testEmptyClaimList(){
		
		ClaimList claimList1 = new ClaimList();
		assertTrue("The claim list should be empty.", claimList1.size() == 0);
	}
	
	public void testGetSubmittedClaims(){
		
		// Create a claim list
		ClaimList claimList1 = new ClaimList();
		
		// Create a submitted claim
		String claimName1 = "Claim 1";
		String claimStatus1 = "Submitted";
		Claim claim1 = new Claim(claimName1);
		claim1.setStatus(claimStatus1);

		// Create a nonsubmitted claim
		String claimName2 = "Claim 2";
		String claimStatus2 = "In progress";
		Claim claim2 = new Claim(claimName2);
		claim2.setStatus(claimStatus2);
		
		// Populate the claim list
		claimList1.addClaim(claim1);		
		claimList1.addClaim(claim2);		
		
		// Carry out tests
		ArrayList<Claim> claims1 = claimList1.getSubmittedClaims();
		assertTrue("claims1 should contain 1 claim.", claims1.size() == 1);
		assertTrue("claims1 should claim2", claims1.contains(claim1));
		assertTrue("claims1 should not contain claim2", claims1.contains(claim2));
	}
	
	public void testClaimListOrder(){
		
		// Create a claim list
		ClaimList claimList1 = new ClaimList();
		
		// Create a claim
		String claimName1 = "Claim 1";
		int startYear1 = 2015;
		int startMonth1 = 2;
		int startDay1 = 1;
		Claim claim1 = new Claim(claimName1);
		claim1.setStartYear(startYear1);
		claim1.setStartMonth(startMonth1);
		claim1.setStartDay(startDay1);
		
		// Create another claim
		String claimName2 = "Claim 2";
		int startYear2 = 2015;
		int startMonth2 = 1;
		int startDay2 = 31;		
		Claim claim2 = new Claim(claimName2);
		claim2.setStartYear(startYear2);
		claim2.setStartMonth(startMonth2);
		claim2.setStartDay(startDay2);		
		
		// Populate the claim list
		claimList1.addClaim(claim1);
		claimList1.addClaim(claim2);
		
		// Carry out tests
		ArrayList<Claim> claims1 = claimList1.getClaims();
		assertTrue("claimName of the first claim in claims1 should be claimName2", claims1.get(0).getName().equals(claimName2));
	}
	
	public void testGetExpenses(){
		
	}
	
	public void testAddApproverComment(){
		
		// Create a claim
		String claimName1 = "Claim 1";
		String claimStatus1 = "In Progress";
		String comment1 = "Approver comment";
		Claim claim1 = new Claim();
		
		// Perform action
		claim1.writeComment(comment1);
		
		// Carry out tests
		assertTrue("claim1 should have comment equal to an empty string.", claim1.getComment().equals(""));
		
		// Perform actions
		String claimStatus2 = "Submitted";
		claim1.setStatus(claimStatus2);
		claim1.writeComment(comment1);		
		
		// Carry out tests
		assertTrue("claim1 should have comment equal to comment1.", claim1.getComment().equals(comment1));
		
	}
	
	public void testApproveOrReturnClaim(){
		
		// Set approver name
		String approverName1 = "Dick T. Approver";
		
		// Create submitted claims
		String claimName1 = "Claim 1";
		String status1 = "Submitted";
		Claim claim1 = new Claim();
		String claimName2 = "Claim 2";
		String status2 = "Submitted";
		Claim claim2 = new Claim();
		
		// Perform actions
		claim1.approveClaim();
		claim2.returnClaim();
		
		// Carry out tests
		assertTrue("claim1 should have status1", claim1.getStatus().equals(status1));
		assertTrue("claim2 should have status2.", claim2.getStatus().equals(status2));
		assertTrue("claim1 should have approverName equal to approverName1.", claim1.getApproverName().equals(approverName1));
		assertTrue("claim2 should have approverName equal to approverName1.", claim1.getApproverName().equals(approverName1));
		
	}
	
	
	boolean updated=false;
	public void testNotifyListeners(){
		ClaimList claimlist = new ClaimList();
		updated= false;
		Listener 1 = new Listener(){
			public void update(){
					ClaimListTest.this.updated=true;
			}
		};
		ClaimList.addListener(1);
		Claim testClaim=new Claim("New");
		ClaimList.addClaim(testClaim);
		assertTrue("ClaimList didnt fire an update", this.updated);
		updated=false;
		claimlist.removeClaim(testClaim);
		assertTrue("Removing claim from claimlist didnt fire an update off", this.updated);
	}
	
}

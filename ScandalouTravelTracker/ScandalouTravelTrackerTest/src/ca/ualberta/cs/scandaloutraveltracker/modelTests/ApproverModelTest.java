package ca.ualberta.cs.scandaloutraveltracker.modelTests;

import junit.framework.TestCase;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.models.Approver;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;

public class ApproverModelTest extends TestCase {

	// When the approver approves a claim, the claim should be
	// in submitted mode and when the approver finishes approving
	// the claim, it should be in approved mode and cannot be edited
	// Also checks that the approver's name and comment is attached
	// US07.04.01
	// US07.05.01
	// US08.06.01
	// US08.08.01
	public void testApproveClaim() {
		
		// Try to approve a claim that is not submitted
		Approver approver = new Approver();
		approver.setName("Test Approver");
		Claim submittedClaim = new Claim();
		submittedClaim.setStatus(Constants.statusInProgress);
		approver.approveClaim(submittedClaim, "Approving Claim");
		assertFalse(submittedClaim.getStatus().equals(Constants.statusApproved));
		assertTrue(submittedClaim.getCanEdit());
		
		// Approve a claim that is submitted
		submittedClaim.setStatus(Constants.statusSubmitted);
		approver.approveClaim(submittedClaim, "Approving Claim");
		assertTrue(submittedClaim.getStatus().equals(Constants.statusApproved));
		assertFalse(submittedClaim.getCanEdit());
		assertTrue(submittedClaim.getApproverName().equals("Test Approver"));
		assertTrue(submittedClaim.getApproverComments().get(0).equals("Approving Claim"));
	}
	
	// When the approver returns a claim, the claim should be in 
	// submitted mode and when the approver returns the claim, it
	// should be in returned mode and can be edited
	// Also checks that the approver's name and comment is attached
	// US07.03.01
	// US07.05.01
	// US08.06.01
	// US08.07.01
	public void testReturnClaim() {
		// Try to approve a claim that is not submitted
		Approver approver = new Approver();
		approver.setName("Test Approver");
		Claim submittedClaim = new Claim();
		submittedClaim.setStatus(Constants.statusInProgress);
		approver.returnClaim(submittedClaim, "Returned Claim");
		assertFalse(submittedClaim.getStatus().equals(Constants.statusApproved));
		assertTrue(submittedClaim.getCanEdit());
		
		// Approve a claim that is submitted
		submittedClaim.setStatus(Constants.statusSubmitted);
		approver.returnClaim(submittedClaim,  "Returned Claim");
		assertTrue(submittedClaim.getStatus().equals(Constants.statusReturned));
		assertTrue(submittedClaim.getCanEdit());
		assertTrue(submittedClaim.getApproverName().equals("Test Approver"));
		assertTrue(submittedClaim.getApproverComments().get(0).equals("Returned Claim"));
	}

}

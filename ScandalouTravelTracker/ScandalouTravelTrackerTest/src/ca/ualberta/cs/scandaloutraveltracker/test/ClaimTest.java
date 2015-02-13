package ca.ualberta.cs.scandaloutraveltracker.test;

import android.test.ActivityInstrumentationTestCase2;

public class ClaimTest extends ActivityInstrumentationTestCase2<Claim> {
	
	public void testNewClaim() {
	    String name = "test";
	    Date sDate = new Date(123)
	    Date eDate = new Date(456)
	    Claim newClaim = new Claim(name, sDate, eDate);
	    assertTrue("Names should match", newClaim.getName().equals(name));
	    assertTrue("Start date should match", newClaim.getStartDate().equals(sDate));
	    assertTrue("End date should match", newClaim.getEndDate().equals(eDate));
	}
	
	public void testAddDestinaion() {
	    Claim newClaim = new Claim();
	    String l1 = "Place";
	    String r1 = "Reason";
	    String l2 = "Place 2";
	    Stirng r2 = "Reason 2";
	    Destination newDestination = new (l1, r1);
	    Destination secDestination = new (l2, r2);
	    assertFalse("The two destinations should be different", 
	                newDestination.equals(secDestination));
	    assertTrue("Place should match", newDestination.getLocation.equals(l1));
	    assertTrue("Reason should match", newDesination.getReason.equals(r2));
	}
	
	public void testClaimDisplayed() {
	    ViewClaimActivity activity = startWithClaim();
	    View allViews = activity.getWindow().getDecorView();
	    TextView claimName = (TextView) activity.findViewById(R.id.claimNameText);
	    TextView startDate = (TextView) activity.findViewById(R.id.startDateText);
	    TextView endDate = (TextView) activity.findViewById(R.id.endDateText);
	    ViewAsserts.assertOnScreen(allViews, (View) claimName);
	    ViewAsserts.assertOnScreen(allViews, (View) startDate);
	    ViewAsserts.assertOnScreen(allViews, (View) endDate);
	}
	
	public void testCantEditClaim() {
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    String diffName = "tester";
	    Date diffSDate = new Date(789);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    newClaim.setCanEdit(false);
	    newClaim.setName(diffName);
	    newClaim.setStartDate(diffSDate);
	    assertTrue("Name should not have changed", newClaim.getName.equals(name));
	    assertTrue("Start date should not have changed", 
	                newClaim.getStartDate.equals(sDate);
	    assertTrue("End date should not have changed", 
	                newClaim.getEndDate.equals(eDate));
	}

	public void testCanEditClaim() {
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    String diffName = "tester";
	    Date diffSDate = new Date(789);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    newClaim.setCanEdit(true);
	    newClaim.setName(diffName);
	    newClaim.setStartDate(diffSDate);
	    assertTrue("Name should have changed", newClaim.getName.equals(diffName));
	    assertTrue("Start date should have changed", 
	                newClaim.getStartDate.equals(diffSDate);
	    assertTrue("End date should not have changed", 
	                newClaim.getEndDate.equals(eDate));
	}
	
	public void testCantDeleteClaim() {
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    ClaimList claimsList = new ClaimList();
	    claimsList.add(newClaim);
	    newClaim.setCanEdit(false);
	    claimsList.remove(newClaim);
	    assertEquals("Count should still be one", claimsList.getCount(), 1);
	}

	public void testCanDeleteClaim() {
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    ClaimList claimsList = new ClaimList();
	    claimsList.add(newClaim);
	    newClaim.setCanEdit(true);
	    claimsList.remove(newClaim);
	    assertEquals("Count should be zero", claimsList.getCount(), 0);
	}
	
	public void testSavedData() {
	    String name = "Justin";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    ClaimList claims = new ClaimList();
	    Claim claim = new Claim(name, sDate, eDate); 
	    claims.add(claim);
	    claims.saveList();

	    ClaimList claims2 = new ClaimList();
	    claims2.loadList();

	    assertEquals("Two claim lists are not equal", claims, claims2);
	}
}

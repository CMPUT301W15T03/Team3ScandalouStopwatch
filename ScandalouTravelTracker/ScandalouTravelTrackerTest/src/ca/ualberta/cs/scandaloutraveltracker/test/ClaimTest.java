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

import java.util.Date;

import junit.framework.TestCase;

import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimList;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListActivity;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.ViewClaimActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.TextView;

public class ClaimTest extends ActivityInstrumentationTestCase2<ClaimListActivity> {
	
	public ClaimTest() {
		super(ClaimListActivity.class);
	}
	
	// Test UC 01.01.01
	public void testNewClaim() {
	    String name = "test";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);
	    Claim newClaim = new Claim(name, sDate, eDate);
	    assertTrue("Names should match", newClaim.getName().equals(name));
	    assertTrue("Start date should match", newClaim.getStartDate().equals(sDate));
	    assertTrue("End date should match", newClaim.getEndDate().equals(eDate));
	}
	
	// Test UC 01.02.01
	public void testAddDestinaion() {
	    Claim newClaim = new Claim();
	    String l1 = "Place";
	    String r1 = "Reason";
	    String l2 = "Place 2";
	    String r2 = "Reason 2";
	    Destination newDestination = new (l1, r1);
	    Destination secDestination = new (l2, r2);
	    assertFalse("The two destinations should be different", 
	                newDestination.equals(secDestination));
	    assertTrue("Place should match", newDestination.getLocation.equals(l1));
	    assertTrue("Reason should match", newDesination.getReason.equals(r2));
	}
	
	// Test UC 01.03.01
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
	
	private ViewClaimActivity startWithClaim() {
		// TODO Give a Claim for the test to start with
		return null;
	}

	// Test UC 01.04.01
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
	    assertTrue("Name should not have changed", newClaim.getName().equals(name));
	    assertTrue("Start date should not have changed", 
	                newClaim.getStartDate().equals(sDate));
	    assertTrue("End date should not have changed", 
	                newClaim.getEndDate().equals(eDate));
	}

	// Test UC 01.04.01
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
	    assertTrue("Name should have changed", newClaim.getName().equals(diffName));
	    assertTrue("Start date should have changed", 
	                newClaim.getStartDate().equals(diffSDate));
	    assertTrue("End date should not have changed", 
	                newClaim.getEndDate().equals(eDate));
	}

	// Test UC 01.05.01	
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

	// Test UC 01.05.01
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
	
	// Test UC 01.06.01
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

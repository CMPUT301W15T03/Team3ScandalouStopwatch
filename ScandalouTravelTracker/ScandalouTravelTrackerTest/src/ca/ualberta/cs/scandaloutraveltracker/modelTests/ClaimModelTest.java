package ca.ualberta.cs.scandaloutraveltracker.modelTests;

import java.util.Date;

import android.location.Location;

import junit.framework.TestCase;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Destination;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.models.User;
import ca.ualberta.cs.scandaloutraveltracker.test.ClaimGenerator;

public class ClaimModelTest extends TestCase {
	
	// Create an expense claim that records a name, starting date of travel, and ending date of travel
	// Also tests that changes can be made to the claim
	// US1.01.01 and US1.04.01
	public void testMakeBasicClaim() {
		ClaimGenerator cg = new ClaimGenerator();
		Claim newClaim = new Claim();
		User newUser = new User();
		Date startDate = cg.createDate(0, 1, 2015);
		Date endDate = cg.createDate(0, 3, 2015);
		
		newUser.setName("Kendrick Lamar");
		newClaim.setUser(newUser);
		newClaim.setStartDate(startDate);
		newClaim.setEndDate(endDate);
		
		assertTrue(newClaim.getUser().getName().equals("Kendrick Lamar"));
		assertTrue(newClaim.getStartDate().equals(startDate));
		assertTrue(newClaim.getEndDate().equals(endDate));
	}
	
	// Creates a new claim and add two destinations, both with a reason for travel
	// US1.02.01
	public void testAddDestinationsToClaim() {
		Claim newClaim = new Claim();
		Destination d1 = new Destination("Harlem", "Meeting with Ferg");
		Destination d2 = new Destination("Bedstuy", "Meeting Biggie");
		newClaim.addDestination(d1);
		newClaim.addDestination(d2);
		
		Destination d3 = newClaim.getDestinations().get(0);
		Destination d4 = newClaim.getDestinations().get(1);
		
		assertEquals(d1, d3);
		assertEquals(d2, d4);
	}
	
	// Edits an expense claim (allowed)
	// US1.04.01
	public void testCantMakeClaimChanges() {
		ClaimGenerator cg = new ClaimGenerator();
		Claim newClaim = new Claim();
		Date startDate = cg.createDate(0, 1, 2015);
		Date endDate = cg.createDate(0, 3, 2015);
		Destination d1 = new Destination("Test", "Test");
		Expense e1 = new Expense();
		
		// Edits the claim while in "can edit"
		newClaim.setStartDate(startDate);
		newClaim.setEndDate(endDate);
		newClaim.setDescription("Test");
		newClaim.addDestination(d1);
		newClaim.addExpense(e1);
		newClaim.setCanEdit(false);
		
		// Attempt to change when in "can't edit"
		Date newStartDate = cg.createDate(1, 1, 2015);
		Date newEndDate = cg.createDate(1, 3, 2015);
		Destination d2 = new Destination("Test2", "Test2");
		Expense e2 = new Expense();
		newClaim.setStartDate(newStartDate);
		newClaim.setEndDate(newEndDate);
		newClaim.setDescription("Test 2");
		
		// Attempt to add destinations and expenses
		newClaim.addDestination(d2);
		newClaim.addExpense(e2);
		
		assertEquals(1, newClaim.getDestinations().size());
		assertEquals(1, newClaim.getExpenses().size());
		assertTrue(newClaim.getStartDate().equals(startDate));
		assertTrue(newClaim.getEndDate().equals(endDate));
		assertTrue(newClaim.getDescription().equals("Test"));
		
		// Attempt to remove destinations and expenses
		newClaim.deleteExpense(e1);
		newClaim.removeDestination(d1);
		
		assertEquals(1, newClaim.getDestinations().size());
		assertEquals(1, newClaim.getExpenses().size());
	}
	
	// Creates a new claim and destination and attaches a mock location to
	// the destination and then adds the destination to the claim. The test
	// asserts that the retrieved destination's location from the claim is
	// the same as the one created earlier.
	// US01.07.01
	public void testAttachGeoLocation() {
		Claim newClaim = new Claim();
		Destination newDestination = new Destination("Carson", "Visiting Soul at Magic Disk");
		Location mockLocation = new Location("Mock Provider");
		mockLocation.setLatitude(25.67);
		mockLocation.setLongitude(-36.45);
		newDestination.setLocation(mockLocation);
		newClaim.addDestination(newDestination);
		
		Destination storedDestination = newClaim.getDestinations().get(0);
		assertEquals(storedDestination.getLocation(), mockLocation);
	}
	
}

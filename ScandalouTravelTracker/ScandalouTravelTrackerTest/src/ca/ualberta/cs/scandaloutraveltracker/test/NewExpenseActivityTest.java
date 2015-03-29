package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.NewExpenseActivity;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;

public class NewExpenseActivityTest extends
		ActivityInstrumentationTestCase2<NewExpenseActivity> {

	Instrumentation instrumentation;
	NewExpenseActivity newExpenseActivity;
	Spinner categorySpinner;
	Spinner currencySpinner;
	EditText amountET;
	EditText dateET;
	EditText descriptionET;
	Button addButton;
	
	int newClaimId;
	
	public NewExpenseActivityTest() {
		super(NewExpenseActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		// Create claim and inject mock intent with claimId
		Intent mockIntent = new Intent();
		mockIntent.putExtra(Constants.claimIdLabel, 0);
		setActivityIntent(mockIntent);
		
		instrumentation = getInstrumentation();
		newExpenseActivity = getActivity();
		
		// Create mock claim with start and end dates 03/01/2014 - 03/03/2014
		newClaimId = createMockClaim();
		
		newExpenseActivity.finish();
		setActivity(null);
		mockIntent.putExtra(Constants.claimIdLabel, newClaimId);
		setActivityIntent(mockIntent);
		newExpenseActivity = getActivity();
		
		categorySpinner = (Spinner) newExpenseActivity.findViewById(R.id.category);
		amountET = (EditText) newExpenseActivity.findViewById(R.id.amount2);
		descriptionET = (EditText) newExpenseActivity.findViewById(R.id.description2);
		dateET =  (EditText) newExpenseActivity.findViewById(R.id.date_expense2);
		currencySpinner = (Spinner) newExpenseActivity.findViewById(R.id.currency);
		addButton = (Button) newExpenseActivity.findViewById(R.id.add_expense_button);

	}
	
	// Creates a new expense by filling out all the data on the screen
	// US04.01.01
	public void testCreateExpense() {		
		// Sets 1.00 as amount
		amountET.performClick();
		instrumentation.waitForIdleSync();
		instrumentation.sendStringSync("1.00");
		instrumentation.waitForIdleSync();
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				// Chooses category 3: Vehicle Rental
				categorySpinner.setSelection(3);
				
				// Sets date to 03/01/2014
				newExpenseActivity.setDate(createDate(2, 2, 2014));
				dateET.setText("FILLER");
			
				// Sets currency to JPY
				currencySpinner.setSelection(5);
			}
			
		});
		
		// Sets description to "d2"
		instrumentation.waitForIdleSync();
		descriptionET.performClick();
		instrumentation.waitForIdleSync();
		instrumentation.sendStringSync("d2");
		instrumentation.waitForIdleSync();
		
		performClick(addButton);
		ClaimController cc = new ClaimController(new Claim(newClaimId));
		assertEquals(1, cc.getExpenseList().size());
		
	}

	// Checks the category spinner to ensure the correct values are being displayed
	// US04.02.01
	public void testCategorySpinner() {
		ArrayList<String> categories = new ArrayList<String>();
		
		for (int i = 0; i < categorySpinner.getCount(); i++) {
			String category = categorySpinner.getItemAtPosition(i).toString();
			categories.add(category);
		}
		
		assertTrue(categories.contains("Air Fare"));
		assertTrue(categories.contains("Ground Transport"));
		assertTrue(categories.contains("Vehicle Rental"));
		assertTrue(categories.contains("Private Automobile"));
		assertTrue(categories.contains("Fuel"));
		assertTrue(categories.contains("Parking"));
		assertTrue(categories.contains("Registration"));
		assertTrue(categories.contains("Accomodation"));
		assertTrue(categories.contains("Meal"));
		assertTrue(categories.contains("Supplies"));
	}
	
	// Checks the currency spinner to ensure the correct values are being displayed
	// US04.03.01
	public void testCurrencySpinner() {
		ArrayList<String> currencies = new ArrayList<String>();

		for (int i = 0; i < currencySpinner.getCount(); i++) {
			currencies.add(currencySpinner.getItemAtPosition(i).toString());
		}
		
		assertTrue(currencies.contains("CAD"));
		assertTrue(currencies.contains("USD"));
		assertTrue(currencies.contains("EUR"));
		assertTrue(currencies.contains("GBP"));
		assertTrue(currencies.contains("CHF"));
		assertTrue(currencies.contains("JPY"));
		assertTrue(currencies.contains("CNY"));
	}
	
	private void performClick(final Button button) {
		try {
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {
					button.performClick();
				}
			});
		} catch (Throwable e) {
			fail();
		}
		getInstrumentation().waitForIdleSync();
	}
	
	private int createMockClaim() throws UserInputException {
		// Create user and add to the list
		UserListController ulc = new UserListController();
		int userId = ulc.createUser("User1");
		ulc.addUser(new User(userId));
		
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = new ArrayList<String>();
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		// Month - Day - Year
		Date startDate = createDate(2, 1, 2014);
		Date endDate = createDate(2, 3, 2014);
		
		// Create the claim
		newClaimId = clc.createClaim("a1", startDate, endDate, "d1", destinations, 
				tagsList, status, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
		
		return newClaimId;
	}
	
	private Date createDate(int month, int day, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();
		
		return date;
	}
}

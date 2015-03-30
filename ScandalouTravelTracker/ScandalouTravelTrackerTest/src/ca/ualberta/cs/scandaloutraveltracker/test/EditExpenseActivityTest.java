package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.EditText;
import android.widget.Spinner;
import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.Destination;
import ca.ualberta.cs.scandaloutraveltracker.EditExpenseActivity;
import ca.ualberta.cs.scandaloutraveltracker.Expense;
import ca.ualberta.cs.scandaloutraveltracker.StateSpinner;
import ca.ualberta.cs.scandaloutraveltracker.User;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.UserListController;

public class EditExpenseActivityTest extends
		ActivityInstrumentationTestCase2<EditExpenseActivity> {
	
	EditExpenseActivity editExpenseActivity;
	Instrumentation instrumentation;
	EditText description;
	EditText date;
	EditText cost;
	StateSpinner category;
	StateSpinner currencyType;
	int newClaimId;
	
	public EditExpenseActivityTest() {
		super(EditExpenseActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Intent mockIntent = new Intent();
		mockIntent.putExtra(Constants.claimIdLabel, 0);
		mockIntent.putExtra("expenseId", 0);
		setActivityIntent(mockIntent);
		editExpenseActivity = getActivity();
		
		newClaimId = createMockClaim(true);
		editExpenseActivity.finish();
		setActivity(null);
		mockIntent = new Intent();
		Long expenseId = (long) 0;
		mockIntent.putExtra(Constants.claimIdLabel, newClaimId);
		mockIntent.putExtra("expenseId", expenseId);
		setActivityIntent(mockIntent);
		
		editExpenseActivity = getActivity();
		instrumentation = getInstrumentation();
		
		description = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.description);
		date = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.date_expense);
		cost = (EditText) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.amount);
		category = (StateSpinner) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.catspinner);
		currencyType = (StateSpinner) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.currencyspinner);
	}
	
	// Tests that all of the passed expense data is shown on the screen
	// US04.05.01
	public void testExpenseDataShown() {
		assertTrue(category.getSelectedItem().toString().equals("Registration"));
		assertTrue(cost.getText().toString().equals("2.25"));
		assertTrue(currencyType.getSelectedItem().toString().equals("CAD"));
		assertTrue(date.getText().toString().equals("03/16/2014"));
		assertTrue(description.getText().toString().equals("Late Registration"));
	}
	
	public void testCanEditExpense() {
		TouchUtils.clickView(this, category);
		assertTrue(category.hasBeenOpened());
	}
	
	public void testCantEditExpense() {
		// Reset the activity with a claim that cant be edited
		Intent mockIntent = new Intent();
		try {
			newClaimId = createMockClaim(false);
		} catch (UserInputException e) {
			fail();
		}
		editExpenseActivity.finish();
		setActivity(null);
		mockIntent = new Intent();
		Long expenseId = (long) 0;
		mockIntent.putExtra(Constants.claimIdLabel, newClaimId);
		mockIntent.putExtra("expenseId", expenseId);
		setActivityIntent(mockIntent);
		editExpenseActivity = getActivity();

		// Verify that either toast appear or spinner has not been opened
		category = (StateSpinner) editExpenseActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.catspinner);
		TouchUtils.clickView(this, category);
		assertFalse(category.hasBeenOpened());
		
	}
	
	private int createMockClaim(boolean editable) throws UserInputException {
		// Create two users and add them to the list
		UserListController ulc = new UserListController();
		int userId = ulc.createUser("User1");
		ulc.addUser(new User(userId));
		
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = new ArrayList<String>();
		boolean canEdit = editable;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		// Setting claim test data
		Destination testDestination = new Destination("Brooklyn", "Gotta see Jay");
		destinations.add(testDestination);
		testDestination = new Destination("Brookyln", "Meet up with Rocky");
		destinations.add(testDestination);
		
		String testTag = "#stoked";
		tagsList.add(testTag);
		testTag = "#NY";
		tagsList.add(testTag);
		
		Expense testExpense = createTestExpense();
		expenses.add(testExpense);
		
		// Month - Day - Year
		Date startDate = createDate(2, 15, 2014);
		Date endDate = createDate(2, 17, 2014);
		
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
	
	private Expense createTestExpense() {
		Expense newExpense = new Expense();
		
		newExpense.setCategory("Registration");
		newExpense.setDescription("Late Registration");
		newExpense.setCost(2.25);
		newExpense.setCurrencyType("CAD");
		newExpense.setDate(createDate(2,16,2014));
		newExpense.setReceiptAttached(false);
		
		return newExpense;
	}
}

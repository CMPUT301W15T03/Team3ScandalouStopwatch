package ca.ualberta.cs.scandaloutraveltracker.test;

import android.app.Instrumentation;
import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.User;
import ca.ualberta.cs.scandaloutraveltracker.views.ClaimListActivity;

public class ClaimListActivityDestinationTest extends
		ActivityInstrumentationTestCase2<ClaimListActivity> {
	
	ClaimListActivity claimListActivity;
	ClaimGenerator cg;
	ListView claimsListView;
	Instrumentation instrumentation;
	int newUserId;

	public ClaimListActivityDestinationTest() {
		super(ClaimListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		
		// Launch activity to get context
		Intent mockIntent = new Intent();
		mockIntent.putExtra("userId", 0);
		claimListActivity = (ClaimListActivity) getActivity();
		cg = new ClaimGenerator();
		
		// Create one mock user with 3 claims - all different distance
		// to the users mock home location
		UserListController userListController = new UserListController();
		newUserId = userListController.createUser("Test User");
		UserController uc = new UserController(new User(newUserId));
		Location l1 = new Location("Mock Location");
		l1.setLatitude(0);
		l1.setLongitude(0);
		uc.setCurrentLocation(l1);
		mockIntent = new Intent();
		mockIntent.putExtra("userId", newUserId);
		
		cg.createClaimsLocationsAttached(newUserId);
		claimListActivity.finish();
		setActivity(null);
		
		// Inject activity with mock intent
		setActivityIntent(mockIntent);
		claimListActivity = (ClaimListActivity) getActivity();
		
		claimsListView = (ListView) claimListActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.claimListActivityList);
		
		instrumentation = getInstrumentation();
	}
	
	public void testDestinationColors() {
		
	}
}

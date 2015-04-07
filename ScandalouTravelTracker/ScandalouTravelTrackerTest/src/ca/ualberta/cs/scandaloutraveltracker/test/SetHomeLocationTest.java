package ca.ualberta.cs.scandaloutraveltracker.test;

import org.osmdroid.views.MapView;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.User;
import ca.ualberta.cs.scandaloutraveltracker.views.SetHomeLocationActivity;

public class SetHomeLocationTest extends ActivityInstrumentationTestCase2<SetHomeLocationActivity> {
	
	int newUserId;
	SetHomeLocationActivity setHomeLocationActivity;
	MapView map;
	Button gpsButton;
	Button saveLocationButton;
	
	public SetHomeLocationTest() {
		super(SetHomeLocationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		
		// Launch activity to get context
		Intent mockIntent = new Intent();
		mockIntent.putExtra("userId", 0);
		setActivityIntent(mockIntent);
		setHomeLocationActivity = (SetHomeLocationActivity) getActivity();
		
		// Create mock user with a mock location
		UserListController userListController = new UserListController();
		newUserId = userListController.createUser("Test User");
		UserController uc = new UserController(new User(newUserId));
		Location l1 = new Location("Mock Location");
		l1.setLatitude(20);
		l1.setLongitude(-20);
		uc.setCurrentLocation(l1);
		
		mockIntent = new Intent();
		mockIntent.putExtra("userId", newUserId);
		setActivityIntent(mockIntent);
		setHomeLocationActivity = (SetHomeLocationActivity) getActivity();
		
		// Get layout elements
		map = (MapView) setHomeLocationActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.map);
		gpsButton = (Button) setHomeLocationActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.goGpsButton);
		saveLocationButton = (Button) setHomeLocationActivity.findViewById(ca.ualberta.cs.scandaloutraveltracker.R.id.saveLocationButton);
	}
	
	// Tests that on the location set screen you can set your locatin via a map
	// or a GPS.
	// US10.02.01
	public void testMapAndGPSShown() {
		assertTrue(map.isShown());
		assertTrue(map.isInTouchMode());
		assertTrue(gpsButton.isShown());
		assertTrue(gpsButton.isClickable());
		assertTrue(saveLocationButton.isShown());
		assertTrue(saveLocationButton.isClickable());
		ClaimGenerator cg = new ClaimGenerator();
		cg.resetState(ClaimApplication.getContext());
	}
	
	
}

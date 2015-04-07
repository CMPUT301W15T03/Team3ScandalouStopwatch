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

import android.app.Instrumentation;
import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
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
	
	// Test updates the progress counter based on the color that each claim
	// should have. 1 for green, 2 for yellow, and 3 for red. This test
	// has one of each color so progressCounter should equal 6 if the
	// colors are displayed properly
	// US02.03.01
	public void testDestinationColors() {
		int count = claimsListView.getChildCount();
		int progressCounter = 0;
		
		for (int i = 0; i < count; i++) {
			Claim currentClaim = (Claim) claimsListView.getItemAtPosition(i);
			int progress = currentClaim.getProgress();
			
			if (0 < progress && progress < 25) {
				progressCounter += 1;
			}
			else if (25 < progress && progress < 75) {
				progressCounter += 2;
			}
			else if (progress > 75) {
				progressCounter += 3;
			}
		}
		
		assertEquals(6, progressCounter);
		cg.resetState(ClaimApplication.getContext());
	}
}

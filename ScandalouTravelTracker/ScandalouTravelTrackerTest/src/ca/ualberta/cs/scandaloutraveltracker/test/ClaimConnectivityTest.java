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

import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimList;
import ca.ualberta.cs.scandaloutraveltracker.Connection;

import android.test.ActivityInstrumentationTestCase2;

public class ClaimConnectivityTest extends
		ActivityInstrumentationTestCase2<Connection> {

	public ClaimConnectivityTest(Class<Connection> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	// Test UC 09.01.01
	public void testDataPush() {
	    String text = "Charles";
	    Date sDate = new Date(123);
	    Date eDate = new Date(456);

	    ClaimList claims = new ClaimList();
	    Claim claim = new Claim(text, sDate, eDate);
	    Connection connect = new Connection();
	    connect.getConnection();
	    connect.save(claims);

	    ClaimList claims2 = new ClaimList();
	    connect.load(claims2);

	    assertEquals("Loaded claim should be the same", claims, claims2);
	}
	
}

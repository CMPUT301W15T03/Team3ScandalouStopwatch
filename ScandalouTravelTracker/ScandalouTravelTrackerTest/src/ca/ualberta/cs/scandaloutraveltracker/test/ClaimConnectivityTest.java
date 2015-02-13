package ca.ualberta.cs.scandaloutraveltracker.test;

import android.test.ActivityInstrumentationTestCase2;

public class ClaimConnectivityTest extends
		ActivityInstrumentationTestCase2<Connection> {

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

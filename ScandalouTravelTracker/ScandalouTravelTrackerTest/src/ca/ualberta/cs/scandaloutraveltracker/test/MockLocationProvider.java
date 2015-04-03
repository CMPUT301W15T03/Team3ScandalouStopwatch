/*
 * https://mobiarch.wordpress.com/2012/07/17/testing-with-mock-location-data-in-android/
 * 04/03/2015
 */

package ca.ualberta.cs.scandaloutraveltracker.test;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;

public class MockLocationProvider {
	String providerName;
	Context context;
	
	public MockLocationProvider(String providerName, Context context) {
		this.providerName = providerName;
		this.context = context;
		
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		lm.addTestProvider(providerName, false, false, false, false, false, true, true, 0, 5);
		lm.setTestProviderEnabled(providerName, true);
	}
	
	public void pushLocation (double latitude, double longitude) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location mockLocation = new Location(providerName);
		mockLocation.setLatitude(latitude);
		mockLocation.setLongitude(longitude);
		mockLocation.setAltitude(0);
		mockLocation.setTime(System.currentTimeMillis());
		mockLocation.setAccuracy(5);
		mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
		lm.setTestProviderLocation(providerName, mockLocation);
	}
	
	public void shutdown() {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		lm.removeTestProvider(providerName);
	}
}

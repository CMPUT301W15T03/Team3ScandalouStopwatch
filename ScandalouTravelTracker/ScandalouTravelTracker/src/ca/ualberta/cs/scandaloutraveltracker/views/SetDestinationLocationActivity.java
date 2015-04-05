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

package ca.ualberta.cs.scandaloutraveltracker.views;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import ca.ualberta.cs.scandaloutraveltracker.R;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 	Allows the user to pick a spot on a map to be able to save it as the new destination location.
 * 	If a location is passed to this activity as two intents, one latitude and one longitude, then 
 * 	it is displayed as the previous location
 * @author Team3ScandalouStopwatch
 *
 */
public class SetDestinationLocationActivity extends MenuActivity {

	private LocationManager lm;
	private MapView map;
	private TextView locationTV;
	private IMapController mapController;
	private MapEventsReceiver mapReceiver;
	private Marker newLocation;
	private Marker currentLocation;
	private Location previousLocation;
	private int position;
	
	/**
	 * 	Called when the activity is created. Sets up the map and adds a marker if a location 
	 * 	was passed as intents to the activity. A listener is also made for long clicks on 
	 * 	spots on the map that sets a marker and the spot long clicked on as the new location.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_location);
		
        Button currentButton = (Button) findViewById(R.id.goCurrentButton);
        currentButton.setText("Prev");
		
		//https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library 2015-03-30
		map = (MapView) findViewById(R.id.map);
		map.setTileSource(TileSourceFactory.MAPNIK);
		map.setBuiltInZoomControls(true);
		map.setMultiTouchControls(true);
		mapController = map.getController();
		mapController.setZoom(9);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationTV = (TextView) findViewById(R.id.displayPickedLocationTextView);
		
		// getting intent values
		Intent intent = getIntent();
		position = intent.getIntExtra("destinationPosition",-1);
		previousLocation = new Location("Destination Location");
		previousLocation.setLatitude(intent.getDoubleExtra("latitude",999));
		previousLocation.setLongitude(intent.getDoubleExtra("longitude",999));
		GeoPoint startPoint;
        try {
        	if ((previousLocation.getLatitude() == 999)||(previousLocation.getLongitude() == 999)){
        		throw new NullPointerException();
        	}
    		startPoint = new GeoPoint(previousLocation);
    		currentLocation = new Marker(map);
        	currentLocation.setPosition(startPoint);
        	currentLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        	currentLocation.setTitle("Set Destination Location");
        	currentLocation.showInfoWindow();
			map.getOverlays().add(currentLocation);
			map.invalidate();
			locationTV.setText("Set Destination location\nLatitude: " + 
					previousLocation.getLatitude() + "\nLongitude: " 
						+ previousLocation.getLongitude());
			mapController.setCenter(startPoint);
    	}
	    catch (NullPointerException e) {
		    try {
		    	new GeoPoint(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		    	startPoint = new GeoPoint(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		    	mapController.setCenter(startPoint);
		    	}
		    catch (NullPointerException e2) {
		    	startPoint = new GeoPoint(53.533333, -113.5);
		    	mapController.setCenter(startPoint);
		    	}
    	}
        
        //http://stackoverflow.com/questions/16402722/longpress-on-osmdroid-map-is-not-working 2015-03-30
        mapReceiver = new MapEventsReceiver() {
			
        	// Does not need to do anything
			@Override
			public boolean singleTapConfirmedHelper(GeoPoint geo) {
				return false;
			}
			
			// Sets a marker to the current point and deletes old new location marker
			//https://code.google.com/p/osmbonuspack/wiki/Tutorial_0 2015-03-31
			@Override
			public boolean longPressHelper(GeoPoint geo) {
				locationTV.setText("Picked new destination location\nLatitude: " + 
		        		geo.getLatitude() + "\nLongitude: " + geo.getLongitude());
				map.getOverlays().remove(newLocation);
				newLocation = new Marker(map);
				newLocation.setPosition(geo);
				newLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
				newLocation.setTitle("New Destination Location");
				newLocation.showInfoWindow();
				map.getOverlays().add(newLocation);
				map.invalidate();
				return false;
			}
		};
		MapEventsOverlay OverlayEventos = new MapEventsOverlay(getBaseContext(), mapReceiver);
		map.getOverlays().add(OverlayEventos);
    }
    
    /**
     *  Called when prev button is clicked, the map goes to previous destination location if 
     *  one was passed to the activity as an intent
     * @param v the current view
     */
    public void goCurrent(View v) {
    	if (currentLocation == null) {
    		Toast.makeText(getApplicationContext(),"Destination location not previously set",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	GeoPoint startPoint = new GeoPoint(previousLocation);
        mapController.setCenter(startPoint);
    }
    
    /**
     *  Called when GPS is clicked, the map goes to current gps location
     * @param v the current view
     */
    public void goToGps(View v) {
        try {
        	new GeoPoint(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
    	}
    	catch (NullPointerException e) {
    		Toast.makeText(getApplicationContext(),"GPS currently not available",Toast.LENGTH_SHORT).show();
    		return;
    	}
        GeoPoint startPoint = new GeoPoint(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        mapController.setCenter(startPoint);
    }
    
    /**
     *  Called when save new location is clicked. Saves the location that is set using the map by passing 
     *  the new location back to the activity that called it as an intent
     * @param v the current view
     */
    public void saveLocation(View v) {
    	if (newLocation == null) {
    		Toast.makeText(getApplicationContext(),"Please pick a new location by long clicking a spot on the map."
    				,Toast.LENGTH_SHORT).show();
    		return;
    	}
    	Intent returnIntent = new Intent();
    	returnIntent.putExtra("latitude",newLocation.getPosition().getLatitude());
    	returnIntent.putExtra("longitude",newLocation.getPosition().getLongitude());
    	returnIntent.putExtra("destination",position);
    	setResult(RESULT_OK,returnIntent);
    	finish();
    }
}
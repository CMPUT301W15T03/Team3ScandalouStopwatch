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
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.User;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SetHomeLocationActivity extends MenuActivity {
	private User currentUser;
	private UserController currentUserController;
	private UserListController ulc;
	private LocationManager lm;
	private MapView map;
	private TextView locationTV;
	private IMapController mapController;
	private MapEventsReceiver mapReceiver;
	private Marker newLocation;
	private Marker currentLocation;
	private int userId;
	
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        
        Bundle extras = getIntent().getExtras();
		userId = extras.getInt("userId");
        currentUser = new User(userId);
        currentUserController = new UserController(currentUser);
		
		//https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library 2015-03-30
		map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(9);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationTV = (TextView) findViewById(R.id.displayPickedLocationTextView);
        
        //sets start point to home location if set or gps location if available, else it will default to around Edmonton
        GeoPoint startPoint;
        try {
    		currentUserController.getLocation();
    		startPoint = new GeoPoint(currentUserController.getLocation());
    		currentLocation = new Marker(map);
        	currentLocation.setPosition(new GeoPoint(startPoint));
        	currentLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        	currentLocation.setTitle("Set Home Location");
        	currentLocation.showInfoWindow();
			map.getOverlays().add(currentLocation);
			map.invalidate();
			locationTV.setText("Set home location\nLatitude: " + 
					currentUserController.getLocation().getLatitude() + "\nLongitude: " 
						+ currentUserController.getLocation().getLongitude());
			mapController.setCenter(new GeoPoint(currentUserController.getLocation()));
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
        /*locationTV.setText("Currently picked new home location\nLatitude: " + 
        		startPoint.getLatitude() + "\nLongitude: " + startPoint.getLongitude());*/
        
        //http://stackoverflow.com/questions/16402722/longpress-on-osmdroid-map-is-not-working 2015-03-30
        mapReceiver = new MapEventsReceiver() {
			
        	// Does not need to do anything
			@Override
			public boolean singleTapConfirmedHelper(GeoPoint geo) {
				return false;
			}
			
			// Sets a marker to the current point and deletes old new home location marker
			//https://code.google.com/p/osmbonuspack/wiki/Tutorial_0 2015-03-31
			@Override
			public boolean longPressHelper(GeoPoint geo) {
				locationTV.setText("Picked new home location\nLatitude: " + 
		        		geo.getLatitude() + "\nLongitude: " + geo.getLongitude());
				map.getOverlays().remove(newLocation);
				newLocation = new Marker(map);
				newLocation.setPosition(geo);
				newLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
				newLocation.setTitle("New Home Location");
				newLocation.showInfoWindow();
				map.getOverlays().add(newLocation);
				map.invalidate();
				return false;
			}
		};
		MapEventsOverlay OverlayEventos = new MapEventsOverlay(getBaseContext(), mapReceiver);
		map.getOverlays().add(OverlayEventos);
    }
    
    // When Home button is clicked, goes to home location if one is set
    public void goCurrent(View v) {
    	try {
    		GeoPoint startPoint = new GeoPoint(currentUserController.getLocation());
    	}
    	catch (NullPointerException e) {
    		Toast.makeText(getApplicationContext(),"Home location not previously set",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	GeoPoint startPoint = new GeoPoint(currentUserController.getLocation());
        mapController.setCenter(startPoint);
    }
    
    // When GPS is clicked, goes to current gps location
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
    
    // Saves the location that is set using the map
    public void saveLocation(View v) {
    	if (newLocation == null) {
    		Toast.makeText(getApplicationContext(),"Please pick a new location by long clicking a spot on the map.",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	Location temp = new Location("Home Location Provider");
    	temp.setLatitude(newLocation.getPosition().getLatitude());
    	temp.setLongitude(newLocation.getPosition().getLongitude());
    	currentUserController.setCurrentLocation(temp);
    	ulc = new UserListController();
		ulc.removeUser(userId);
		currentUserController = new UserController(new User(currentUser.getId()));
		ulc.addUser(new User(userId));
		finish();
    }
}
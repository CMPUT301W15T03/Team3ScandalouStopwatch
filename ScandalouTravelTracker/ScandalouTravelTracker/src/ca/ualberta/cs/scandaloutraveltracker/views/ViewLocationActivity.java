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
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import ca.ualberta.cs.scandaloutraveltracker.R;
import android.location.Location;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 	Shows a location on a map if the location is passed as two intents, one for latitude 
 * 	and one for longitude, Otherwise the activity is just viewing a map.
 * @author Team3ScandalouStopwatch
 *
 */
public class ViewLocationActivity extends MenuActivity {

	private MapView map;
	private IMapController mapController;
	private Location previousLocation;
	private Marker currentLocation;
	private TextView locationTV;
	
	/**
	 * 	Called when the activity is created. Sets up the map and adds a marker 
	 * 	if a location was passed as intents to the activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_location);
		map = (MapView) findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(9);
        locationTV = (TextView) findViewById(R.id.view_location_text_view);
        
        Intent intent = getIntent();
        previousLocation = new Location("Location");
        previousLocation.setLatitude(intent.getDoubleExtra("latitude", 999));
        previousLocation.setLongitude(intent.getDoubleExtra("longitude", 999));
        GeoPoint startPoint;
        if ((previousLocation.getLatitude() == 999)||(previousLocation.getLongitude() == 999)){
        	Toast.makeText(getApplicationContext(), "Location not set", Toast.LENGTH_SHORT).show();
        	finish();
        }
    	startPoint = new GeoPoint(previousLocation);
    	currentLocation = new Marker(map);
        currentLocation.setPosition(startPoint);
        currentLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        currentLocation.setTitle("Set Location");
        currentLocation.showInfoWindow();
		map.getOverlays().add(currentLocation);
		map.invalidate();
		locationTV.setText("Set location\nLatitude: " + 
			previousLocation.getLatitude() + "\nLongitude: " 
				+ previousLocation.getLongitude());
		mapController.setCenter(startPoint);
	}
	
	/**
	 * Called when go to set location is pressed. Sets the map to the location that was passed 
	 * to this activity if a location was passed.
	 * @param v - the current view
	 */
	public void goToLocation(View v) {
		GeoPoint startPoint = new GeoPoint(previousLocation);
        mapController.setCenter(startPoint);
	}

}

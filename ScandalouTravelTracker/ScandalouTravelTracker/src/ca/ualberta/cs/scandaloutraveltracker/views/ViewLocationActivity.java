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
import ca.ualberta.cs.scandaloutraveltracker.R.layout;
import ca.ualberta.cs.scandaloutraveltracker.R.menu;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewLocationActivity extends MenuActivity {

	private LocationManager lm;
	private MapView map;
	private IMapController mapController;
	private Location previousLocation;
	private Marker currentLocation;
	private TextView locationTV;
	
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
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationTV = (TextView) findViewById(R.id.view_location_text_view);
        
        Intent intent = getIntent();
        previousLocation = new Location("Expense Location");
        previousLocation.setLatitude(intent.getDoubleExtra("latitude", 999));
        previousLocation.setLongitude(intent.getDoubleExtra("longitude", 999));
        GeoPoint startPoint;
        if ((previousLocation.getLatitude() == 999)||(previousLocation.getLongitude() == 999)){
        	Toast.makeText(getApplicationContext(), "Expense location not set", Toast.LENGTH_SHORT).show();
        	finish();
        }
    	startPoint = new GeoPoint(previousLocation);
    	currentLocation = new Marker(map);
        currentLocation.setPosition(startPoint);
        currentLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        currentLocation.setTitle("Current Expense Location");
        currentLocation.showInfoWindow();
		map.getOverlays().add(currentLocation);
		map.invalidate();
		locationTV.setText("Current Expense location\nLatitude: " + 
			previousLocation.getLatitude() + "\nLongitude: " 
				+ previousLocation.getLongitude());
		mapController.setCenter(startPoint);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_location, menu);
		return true;
	}
	
	public void goToLocation(View v) {
		GeoPoint startPoint = new GeoPoint(previousLocation);
        mapController.setCenter(startPoint);
	}

}

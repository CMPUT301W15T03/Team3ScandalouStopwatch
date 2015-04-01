package ca.ualberta.cs.scandaloutraveltracker.views;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.models.User;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SetExpenseLocationActivity extends MenuActivity {
	private LocationManager lm;
	private MapView map;
	private TextView locationTV;
	private IMapController mapController;
	private MapEventsReceiver mapReceiver;
	private Marker newLocation;
	private Marker currentLocation;
	private Button currentButton;
	
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        currentButton = (Button) findViewById(R.id.goCurrentButton);
        currentButton.setText("Current Geo.");
		
		//https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library 2015-03-30
		map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(9);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationTV = (TextView) findViewById(R.id.displayPickedLocationTextView);
        
        //sets start point to gps location if available, else it will default to around Edmonton
        GeoPoint startPoint;
    	try {
            new GeoPoint(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
           	startPoint = new GeoPoint(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
           	mapController.setCenter(startPoint);
        }
        catch (NullPointerException e2) {
        	startPoint = new GeoPoint(53.533333, -113.5);
        	mapController.setCenter(startPoint);
        }
        
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
				locationTV.setText("Currently picked new expense location\nLatitude: " + 
		        		geo.getLatitude() + "\nLongitude: " + geo.getLongitude());
				map.getOverlays().remove(newLocation);
				newLocation = new Marker(map);
				newLocation.setPosition(geo);
				newLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
				newLocation.setTitle("New Expense Location");
				newLocation.showInfoWindow();
				map.getOverlays().add(newLocation);
				map.invalidate();
				return false;
			}
		};
		MapEventsOverlay OverlayEventos = new MapEventsOverlay(getBaseContext(), mapReceiver);
		map.getOverlays().add(OverlayEventos);
    }
    
    // When Home button is clicked, goes to destination location if one is set
    public void goCurrent(View v) {
    	Toast.makeText(getApplicationContext(),"Does Nothing right now",Toast.LENGTH_SHORT).show();
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
    	Intent returnIntent = new Intent();
    	returnIntent.putExtra("latitude",newLocation.getPosition().getLatitude());
    	returnIntent.putExtra("longitude",newLocation.getPosition().getLongitude());
    	setResult(RESULT_OK,returnIntent);
    	finish();
    }
}
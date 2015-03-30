package ca.ualberta.cs.scandaloutraveltracker;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.events.MapEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SetLocationActivity extends Activity {
	private User currentUser;
	private UserController currentUserController;
	private LocationManager lm;
	private MapView map;
	private TextView locationTV;
	private IMapController mapController;
	private MapEventsReceiver mapReceiver;
	
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        
        currentUser = ( (ClaimApplication) getApplication()).getUser();
		currentUserController = new UserController(currentUser);
		
		//https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library 2015-03-30
		map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        
        mapController = map.getController();
        mapController.setZoom(9);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        //defaults to around edmonton, will need to change
        GeoPoint startPoint = new GeoPoint(53.533333,-113.5);
        mapController.setCenter(startPoint);
        
        locationTV = (TextView) findViewById(R.id.displayPickedLocationTextView);
        locationTV.setText(startPoint.toString());
        
        //http://stackoverflow.com/questions/16402722/longpress-on-osmdroid-map-is-not-working 2015-03-30
        mapReceiver = new MapEventsReceiver() {
			
			@Override
			public boolean singleTapConfirmedHelper(GeoPoint geo) {
				Toast.makeText(getApplicationContext(),"Testing single tap on item",Toast.LENGTH_SHORT).show();
				return false;
			}
			
			@Override
			public boolean longPressHelper(GeoPoint geo) {
				Toast.makeText(getApplicationContext(),"Testing long tap on item",Toast.LENGTH_SHORT).show();
				locationTV.setText(geo.toString());
				return false;
			}
		};
		MapEventsOverlay OverlayEventos = new MapEventsOverlay(getBaseContext(), mapReceiver);
		map.getOverlays().add(OverlayEventos);
    }
    
    // When Home button is clicked, goes to home location if one is set
    public void goHome(View v) {
    	try {
    		currentUserController.getLocation();
    	}
    	catch (NullPointerException e) {
    		Toast.makeText(getApplicationContext(),"Home Location Not Set",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	GeoPoint startPoint = new GeoPoint(currentUserController.getLocation());
        mapController.setCenter(startPoint);
    }
    
    // When GPS is clicked, goes to current gps location
    public void goToGps(View v) {
    	GeoPoint startPoint = new GeoPoint(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        mapController.setCenter(startPoint);
    }
    
    // Saves the location that is set using the map
    public void saveLocation(View v) {
    	Toast.makeText(getApplicationContext(),"Save",Toast.LENGTH_SHORT).show();
    }
}
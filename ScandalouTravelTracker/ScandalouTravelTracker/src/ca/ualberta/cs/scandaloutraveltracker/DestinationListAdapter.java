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

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.models.Destination;
import ca.ualberta.cs.scandaloutraveltracker.views.EditClaimActivity;
import ca.ualberta.cs.scandaloutraveltracker.views.NewClaimActivity;
import ca.ualberta.cs.scandaloutraveltracker.views.SetDestinationLocationActivity;
import ca.ualberta.cs.scandaloutraveltracker.views.ViewLocationActivity;

/**
 *  Allows essential information from the Destination class to be displayed
 *  in a ListView.
 * @author Team3ScandalouStopwatch
 *
 */
public class DestinationListAdapter extends BaseAdapter {
	protected ArrayList<Destination> destinations;
	protected Context context;
	protected String listLocation;
	protected boolean canEdit;
	private LocationManager lm;
	private Location GPSLocation;
	
	/**
	 * Information that needs to be passed to the DestinationListAdapter so
	 * it can properly display Destination information.
	 * @param context
	 * @param listLocation
	 * @param destinations
	 * @param canEdit
	 */
	public DestinationListAdapter(Context context, String listLocation, ArrayList<Destination> destinations, boolean canEdit) {
		this.context = context;
		this.destinations = destinations;
		this.listLocation = listLocation;
		this.canEdit = canEdit;
	}
	
	@Override
	public int getCount() {
		return destinations.size();
	}

	@Override
	public Object getItem(int position) {
		return destinations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * The getView will create all the child views necessary to display the appropriate information
	 * about a destination in a listview. The visibility of certain elements, such as the delete button,
	 * is based on the canEdit status of the Destination's parent claim. The long click menu is also set
	 * up within this getView (menu allows adding/editing/deleting of geolocations to a destination).
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final int hackyPositionReference = position;
		
		// If an instance of the view hasn't been created yet, create it.
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_destination_display, parent, false);
		}
		
		// Get displays
		TextView nameDisplay = (TextView) convertView.findViewById(R.id.list_destination_name);
		TextView descriptionDisplay = (TextView) convertView.findViewById(R.id.list_destination_description);
		ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.list_destination_delete);
		final ImageView locationImage = (ImageView) convertView.findViewById(R.id.destinationLocationIcon);
		if (destinations.get(position).getLocation() == null) {
			locationImage.setImageDrawable(null);
		}
		else {
			locationImage.setImageResource(R.drawable.mapmarker);
		}
		
		// Get current destination
		String currentDestinationName = destinations.get(position).getName();
		String currentDestinationDescription = destinations.get(position).getDescription();

		// Set TextViews
		nameDisplay.setText(currentDestinationName);
		descriptionDisplay.setText(currentDestinationDescription);
		
		if (canEdit){
			// Delete expense button handler
			deleteButton.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					
					destinations.remove(hackyPositionReference);
					// CITATION http://stackoverflow.com/questions/12142255/call-activity-method-from-adapter
					// 2015-03-14
					// Eldhose M Babu's answer
					if (listLocation == "newClaim"){
						((NewClaimActivity)context).update();
					} else if (listLocation == "editClaim") {
						((EditClaimActivity)context).update();
					}
				}
			});	
		} else {
			
			deleteButton.setVisibility(View.INVISIBLE);
			
			// Will fix later
			deleteButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
						Toast.makeText(context,
								"Submitted claims cannot be edited.", Toast.LENGTH_SHORT).show();
				}
				
			});			
		}
		//set up GPS
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		GPSLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		View.OnClickListener options = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String title = "Current Location\n";
				if (destinations.get(position).getLocation() == null) {
					title = title + "Location not set";
				}
				else {
					locationImage.setImageResource(R.drawable.mapmarker);
					title = title + "Lat: " + String.format("%.4f", destinations.get(position).getLocation().getLatitude());
					title = title + "\nLong: " + String.format("%.4f",destinations.get(position).getLocation().getLongitude());
				}
				TextView view = new TextView(context);
				//http://stackoverflow.com/questions/4602902/how-to-set-the-text-color-of-textview-in-code 2015-04-02
				view.setTextColor(Color.parseColor("#33b5e5"));
				view.setText(title);
				view.setPadding(20, 20, 0, 20);
				view.setTextSize(20);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setCustomTitle(view)
				.setCancelable(true)
				.setItems(R.array.location_menu, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// set destination location to GPS location if it is available
						if (which == 0) {
							GPSLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (GPSLocation == null) {
					        	Toast.makeText(context, "GPS currently unavailable", Toast.LENGTH_SHORT).show();
							}
							else {
								Destination temp = destinations.get(position);
								Location locationTemp = new Location("Destination Location");
								locationTemp.setLatitude(GPSLocation.getLatitude());
								locationTemp.setLongitude(GPSLocation.getLongitude());
								temp.setLocation(locationTemp);
								destinations.set(position, temp);
								locationImage.setImageResource(R.drawable.mapmarker);
							}
							if (destinations.get(position).getLocation() == null) {
				        		locationImage.setImageDrawable(null);
				        	}
						}
						
						// set destination location to map selection
						if (which == 1) {
							Intent intent = new Intent(context, SetDestinationLocationActivity.class);
							if (destinations.get(position).getLocation() == null) {
								intent.putExtra("latitude",999);
						    	intent.putExtra("longitude",999);
							}
							else {
								intent.putExtra("latitude",destinations.get(position).getLocation().getLatitude());
								intent.putExtra("longitude",destinations.get(position).getLocation().getLongitude());
							}
							intent.putExtra("destinationPosition", position);
							if (listLocation == "newClaim"){
								((NewClaimActivity)context).startActivityForResult(intent, 1);
							} else if (listLocation == "editClaim") {
								((EditClaimActivity)context).startActivityForResult(intent, 1);
							}
						}
						
						// set destination location to null
						if (which == 2) {
							Destination temp = destinations.get(position);
							temp.setLocation(null);
							destinations.set(position, temp);
							locationImage.setImageDrawable(null);
						}
					}
					
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		};
		
		// will just show the destination location on the map if editing is not allowed
		View.OnClickListener view = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ViewLocationActivity.class);
				if (destinations.get(position).getLocation() == null) {
					intent.putExtra("latitude",999);
			    	intent.putExtra("longitude",999);
				}
				else {
					intent.putExtra("latitude",destinations.get(position).getLocation().getLatitude());
					intent.putExtra("longitude",destinations.get(position).getLocation().getLongitude());
				}
				intent.putExtra("destinationPosition", position);
				if (listLocation == "newClaim"){
					((NewClaimActivity)context).startActivity(intent);
				} else if (listLocation == "editClaim") {
					((EditClaimActivity)context).startActivity(intent);
				}
			}
		};
		
		if (canEdit) {
			nameDisplay.setOnClickListener(options);
			descriptionDisplay.setOnClickListener(options);
			locationImage.setOnClickListener(options);
		}
		else {
			nameDisplay.setOnClickListener(view);
			descriptionDisplay.setOnClickListener(view);
			locationImage.setOnClickListener(view);
		}
		
		return convertView;
	}
	
}

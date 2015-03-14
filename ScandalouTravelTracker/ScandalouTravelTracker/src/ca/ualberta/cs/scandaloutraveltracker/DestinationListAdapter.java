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

/* DestinationListAdapter.java Basic Info:
 *  Allows essential information from the Destination class to be displayed
 *  in a ListView.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class DestinationListAdapter extends BaseAdapter {
	protected ArrayList<Destination> destinations;
	protected Context context;
	
	public DestinationListAdapter(Context context, ArrayList<Destination> destinations) {
		this.context = context;
		this.destinations = destinations;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
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
		
		// Get current destination
		String currentDestinationName = destinations.get(position).getName();
		String currentDestinationDescription = destinations.get(position).getDescription();

		// Set TextViews
		nameDisplay.setText(currentDestinationName);
		descriptionDisplay.setText(currentDestinationDescription);
		
		// Delete expense button handler
		deleteButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				
				destinations.remove(hackyPositionReference);
				// CITATION http://stackoverflow.com/questions/12142255/call-activity-method-from-adapter
				// 2015-03-14
				// Eldhose M Babu's answer
				((NewClaimActivity)context).update();
			}
		});			
		
		return convertView;
	}
	
}

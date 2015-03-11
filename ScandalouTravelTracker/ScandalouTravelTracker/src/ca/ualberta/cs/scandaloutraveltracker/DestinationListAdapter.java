package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
		
		// If an instance of the view hasn't been created yet, create it.
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_destination_display, parent, false);
		}
		
		// Get displays
		TextView nameDisplay = (TextView) convertView.findViewById(R.id.list_destination_name);
		TextView descriptionDisplay = (TextView) convertView.findViewById(R.id.list_destination_description);
		
		// Get current destination
		String currentDestinationName = destinations.get(position).getName();
		String currentDestinationDescription = destinations.get(position).getDescription();

		
		// Set TextViews
		nameDisplay.setText(currentDestinationName);
		descriptionDisplay.setText(currentDestinationDescription);
		
		return convertView;
	}
	
}

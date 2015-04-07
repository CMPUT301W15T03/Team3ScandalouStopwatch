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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * The UserInformationDialog is a dialog that has all the relevant information
 * pertaining to a user that is to be shown before a user logs in to the actual
 * application. 
 * @author Team3ScandalouStopwatch
 *
 */
public class UserInformationDialog extends DialogFragment {
	
	// Information to set views
	String name;
	String latitude;
	String longitude;
	TextView nameTV;
	TextView latitudeTV;
	TextView longitudeTV;
	
	// Used for testing the dialog
	AlertDialog alert;
	
	// Used to deliver action events
	UserInformationDialogListener listener;
	
	/**
	 * Uses an interface so that any class that wishes to use the UserInformationDialog
	 * should also implement what happens on the positive and negative clicks of the
	 * alert dialog.
	 * @author Team3ScandalouStopwatch
	 *
	 */
	public interface UserInformationDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	/**
	 * Sets the views within the dialog to show the correct information
	 * @param name User's name
	 * @param latitude The latitude associated with user's home location
	 * @param longitude The longitude associated with user's home location
	 */
	public void setViews(String name, String latitude, String longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@SuppressLint("InflateParams") 
	@Override
	/**
	 * The onCreateDialog creates the dialog with the appropriate user information
	 * that we want to display.
	 */
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View content = inflater.inflate(R.layout.dialog_user_info, null);
		nameTV = (TextView) content.findViewById(R.id.user_info_name);
		latitudeTV = (TextView) content.findViewById(R.id.user_info_latitude);
		longitudeTV = (TextView) content.findViewById(R.id.user_info_longitude);
		nameTV.setText(nameTV.getText() + " " + name);
		latitudeTV.setText(latitudeTV.getText() + " " + latitude);
		longitudeTV.setText(longitudeTV.getText() + " " + longitude);
		
		// Inflate and set the layout for the dialog
		// Null is passed as parent view because its going in the dialog layout
		builder.setView(content)
		.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogPositiveClick(UserInformationDialog.this);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogNegativeClick(UserInformationDialog.this);
			}
		});
		
		return builder.create();
	}
	
	@Override
	/**
	 * Overwrites the original onAttach to ensure that the activity that host the dialog
	 * also implements the interface needed. 
	 * @see android.app.DialogFragment#onAttach(android.app.Activity)
	 */
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Verify that the host activity implements the callback interface
		try {
			// UserInformationDialogListener instantiated so we can send events to host
			listener = (UserInformationDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement UserInformationDialogListener");
		}
	}
}

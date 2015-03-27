package ca.ualberta.cs.scandaloutraveltracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
	
	public interface UserInformationDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	public void setViews(String name, String latitude, String longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
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

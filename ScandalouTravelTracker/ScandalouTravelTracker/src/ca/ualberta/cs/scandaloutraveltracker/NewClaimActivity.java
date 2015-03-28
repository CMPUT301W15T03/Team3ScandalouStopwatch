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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import android.content.Context;

/**
 *  Activity that allows the user to add a new claim to the claim list.
 *  Accessed through the add button on the ClaimListActivity.
 * @author Team3ScandalouStopwatch
 *
 */
public class NewClaimActivity extends Activity implements ViewInterface{
	
	private ClaimController claimController = new ClaimController(new Claim());
	private ArrayList<Destination> destinations = new ArrayList<Destination>();
	private Date startDate;
	private Date endDate;
	
	private Context context;
	private TextView nameSet; 
	private EditText sDateSet;
	private EditText eDateSet;
	private EditText descriptionSet;
	private EditText tagsSet;
	private Button claimOkButton;
	private ImageButton addDestButton;
	
	private ListView destList;
	private DestinationListAdapter destinationListAdapter;
	
	// For the destination alert
	private EditText nameInput;
	private EditText descriptionInput;
	private AlertDialog alertDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_claim);
		
		context = this;
		nameSet = (TextView)findViewById(R.id.claimant_name);
		sDateSet = (EditText)findViewById(R.id.start_date);	
		eDateSet = (EditText)findViewById(R.id.end_date);	
		descriptionSet = (EditText)findViewById(R.id.edit_claim_description);
		tagsSet = (EditText)findViewById(R.id.tags_tv);
		
		addDestButton = (ImageButton) findViewById(R.id.add_dest_button);		
		claimOkButton = (Button) findViewById(R.id.claim_ok_button);
		
		destList = (ListView)findViewById(R.id.destinations_lv);
		destinationListAdapter = new DestinationListAdapter(this, "newClaim", destinations, true);
		
		update();

		claimOkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// Get fields
				String name = nameSet.getText().toString();
				String description = descriptionSet.getText().toString();
				String status = Constants.statusInProgress;
				String tagsString = tagsSet.getText().toString();
				ArrayList<String> tagsList = getTagsList(tagsString);
				boolean canEdit = true;
				ArrayList<Expense> expenses = new ArrayList<Expense>();
				
				try {

					
					ClaimListController claimListController = new ClaimListController();
					
					// Get user
					Context context = NewClaimActivity.this;
					ClaimApplication app = (ClaimApplication) context.getApplicationContext();
					User user = app.getUser();
					
					// Create the claim
					int newClaimId = claimListController.createClaim(name, startDate, endDate, description, destinations, 
							tagsList, status, canEdit, expenses, user);
					
					// Add the claim to list
					claimListController.addClaim(new Claim(newClaimId));
					
					finish();
					
				} catch (UserInputException e) {

					System.out.println(e.getMessage());
					Toast.makeText(getApplicationContext(),
							e.getMessage(), Toast.LENGTH_SHORT).show();
				}		

			}
		});			
		
        // startDate dialog picker
		sDateSet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						String startDateString = convertToString(year, monthOfYear, dayOfMonth);
						sDateSet.setHint(startDateString);
						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						Date date = cal.getTime();
						startDate = date;
						sDateSet.setText(startDateString);
						claimController.setStartDate(date);
					}
				};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
        
        // endDate dialog picker
		eDateSet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {;
			DialogFragment newFragment = new DatePickerFragment() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					String endDateString = convertToString(year, monthOfYear, dayOfMonth);
					eDateSet.setHint(endDateString);
					Calendar cal = Calendar.getInstance();
					cal.set(year, monthOfYear, dayOfMonth);
					Date date = cal.getTime();
					endDate = date;
					eDateSet.setText(endDateString);
					claimController.setEndDate(date);
				}
			};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
		
		addDestButton.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InflateParams") @Override
			public void onClick(View v) {
			// http://newtoknow.blogspot.ca/2011/08/android-alert-dialog-with-multi-edit.html 13/3/15
				 LayoutInflater newDestInf = LayoutInflater.from(context);

				 //text_entry is an Layout XML file containing two text field to display in alert dialog
				 final View newDestView= newDestInf.inflate(R.layout.edit_destination, null);
				 nameInput = (EditText) newDestView.findViewById(R.id.edit_destination_name);
				 nameInput.setLines(2);
				 descriptionInput = (EditText) newDestView.findViewById(R.id.edit_destination_description);
				 descriptionInput.setLines(2);
		
				 final AlertDialog.Builder newDest = new AlertDialog.Builder(context);
				 	newDest.setTitle("New Destination")
				 	.setView(newDestView)
				 	.setCancelable(false)
			
				 	.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							String dname = nameInput.getText().toString();
							String dreason = descriptionInput.getText().toString();
							
							if (dname.length()!=0 && dreason.length() != 0){
								
								Destination destination = new Destination(dname, dreason);
								destinations.add(destination);
								destination.notifyViews();
								setViews();

								update();
								
							} else {
								Toast.makeText(context, "Must enter name and/or reason", Toast.LENGTH_SHORT).show();
							}	
							
						}
				 	})
				
					.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				       	public void onClick(DialogInterface dialog, int whichButton) {
					    	dialog.cancel();
				       	}
					});
					 	
				alertDialog = newDest.create();
				alertDialog.show();
			}
		});
		

	
	}
	
	/**
	 * Sets the views for all the destinations after they have been removed. 
	 */
	private void setViews() {
		for (Destination dest : claimController.getDestinations()) {
			dest.addView(this);
		}
	}
	
	/**
	 * Takes a string that contains tags separated by commas and parses it
	 * so the ArrayList just contains the tags.
	 * @param tagsString Comma separated tag string
	 * @return Parsed ArrayList of tags
	 */
	public ArrayList<String> getTagsList(String tagsString){
		
		String[] temp = tagsString.split(", ");
		// CITATION http://stackoverflow.com/questions/10530353/convert-string-array-to-arraylist
		// 2015-03-13
		// Matten's answer
		ArrayList<String> tags = new ArrayList<String>(Arrays.asList(temp));
		
		return tags;
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_claim, menu);
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		destList.setAdapter(destinationListAdapter);
	}
	
	// TEST METHODS BELOW
	public void setStartDate(Date date) {
		startDate = date;
	}
	
	public void setEndDate(Date date) {
		endDate = date;
	}
	
	public void setDestinationName(String name) {
		nameInput.setText(name);
	}
	
	public void setDestinationReason(String reason) {
		descriptionInput.setText(reason);
	}
	
	public AlertDialog getDestinationDialog() {
		return alertDialog;
	}
	
	public ArrayList<Destination> getDestinationsList() {
		return destinations;
	}
}


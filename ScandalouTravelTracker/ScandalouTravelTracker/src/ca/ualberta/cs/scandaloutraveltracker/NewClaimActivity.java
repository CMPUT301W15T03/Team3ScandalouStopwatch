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

/* NewClaimActivity.java Basic Info:
 *  Activity that allows the user to add a new claim to the claim list.
 *  Accessed through the add button on the ClaimListActivity.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import android.view.View.OnLongClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import android.content.Context;
import android.content.DialogInterface;
//import android.content.Context;
import android.content.Intent;

public class NewClaimActivity extends Activity implements ViewInterface{
	
	private Claim claim = new Claim();
	private ClaimController claimController = new ClaimController(claim);
	private ArrayList<Destination> destinations = new ArrayList<Destination>();
	private Date startDate;
	private Date endDate;
	
	private ListView destList;
	private DestinationListAdapter destinationListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_claim);
		
		final Context context = this;
		final EditText nameSet = (EditText)findViewById(R.id.claimant_name);
		final EditText sDateSet = (EditText)findViewById(R.id.start_date);	
		final EditText eDateSet = (EditText)findViewById(R.id.end_date);	
		final EditText descriptionSet = (EditText)findViewById(R.id.edit_claim_description);
		final EditText tagsSet = (EditText)findViewById(R.id.tags_tv);
		
		ImageButton addDestButton = (ImageButton) findViewById(R.id.add_dest_button);		
		Button claimOkButton = (Button) findViewById(R.id.claim_ok_button);
		
		destList = (ListView)findViewById(R.id.destinations_lv);
		destinationListAdapter = new DestinationListAdapter(this, destinations);
		
		update();

		claimOkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (sDateSet.getText().length() == 0) {
					Toast.makeText(getApplicationContext(), "Please include a Start Date", Toast.LENGTH_SHORT).show();
				}
				
				else if (eDateSet.getText().length() == 0) {
					Toast.makeText(getApplicationContext(), "Please include an End Date", Toast.LENGTH_SHORT).show();
				}
				
				else {
					
					String name = nameSet.getText().toString();
					String description = descriptionSet.getText().toString();
					String status = Constants.statusInProgress;
					String tagsString = tagsSet.getText().toString();
					ArrayList<String> tagsList = getTagsList(tagsString);
					boolean canEdit = true;
					ArrayList<Expense> expenses = new ArrayList<Expense>();
					
					ClaimMapper mapper = new ClaimMapper(context.getApplicationContext());
					int newClaimId = mapper.createClaim(name, startDate, endDate, description, destinations, 
							tagsList, status, canEdit, expenses);
	
					ClaimListController claimListController = new ClaimListController();
					claimListController.addClaim(new Claim(newClaimId));
					
					finish();
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
						claim.setStartDate(date);
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
					claim.setEndDate(date);
				}
			};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
		
		addDestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			// http://newtoknow.blogspot.ca/2011/08/android-alert-dialog-with-multi-edit.html 13/3/15
				 LayoutInflater newDestInf = LayoutInflater.from(context);

				 //text_entry is an Layout XML file containing two text field to display in alert dialog
				 final View newDestView= newDestInf.inflate(R.layout.edit_destination, null);
				 final EditText nameInput = (EditText) newDestView.findViewById(R.id.edit_destination_name);
				 final EditText descriptionInput = (EditText) newDestView.findViewById(R.id.edit_destination_description);
		
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
					 	
				AlertDialog alertDialog =newDest.create();
				alertDialog.show();
			}
		});
		

	
	}
	
	private void setViews() {
		for (Destination dest : claimController.getDestinations()) {
			dest.addView(this);
		}
	}
	
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
	
}


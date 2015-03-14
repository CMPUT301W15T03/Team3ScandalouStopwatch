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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import android.content.Context;
import android.content.DialogInterface;
//import android.content.Context;
import android.content.Intent;

public class NewClaimActivity extends Activity implements ViewInterface{
	
	Claim c = new Claim();
	ClaimController claim = new ClaimController(c);
	String name;
	String sDate;
	String eDate;
	ArrayList<Destination> dList;
	String description;
	ArrayList<String> tags;
	private Date startDate;
	private Date endDate;
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
		

		Button claimOkButton = (Button) findViewById(R.id.claim_ok_button);
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
					//fills in most fields of claim from edit texts
					claim.setName(nameSet.getText().toString());
					claim.setDescription(descriptionSet.getText().toString());
					
					// For testing
					//dList = new ArrayList<Destination>();
					//dList.add(new Destination("Alderaan Orbit", "Product demo"));
					//dList.add(new Destination("Cloud City, Bespin", "More Empire business; catching up with son"));
					
					// Also for testing
					//tags = new ArrayList<String>();
					//tags.add("Tag1");
					//tags.add("Tag2");
					
					// Creation status
					String status = Constants.statusInProgress;
				
					ClaimMapper mapper = new ClaimMapper(context.getApplicationContext());
					int newClaimId = mapper.createClaim(nameSet.getText().toString(), 
							startDate, endDate, descriptionSet.getText().toString(), dList, tags, status, true,
							new ArrayList<Expense>());
	
					ClaimListController claimListController = new ClaimListController();
					claimListController.addClaim(new Claim(newClaimId));
					
					finish();
				}
			}
		});
		
		ListView destList = (ListView)findViewById(R.id.destinations_lv);
		destinationListAdapter= new DestinationListAdapter(this, c.getDestinations());
		destList.setAdapter(destinationListAdapter);
		
		
		Button addDestButton = (Button) findViewById(R.id.add_dest_button);
			addDestButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				//	http://newtoknow.blogspot.ca/2011/08/android-alert-dialog-with-multi-edit.html 13/3/15
						 LayoutInflater newDestInf = LayoutInflater.from(context);

				final View newDestView= newDestInf.inflate(R.layout.list_destination_display, null);
				       //text_entry is an Layout XML file containing two text field to display in alert dialog

				final EditText name = (EditText) newDestView.findViewById(R.id.destination_name);
				final EditText reason = (EditText) newDestView.findViewById(R.id.destination_description);

				name.setText("Name", EditText.BufferType.EDITABLE);
				reason.setText("Reason", EditText.BufferType.EDITABLE);
				
				final AlertDialog.Builder newDest = new AlertDialog.Builder(context);
				newDest.setTitle("New Destination");
				newDest.setView(newDestView);
				newDest.setCancelable(false);
					
					newDest.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							String dreason = reason.getText().toString();
							String dname = name.getText().toString();
							
							if (dname.length()!=0 && dreason.length()!=0){
								Destination d = new Destination(dname, dreason);
																
								claim.addDestination(d);
								c.notifyViews();
								Toast.makeText(context, "reasons and name entered", Toast.LENGTH_SHORT).show();
								setViews();
							}else{
								Toast.makeText(context, "Must enter name and/or reason", Toast.LENGTH_SHORT).show();
							}
							
							
							
							dialog.cancel();
							
						}
					})
					
					.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}});
				AlertDialog alertDialog =newDest.create();
				alertDialog.show();
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
							c.setStartDate(date);
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
						c.setEndDate(date);
					}
				};
					newFragment.show(getFragmentManager(), "datePicker");
				}
			});
	
	}
	
	private void setViews() {
		for (Destination dest : claim.getDestinations()) {
			dest.addView(this);
		}
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
		
	}
	
}


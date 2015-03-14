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

/* EditClaimActivity.java Basic Info:
 *  This activity contains a claim that was selected from the ClaimListActivity
 *  and allows you to edit the claim.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditClaimActivity extends Activity implements ViewInterface {

	private Claim claim;
	private int claimId;
	
	private EditText nameDisplay;
	private TextView statusDisplay;
	private EditText startDateDisplay;
	private EditText endDateDisplay;
	private EditText descriptionDisplay;
	private TextView tagsDisplay;
	private ListView destinationList;
	private EditText tagsInput;
	private Button editTagsButton;
	private Button updateButton;
	private Button sendButton;
	
	private String name;
	private Date startDate;
	private Date endDate;
	private String description;
	private ArrayList<Destination> destinations;
	private ArrayList<String> tagsString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);

		Intent intent;
		
	    // Get the message from the intent
	    intent = getIntent();
	    claimId = intent.getIntExtra(Constants.claimIdLabel, 0);
		
		// Get view elements
		nameDisplay = (EditText) findViewById(R.id.edit_claim_claimant_name);
		//statusDisplay = (TextView) findViewById(R.id.edit_claim_status);
		startDateDisplay = (EditText) findViewById(R.id.edit_claim_start_date);
		endDateDisplay = (EditText) findViewById(R.id.edit_claim_end_date);
		descriptionDisplay = (EditText) findViewById(R.id.edit_claim_descr);
		tagsDisplay = (TextView) findViewById(R.id.edit_claim_tags);
		destinationList = (ListView) findViewById(R.id.edit_claim_destinations);
		editTagsButton = (Button) findViewById(R.id.edit_claim_edit_tags);
		updateButton = (Button) findViewById(R.id.edit_claim_update);
		sendButton = (Button) findViewById(R.id.edit_claim_send);	
		
	    claim = new Claim(claimId);
	    
	    claim.addView(this);
		update();		

        // startDate dialog picker
		startDateDisplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						String startDateString = convertToString(year, monthOfYear, dayOfMonth);
						startDateDisplay.setHint(startDateString);
						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						Date date = cal.getTime();
						startDate = date;
						startDateDisplay.setText(startDateString);
						claim.setStartDate(date);
					}
				};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
        
        // endDate dialog picker
		endDateDisplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {;
			DialogFragment newFragment = new DatePickerFragment() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					String endDateString = convertToString(year, monthOfYear, dayOfMonth);
					endDateDisplay.setHint(endDateString);
					Calendar cal = Calendar.getInstance();
					cal.set(year, monthOfYear, dayOfMonth);
					Date date = cal.getTime();
					endDate = date;
					endDateDisplay.setText(endDateString);
					claim.setEndDate(date);
				}
			};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});		
		
		editTagsButton.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v){
			
			    tagsInput = new EditText(EditClaimActivity.this);	
			    tagsInput.setText(tagsDisplay.getText().toString());
				
				//http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog 2015-02-01
				//http://stackoverflow.com/questions/8227820/alert-dialog-two-buttons 2015-02-01
				AlertDialog.Builder builder = new AlertDialog.Builder(EditClaimActivity.this);
				builder.setMessage("Are you sure you want to submit your claim? You won't be able to make any changes " +
						"except adding/removing tags.")
						
				   .setView(tagsInput)
				   .setCancelable(true)
				   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				       	public void onClick(DialogInterface dialog, int i) {
					    	dialog.cancel();
				       	}
				   })
				   .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int i) {

							String tagsString = tagsInput.getText().toString();
							ArrayList<String> tagsList = getTagsList(tagsString);							
	
							ClaimController claimController = new ClaimController(claim);
							
							claimController.updateTags(tagsList);
						}  
				   });
				AlertDialog alert = builder.create();
				alert.show();				
				
			}
			
		});		
		
		updateButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v){
				
				// Get fields
				String name = nameDisplay.getText().toString();
				String description = descriptionDisplay.getText().toString();
				boolean canEdit = true;
				// TODO: Destinations				

				// Test destinations
				ArrayList<Destination> testDestinations = new ArrayList<Destination>();	
				testDestinations.add(new Destination("Minneapolis", "Too cold"));
				testDestinations.add(new Destination("Seattle", "Too rainy"));
				testDestinations.add(new Destination("Chicago", "Too windy"));
				
				ClaimController claimController = new ClaimController(claim);
				
				claimController.updateClaim(name, startDate, endDate, description, testDestinations, canEdit);

			}
			
		});
		
		sendButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v){
			
				//http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog 2015-02-01
				//http://stackoverflow.com/questions/8227820/alert-dialog-two-buttons 2015-02-01
				AlertDialog.Builder builder = new AlertDialog.Builder(EditClaimActivity.this);
				builder.setMessage("Are you sure you want to submit your claim? You won't be able to make any changes " +
						"except adding/removing tags.")
				   .setCancelable(true)
				   .setNegativeButton("Don't Submit", new DialogInterface.OnClickListener() {
				       	public void onClick(DialogInterface dialog, int i) {
					    	dialog.cancel();
				       	}
				   })
				   .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int i) {
							
							ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
							mapper.submitClaim(claimId, Constants.statusSubmitted, false);
							
							update();
						}  
				   });
				AlertDialog alert = builder.create();
				alert.show();				
				
			}
			
		});			
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_claim, menu);
		return true;
	}

	@Override
	public void update() {
		
		// Get controller
		ClaimController claimController = new ClaimController(claim);
		
		// Get claim info
		name = claimController.getName();
		//status = claimController.getStatus();
		startDate = claimController.getStartDate();
		endDate = claimController.getEndDate();
		description = claimController.getDescription();
		ArrayList<String> tagsList = claimController.getTags();
		destinations = claimController.getDestinations();
		
		// Process more complicated claim info
		String tagsString = getTagsString(tagsList);
		
		// Set adapters
		DestinationListAdapter destinationsAdapter = new DestinationListAdapter(this, destinations);
		
		// Update view elements with claim info
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
		nameDisplay.setText(name);
		//statusDisplay.setText(status);
		startDateDisplay.setText(sdf.format(startDate));
		endDateDisplay.setText(sdf.format(endDate));
		descriptionDisplay.setText(description);
		tagsDisplay.setText(tagsString);
		destinationList.setAdapter(destinationsAdapter);
		
	}
	
	public ArrayList<String> getTagsList(String tagsString){
		
		String[] temp = tagsString.split(", ");
		// CITATION http://stackoverflow.com/questions/10530353/convert-string-array-to-arraylist
		// 2015-03-13
		// Matten's answer
		ArrayList<String> tags = new ArrayList<String>(Arrays.asList(temp));
		
		return tags;
	}
	
	public String getTagsString(ArrayList<String> tagsList){
		
		String tags = "";
		
		for (int i = 0; i < tagsList.size(); i++){
			if (i != tagsList.size() - 1){
				tags += tagsList.get(i) + ", ";
			} else {
				tags += tagsList.get(i);
			}
		}
		
		return tags;
	}

}

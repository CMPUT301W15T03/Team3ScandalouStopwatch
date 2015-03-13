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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditClaimActivity extends Activity implements ViewInterface {

	Claim claim;
	int claimId;
	
	EditText nameDisplay;
	TextView statusDisplay;
	Button startDateButton;
	Button endDateButton;
	EditText descriptionDisplay;
	EditText tagsDisplay;
	ListView destinationList;
	Button updateButton;
	Button sendButton;	
	
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
		statusDisplay = (TextView) findViewById(R.id.edit_claim_status);
		startDateButton = (Button) findViewById(R.id.edit_claim_start_date);
		endDateButton = (Button) findViewById(R.id.edit_claim_end_date);
		descriptionDisplay = (EditText) findViewById(R.id.edit_claim_descr);
		tagsDisplay = (EditText) findViewById(R.id.edit_claim_tags);
		destinationList = (ListView) findViewById(R.id.edit_claim_destinations);
		updateButton = (Button) findViewById(R.id.edit_claim_update);
		sendButton = (Button) findViewById(R.id.edit_claim_send);		
		
	    claim = new Claim(claimId);
	    
	    claim.addView(this);
		update();		
		
		updateButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v){
				
				// Get fields
				String name = nameDisplay.getText().toString();
				String description = descriptionDisplay.getText().toString();
				String tagsString = tagsDisplay.getText().toString();
				// TODO: Dates, destinations
				
				// Process more complicated fields
				ArrayList<String> tags = getTagsList(tagsString);

				ArrayList<Destination> testDestinations = new ArrayList<Destination>();	
				testDestinations.add(new Destination("Minneapolis", "Too cold"));
				testDestinations.add(new Destination("Seattle", "Too rainy"));
				testDestinations.add(new Destination("Chicago", "Too windy"));
				
				ArrayList<String> testTags = new ArrayList<String>();	
				testTags.add("#thug4life");
				testTags.add("#kony2012");
				
				ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
				//mapper.updateClaim(claimId, name, new Date(2015, 1, 1), new Date(2015, 2, 2), 
				//		description, testDestinations, tags);
				
				update();
			}
			
		});	
		
		sendButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v){
				
				ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
				//mapper.submitClaim(claimId, Constants.statusSubmitted);
				
				update();
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
		String name = claimController.getName();
		Date startDate = claimController.getStartDate();
		Date endDate = claimController.getEndDate();
		String description = claimController.getDescription();
		//ArrayList<String> tagsList = claimController.getTags();
		ArrayList<Destination> destinations = claimController.getDestinations();
		
		// Process more complicated claim info
		//String tags = getTagsString(tagsList);
		
		// Set adapters
		DestinationListAdapter destinationsAdapter = new DestinationListAdapter(this, destinations);
		
		// Update view elements with claim info
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
		nameDisplay.setText(name);
		startDateButton.setText(sdf.format(startDate));
		endDateButton.setText(sdf.format(endDate));
		descriptionDisplay.setText(description);
		//tagsDisplay.setText(tags);
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
	
	/*
	public ArrayList<String> getTagsString(ArrayList<String> tagsList){
		
		String tags = "";
		
		for (String tag: tagsList){
			tags += tag;
		}
		
		
		return tags;
	}
	*/

}

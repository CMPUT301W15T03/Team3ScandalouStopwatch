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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EditClaimActivity extends Activity implements ViewInterface {

	Claim claim;
	int claimId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);

		Intent intent;
		
	    // Get the message from the intent
	    intent = getIntent();
	    claimId = intent.getIntExtra(Constants.claimIdLabel, 0);
		/*
	    ArrayList<Destination> destinations = new ArrayList<Destination>();
	    destinations.add(new Destination("Death Star II", "Fully armed (and operational) battlestation"));
	    ArrayList<String> tags = new ArrayList<String>();
	    tags.add("Tag 1", "Tag 2");
	    Calendar cal1 = Calendar.getInstance();
	    cal1.set(Calendar.YEAR, 1983);
	    cal1.set(Calendar.MONTH, 4);
	    cal1.set(Calendar.DAY_OF_MONTH, 4);	    
	    Date startDate = cal1.getTime();
	    Calendar cal2 = Calendar.getInstance();	    
	    cal2.set(Calendar.YEAR, 1983);
	    cal2.set(Calendar.MONTH, 5);
	    cal2.set(Calendar.DAY_OF_MONTH, 4);
	    Date endDate = cal2.getTime();	 	    
		claim = new Claim(
				"Emperor Palpatine", 
				"Inspection went great. Everything is proceeding as I have foreseen.", 
				startDate, 
				endDate, 
				destinations,
				tags);
		claim.addView(this);
		*/
	    
	    claim = new Claim(claimId);
	    claim.addView(this);
	    
		update();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_claim, menu);
		return true;
	}

	@Override
	public void update() {
		
		// Get view elements
		TextView nameDisplay = (TextView) findViewById(R.id.edit_claim_name);
		TextView descriptionDisplay = (TextView) findViewById(R.id.edit_claim_descr);		
		Button startDateButton = (Button) findViewById(R.id.edit_claim_start_date);
		Button endDateButton = (Button) findViewById(R.id.edit_claim_end_date);
		ListView destinationList = (ListView) findViewById(R.id.edit_claim_destinations);
		Button updateButton = (Button) findViewById(R.id.edit_claim_update);
		Button sendButton = (Button) findViewById(R.id.edit_claim_send);
		
		// Get controller
		ClaimController claimController = new ClaimController(claim);
		
		// Get claim info
		String name = claimController.getName();
		String description = claimController.getDescription();
		Date startDate = claimController.getStartDate();
		Date endDate = claimController.getEndDate();
		ArrayList<Destination> destinations = claimController.getDestinations();
		
		// Set adapters
		DestinationListAdapter destinationsAdapter = new DestinationListAdapter(this, destinations);
		
		// Update view elements with claim info
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
		nameDisplay.setText(name);
		descriptionDisplay.setText(description);
		startDateButton.setText(sdf.format(startDate));
		endDateButton.setText(sdf.format(endDate));
		destinationList.setAdapter(destinationsAdapter);
		
	}

}

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

/* ClaimListActivity.java Basic Info:
 *  This activity is the first activity that is displayed to the user.
 *  The activity contains the list of claims that the user has and
 *  allows the user to make modifications to the claim they choose.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ClaimListActivity extends Activity implements ViewInterface {
	private Button addClaimButton;
	private ListView claimsListView;
	private ClaimListAdapter claimListAdapter;
	private ClaimList claimsList; // Test variable - Remove Later
	private ClaimListController claimListController;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_list);
		
		// Get Claims
		claimListController = new ClaimListController();
		claimsList = claimListController.getClaimList();
		
		// Set layout elements
		addClaimButton = (Button) findViewById(R.id.addButtonClaimList);
		claimsListView = (ListView) findViewById(R.id.claimListActivityList);

		claimListController.addView(this); // Testing to add view for claimsLists
		claimListAdapter = new ClaimListAdapter(this, claimsList);
		claimsListView.setAdapter(claimListAdapter);
		
		/*
		//for testing purposes
		Claim testClaim = createTestClaim();
		Claim testClaim2 = createTestClaim2();
		Claim testClaim3 = createTestClaim3();
		claimListController.addClaim(testClaim3);
		claimListController.addClaim(testClaim);
		claimListController.addClaim(testClaim2);
		claimListController.notifyViews();
		*/
		
		// Add claim button on click
		addClaimButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ClaimListActivity.this, NewClaimActivity.class);
				startActivity(intent);
			}
		});
		
		//when claim is clicked alert dialog appears with edit/view claim, add expense, delete claim
		claimsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, final long claimPos) {
				
				final int claimId = claimListController.getClaim((int) claimPos).getId();
				
				//http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog 2015-02-01
				//http://stackoverflow.com/questions/8227820/alert-dialog-two-buttons 2015-02-01
				AlertDialog.Builder builder = new AlertDialog.Builder(ClaimListActivity.this);
				builder.setMessage("Claim Options")
				   .setCancelable(true)
				   .setNegativeButton("Edit/View Claim", new DialogInterface.OnClickListener() {
				       	public void onClick(DialogInterface dialog, int i) {
					    	//edit claim
					   		Intent intent = new Intent(ClaimListActivity.this, EditClaimActivity.class);
					   		intent.putExtra(Constants.claimIdLabel, claimId);
					   		startActivity(intent);
				       	}
				   })
				   .setPositiveButton("Delete Claim", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int i) {
							//delete correct claim
							claimListController.removeClaim(claimPos);
						}  
				   })
				   .setNeutralButton("Add Expense", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int i) {;
						    //add a new expense
						   	Intent intent = new Intent(ClaimListActivity.this, AddExpenseActivity.class);
						   	intent.putExtra(Constants.claimIdLabel, claimId);
						   	startActivity(intent);
						}
				   });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
	
	// For making a test claim (comparing dates)
	private Claim createTestClaim() {
		Claim newClaim = new Claim();
		
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
		Date startDate;
		try {
			startDate = sdf.parse("01/01/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		Date endDate;
		try {
			endDate = sdf.parse("02/02/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		Destination d1 = new Destination("Edmonton", "Chillin here");
		Destination d2 = new Destination("Vancouver", "Now I'm here");
		
		Expense expense1 = new Expense();
		expense1.setCost(16.00);
		expense1.setCurrencyType("Canadian (CAD)");
		
		Expense expense2 = new Expense();
		expense2.setCost(4.21);
		expense2.setCurrencyType("Canadian (CAD)");
		
		ClaimController claimController = new ClaimController(newClaim);
		claimController.setName("Test Claim");
		claimController.setStartDate(startDate);
		claimController.setEndDate(endDate);
		claimController.addDestination(d1);
		claimController.addDestination(d2);
		claimController.addTag("#AB");
		claimController.addTag("#BC");
		claimController.addExpense(expense1);
		claimController.addExpense(expense2);
		
		return newClaim;
	}

	// For making a test claim (comparing dates)
	private Claim createTestClaim2() {
		Claim newClaim = new Claim();
		
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
		Date startDate;
		try {
			startDate = sdf.parse("02/02/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		Date endDate;
		try {
			endDate = sdf.parse("03/03/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		Destination d1 = new Destination("Beijing", "Chillin here");
		Destination d2 = new Destination("Macau", "Now I'm here");
		
		Expense expense1 = new Expense();
		expense1.setCost(12.15);
		expense1.setCurrencyType("Swiss Franc (CHF)");
		
		Expense expense2 = new Expense();
		expense2.setCost(412.00);
		expense2.setCurrencyType("Japanese Yen (JPY)");
		
		ClaimController claimController = new ClaimController(newClaim);
		claimController.setName("Test Claim");
		claimController.setStartDate(startDate);
		claimController.setEndDate(endDate);
		claimController.addDestination(d1);
		claimController.addDestination(d2);
		claimController.addExpense(expense2);
		claimController.addExpense(expense1);
		
		return newClaim;
	}
	
	// For making a test claim (comaparing dates)
	private Claim createTestClaim3() {
		Claim newClaim = new Claim();
		
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
		Date startDate;
		try {
			startDate = sdf.parse("04/04/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		Date endDate;
		try {
			endDate = sdf.parse("05/05/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		Destination d1 = new Destination("Melbourne", "Chillin here");
		Destination d2 = new Destination("Sydney", "Now I'm here");
		
		ClaimController claimController = new ClaimController(newClaim);
		claimController.setName("Test Claim");
		claimController.setStartDate(startDate);
		claimController.setEndDate(endDate);
		claimController.addDestination(d1);
		claimController.addDestination(d2);
		
		return newClaim;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claim_list, menu);
		return true;
	}

	@Override
	public void update() {
		claimListAdapter.notifyDataSetChanged();
	}

}

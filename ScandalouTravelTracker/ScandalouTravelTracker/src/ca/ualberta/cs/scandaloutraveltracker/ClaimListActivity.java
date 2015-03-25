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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 *  This activity is the first activity that is displayed to the user.
 *  The activity contains the list of claims that the user has and
 *  allows the user to make modifications to the claim they choose.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimListActivity extends MenuActivity implements ViewInterface {
	private Button addClaimButton;
	private ListView claimsListView;
	private ClaimListAdapter claimListAdapter;
	private ClaimListController claimListController;
	private User currentUser;
	
	// need to make this easier to get. for testing right now and
	// should be 0 for approver screen, 1 for claimant screen
	private int screenType = -1;
	
	// is for picking between screens, screenType should only change
	// when confirm is clicked.  
	private int screenTypeTemp = -1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_list);
		
		// Get Claims
		currentUser = ( (ClaimApplication) getApplication()).getUser();
		claimListController = new ClaimListController(currentUser);
		
		// Set layout elements
		addClaimButton = (Button) findViewById(R.id.addButtonClaimList);
		claimsListView = (ListView) findViewById(R.id.claimListActivityList);

		claimListController.addView(this); // Testing to add view for claimsLists
		claimListAdapter = new ClaimListAdapter(this, claimListController.getClaimList());
		claimsListView.setAdapter(claimListAdapter);
		
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
				builder.setTitle("Claim Options")
				.setCancelable(true)
				.setItems(R.array.claim_menu, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	   //when edit/view claim is pressed
		            	   if (which == 0){ 
		            		   Intent intent = new Intent(ClaimListActivity.this, EditClaimActivity.class);
		            		   intent.putExtra(Constants.claimIdLabel, claimId);
		            		   startActivity(intent);
		            	   }
		            	   //when list expenses is pressed
		            	   else if (which == 1){
		            		   Intent intent = new Intent(ClaimListActivity.this, ExpenseListActivity.class);
							   intent.putExtra(Constants.claimIdLabel, claimId);
							   startActivity(intent);
		            	   }
		            	   //when add expense is pressed
		            	   else if(which == 2){
		            		   Claim currentClaim = claimListController.getClaim((int)claimPos);
		            		   
		            		   // If/Else Checks if the Claim can actually be edited
		            		   if (currentClaim.getCanEdit()) {
		            			   Intent intent = new Intent(ClaimListActivity.this, NewExpenseActivity.class);
								   intent.putExtra(Constants.claimIdLabel, claimId);
			            		   startActivity(intent);
		            		   }
		            		   else {
		            			   Toast.makeText(getApplicationContext(), 
		            					   		  currentClaim.getStatus() + " claims can not be edited.", 
		            					   		  Toast.LENGTH_SHORT).show();
		            		   }
		            	   }
		            	   //when delete claim is pressed
		            	   else if(which == 3){
		            		   Claim currentClaim = claimListController.getClaim((int)claimPos);
		            		   
		            		   // If/Else Checks if the Claim can actually be edited
		            		   if (currentClaim.getCanEdit()) {
		            			   AlertDialog.Builder builder = new AlertDialog.Builder(ClaimListActivity.this);
			            		   builder.setMessage("This will delete the claim and the corresponding expenses. Are you sure?")
			            		   		.setCancelable(true)
			            		   .setPositiveButton("No", new DialogInterface.OnClickListener() {
			                           public void onClick(DialogInterface dialog, int id) {
			                        	   
			                           }
			                       })
			                       .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			                           public void onClick(DialogInterface dialog, int id) {
			                        	   
			                        	   // Remove the claim from the list
			                        	   claimListController.removeClaim(claimId);
			                        	   // Delete the claim from storage
			                        	   claimListController.deleteClaim(claimId);
			                        	   
			                        	   // Update ListView
			                        	   update();
			                           }
			                       });
			            		   AlertDialog alert = builder.create();
			            		   alert.show();
		            		   }
		            		   else {
		            			   Toast.makeText(getApplicationContext(), 
		            					   		  currentClaim.getStatus() + " claims can not be edited.", 
		            					   		  Toast.LENGTH_SHORT).show();
		            		   }
		            	   }
		           }
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
	
	// Used for testing that users have the correct claim list
	public ClaimList getCurrentClaimList() {
		return claimListController.getClaimList();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		claimListController = new ClaimListController(currentUser);
		claimListAdapter = new ClaimListAdapter(this, claimListController.getClaimList());
		claimsListView.setAdapter(claimListAdapter);
	}

	@Override
	public void update() {
		claimListAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items.
	    switch (item.getItemId()) {
	    	// Goes to "main" menu of the app while clearing the activity stack.
	        case R.id.action_user:
	        	Toast.makeText(getApplicationContext(), "change user selected",Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(ClaimListActivity.this, UserSelectActivity.class);
	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
	            return true;
	        // Change the User from approver to claimant or vice versa.
	        // TODO - get which screen the user is currently on so it can be changed
	        //	     - implement similar thing for Expense List Viewing or go back to ClaimListActivity
	        //		   when change screen is confirmed?
	        case R.id.action_screen:
	        	Toast.makeText(getApplicationContext(), "change screen selected",Toast.LENGTH_SHORT).show();
	        	screenTypeTemp = -1;
	        	AlertDialog.Builder builder = new AlertDialog.Builder(ClaimListActivity.this);
				builder.setTitle("Switch Screen View")
				.setCancelable(true)
				// don't change the screen type
				.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						return;
					}
				})
				// change the screen type to the one selected
				.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// screenType should not change since nothing except confirm was clicked
						if (screenTypeTemp == -1) {
							Toast.makeText(getApplicationContext(), "Screen Unchanged",Toast.LENGTH_SHORT).show();
						}
						// set screen to approver
						if (screenTypeTemp == 0) {
							Toast.makeText(getApplicationContext(), "Change to Approver",Toast.LENGTH_SHORT).show();
							screenType = 0;
						}
						// set screen to claimant
						if (screenTypeTemp == 1) {
							Toast.makeText(getApplicationContext(), "change to Claimant",Toast.LENGTH_SHORT).show();
							screenType = 1;
						}
					}
				})
				// record which one was selected for confirmation
				.setSingleChoiceItems(R.array.screen_menu, screenType, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						screenTypeTemp = which;
					}
				}); 
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			default:
	        	return false;
	    }
	}

}

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
	private UserController currentUserController;
	private int screenTypeTemp;
	private AlertDialog tagSelectDialog;
	private ArrayList<String> tagsList;
	private boolean tagsBooleanArray[];
	private ArrayList<String> selectedTags = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_list);
		
		// Set user in ClaimApplication
		ClaimApplication app = (ClaimApplication) getApplicationContext();
		Intent intent = getIntent();
		int userId = intent.getIntExtra("userId", 0);
		app.setUser(new User(userId));
		
		// Get Claims
		currentUser = ( (ClaimApplication) getApplication()).getUser();
		currentUserController = new UserController(currentUser);
		screenTypeTemp = -1;
		
		// Set layout elements
		addClaimButton = (Button) findViewById(R.id.addButtonClaimList);
		claimsListView = (ListView) findViewById(R.id.claimListActivityList);
		
		// Claimant mode
		if (currentUserController.getMode() == 0) { 
			claimListController = new ClaimListController(currentUser);
			claimListController.addView(this); // Testing to add view for claimsLists
			claimListAdapter = new ClaimListAdapter(this, claimListController.getClaimList());
			claimsListView.setAdapter(claimListAdapter);
		}
		
		// Approver mode
		else if (currentUserController.getMode() == 1) {
			claimListController = new ClaimListController(currentUser, Constants.APPROVER_MODE);
			claimListController.addView(this);
			claimListAdapter = new ClaimListAdapter(this, claimListController.getClaimList());
			claimsListView.setAdapter(claimListAdapter);
		}
		
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
		// Claimant mode
		if (currentUserController.getMode() == 0) { 
			claimListController = new ClaimListController(currentUser);
			claimListController.addView(this); // Testing to add view for claimsLists
			claimListAdapter = new ClaimListAdapter(this, claimListController.getClaimList());
			claimsListView.setAdapter(claimListAdapter);
		}
		
		// Approver mode
		else if (currentUserController.getMode() == 1) {
			claimListController = new ClaimListController(currentUser, Constants.APPROVER_MODE);
			claimListController.addView(this);
			claimListAdapter = new ClaimListAdapter(this, claimListController.getClaimList());
			claimsListView.setAdapter(claimListAdapter);
		}
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
	        	Intent intent = new Intent(ClaimListActivity.this, UserSelectActivity.class);
	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
	            return true;
	        // Change the User from approver to claimant or vice versa.
	        case R.id.action_screen:
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
						// Mode should not change since nothing except confirm was clicked
						if (screenTypeTemp == -1) {
							Toast.makeText(getApplicationContext(), "Screen Unchanged",Toast.LENGTH_SHORT).show();
						}
						// Claimant Mode
						if (screenTypeTemp == 0) {
							Toast.makeText(getApplicationContext(), "Change to Claimant",Toast.LENGTH_SHORT).show();
							currentUserController.setMode(0);
							claimListController = new ClaimListController(currentUser);
							claimListController.addView(ClaimListActivity.this); // Testing to add view for claimsLists
							claimListAdapter = new ClaimListAdapter(ClaimListActivity.this, claimListController.getClaimList());
							claimsListView.setAdapter(claimListAdapter);
						}
						// Approver mode
						if (screenTypeTemp == 1) {
							Toast.makeText(getApplicationContext(), "Change to Approver",Toast.LENGTH_SHORT).show();
							currentUserController.setMode(1);
							claimListController = new ClaimListController(currentUser, Constants.APPROVER_MODE);
							claimListController.addView(ClaimListActivity.this);
							claimListAdapter = new ClaimListAdapter(ClaimListActivity.this, claimListController.getClaimList());
							claimsListView.setAdapter(claimListAdapter);
						}
					}
				})
				// record which one was selected for confirmation
				.setSingleChoiceItems(R.array.screen_menu, currentUserController.getMode(), new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						screenTypeTemp = which;
					}
				}); 
				AlertDialog alert = builder.create();
				alert.show();
				return true;
	        case R.id.action_filter_claims:
	        	tagsList = getAllTagsSequence();
	        	final CharSequence[] tagsSequence = tagsList.toArray(new CharSequence[tagsList.size()]);
	        	AlertDialog.Builder tagFilterBuilder = new AlertDialog.Builder(ClaimListActivity.this);
	        	tagFilterBuilder.setTitle("Select Tags to Include")
	        	.setCancelable(true)
	        	.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Clicking on the Cancel button exits dialog
						return;
					}
				})
				.setMultiChoiceItems(tagsSequence, 
						tagsBooleanArray, new DialogInterface.OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// If item is checked, add it to the list. If the item is in the
						// list, remove it.
						if (isChecked) {
							selectedTags.add( (String) tagsSequence[which]);
						} else if (selectedTags.contains((String)tagsSequence[which])) {
							selectedTags.remove((String)tagsSequence[which]);
						}
					}
				})
				.setPositiveButton("Filter Claims", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
	        	tagSelectDialog = tagFilterBuilder.create();
	        	tagSelectDialog.show();
	        	return true;
			default:
	        	return false;
	    }
	    
	}
	
	private ArrayList<String> getAllTagsSequence() {
		ClaimListMapper clm = new ClaimListMapper(getApplicationContext(), currentUser);
		tagsList = clm.getAllTags();
		
		tagsBooleanArray = new boolean[tagsList.size()];
		
		return tagsList;
	}

	// Methods that are used for testing
	public AlertDialog getTagDialog() {
		return tagSelectDialog;
	}
	
	public ArrayList<String> getAllTagsList() {
		return tagsList;
	}
	
	public ArrayList<String> getSelectedTags() {
		return selectedTags;
	}
}

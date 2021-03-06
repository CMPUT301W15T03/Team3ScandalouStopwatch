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

package ca.ualberta.cs.scandaloutraveltracker.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.ClaimListAdapter;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.mappers.ClaimListMapper;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.ClaimList;
import ca.ualberta.cs.scandaloutraveltracker.models.User;

/**
 *  This activity is the first activity that is displayed to the user.
 *  The activity contains the list of claims that the user has and
 *  allows the user to make modifications to the claim they choose.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimListActivity extends Activity implements ViewInterface {
	private ListView claimsListView;
	private ClaimListAdapter claimListAdapter;
	private ClaimListController claimListController;
	private User currentUser;
	private UserController currentUserController;
	private int screenTypeTemp;
	private int statusTemp;
	
	private AlertDialog tagSelectDialog;
	private AlertDialog claimAlert;
	private AlertDialog deleteAlert;
	private AlertDialog commentAlert;
	private AlertDialog userModeAlert;
	
	private ArrayList<String> tagsList;
	private boolean tagsBooleanArray[];
	private ArrayList<String> selectedTags = new ArrayList<String>();
	private CharSequence[] tagsSequence;
	
	/**
	 * 	Called when the activity is created. Sets up the views and 
	 * 	listeners of the activity depending on the users current role.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_list);
		
		// Set user in ClaimApplication
		setApplicationUser();
		
		// Get Claims
		currentUser = ( (ClaimApplication) getApplication()).getUser();
		currentUserController = new UserController(currentUser);
		screenTypeTemp = -1;
		
		// Set layout elements
		claimsListView = (ListView) findViewById(R.id.claimListActivityList);
		
		// Set screen mode (User or Approver)
		setScreenMode();
		
		// Set buttons up on the screen
		setClickListener();
	}
	
	/**
	 * Used for testing that users have the correct claim list
	 * @return the claim list
	 */
	public ClaimList getCurrentClaimList() {
		return claimListController.getClaimList();
	}
	
	/**
	 * 	Called when the activity resumes from a background state. The method updates the screen 
	 * 	mode and corresponding views depending on if the user is in approver or claimant mode.
	 */
	@Override
	public void onResume() {
		super.onResume();
		setScreenMode();
	}

	/**
	 * 	notify the claim list adapter of changed data.
	 */
	@Override
	public void update() {
		claimListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 	Creates the unique action bar for the activity that includes various 
	 * 	options so changing the user role and how the claim list is displayed.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 	Executes options depending on which option the user picked from the action bar dropdown
	 * 	menu.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    Intent intent;
	    // Handle presses on the action bar items.
	    switch (item.getItemId()) {
	    	// Goes to the new claim activity screen
	    	case R.id.action_add_claim:
	    		intent = new Intent(ClaimListActivity.this, NewClaimActivity.class);
				startActivity(intent);
	    		return true;
	    	// Lets the user filter the claims via tags
	    	case R.id.action_search_claims:
	    		showTagSearchDialog();
	    		return true;
	    	// Goes to "main" menu of the app while clearing the activity stack.
	        case R.id.action_user:
	        	intent = new Intent(ClaimListActivity.this, UserSelectActivity.class);
	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
	            return true;
	        // Change the User from approver to claimant or vice versa.
	        case R.id.action_role:
	        	screenTypeTemp = -1;
	        	AlertDialog.Builder builder = new AlertDialog.Builder(ClaimListActivity.this);
				builder.setTitle("Switch Role View")
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
							Toast.makeText(getApplicationContext(), "Role Unchanged",Toast.LENGTH_SHORT).show();
						}
						// Claimant Mode
						if (screenTypeTemp == 0) {
							Toast.makeText(getApplicationContext(), "Change to Claimant",Toast.LENGTH_SHORT).show();
							currentUserController.setMode(0);
							claimListController = new ClaimListController(currentUser);
							claimListController.addView(ClaimListActivity.this); // Testing to add view for claimsLists
							claimListController.sortNewFirst();
							claimListAdapter = new ClaimListAdapter(ClaimListActivity.this, claimListController.getClaimList(), false);
							claimsListView.setAdapter(claimListAdapter);
						}
						// Approver mode
						if (screenTypeTemp == 1) {
							Toast.makeText(getApplicationContext(), "Change to Approver",Toast.LENGTH_SHORT).show();
							currentUserController.setMode(1);
							claimListController = new ClaimListController(currentUser, Constants.APPROVER_MODE);
							claimListController.addView(ClaimListActivity.this);
							claimListController.sortLastFirst();
							claimListAdapter = new ClaimListAdapter(ClaimListActivity.this, claimListController.getClaimList(), true);
							claimsListView.setAdapter(claimListAdapter);
						}
					}
				})
				// record which one was selected for confirmation
				.setSingleChoiceItems(R.array.role_menu, currentUserController.getMode(), new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						screenTypeTemp = which;
					}
				}); 
				userModeAlert = builder.create();
				userModeAlert.show();
				return true;
	        case R.id.action_restore_claims:
	        	if (currentUserController.getMode() == 1) {
	        		currentUserController.setMode(0);
	        	}
	        	claimListController = new ClaimListController(currentUser);
				claimListController.addView(ClaimListActivity.this); // Testing to add view for claimsLists
				claimListController.sortNewFirst();
				claimListAdapter = new ClaimListAdapter(ClaimListActivity.this, claimListController.getClaimList(), false);
				claimsListView.setAdapter(claimListAdapter);
	        	return true;
			default:
	        	return false;
	    }
	    
	}
	
	/**
	 * Shows a dialog containing all the tags the user has entered and allows
	 * the user to select tags they wish to search for
	 */
	private void showTagSearchDialog() {
    	tagsList = getAllTagsSequence();
    	selectedTags = new ArrayList<String>();
    	tagsSequence = tagsList.toArray(new CharSequence[tagsList.size()]);	
    	AlertDialog.Builder tagFilterBuilder = new AlertDialog.Builder(ClaimListActivity.this);
    	tagFilterBuilder.setTitle("Find Claims Tagged With:")
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
		.setPositiveButton("Search Claims", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				claimListController = new ClaimListController(currentUser, Constants.TAG_MODE, selectedTags);
				claimListController.addView(ClaimListActivity.this); // Testing to add view for claimsLists
				claimListController.sortNewFirst();
				claimListAdapter = new ClaimListAdapter(ClaimListActivity.this, claimListController.getClaimList(), false);
				claimsListView.setAdapter(claimListAdapter);
			}
		});
    	tagSelectDialog = tagFilterBuilder.create();
    	tagSelectDialog.show();
	}
	
	/**
	 * 	Set up the listeners for the claim list and other buttons.
	 */
	private void setClickListener() {		
		//when claim is clicked alert dialog appears with edit/view claim, list expenses, add expense, delete claim, approve/reject claim
		claimsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, final long claimPos) {
				
				final int claimId = claimListController.getClaim((int) claimPos).getId();
				
				//http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog 2015-02-01
				//http://stackoverflow.com/questions/8227820/alert-dialog-two-buttons 2015-02-01
				AlertDialog.Builder builder = new AlertDialog.Builder(ClaimListActivity.this);
				builder.setTitle("Claim Options")
				.setCancelable(true);
				
				ClaimController cc = new ClaimController(new Claim(claimId));
				
				if (currentUser.getMode() == 1) {
					buildApproversOption(builder, claimId, claimPos);
				} else if (cc.getApproverCommentsString().length() != 0) {
					buildWithCommentsOption(builder, claimId, claimPos);
				} else {
					buildNoCommentsOption(builder, claimId, claimPos);
				}
				
				claimAlert = builder.create();
				claimAlert.show();
			}
		});
	}
	
	/**
	 * Builds the claim options with options for the approver
	 * @param builder The builder that has built the base of the alert dialog
	 * @param claimId The claim's Id
	 * @param claimPos The claim's position within
	 */
	private void buildApproversOption(AlertDialog.Builder builder, final int claimId, final long claimPos) {
		builder.setItems(R.array.claim_menu_approver, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
          	   //when edit/view claim is pressed
          	   if (which == 0){ 
          		   viewClaimPressed(claimId);
          	   }
          	   //when list expenses is pressed
          	   else if (which == 1){
          		   viewExpensesForClaimPressed(claimId);
          	   }
         	   //when rejecting/approving claim is pressed
         	   else if (which == 2) {
         		   rejectOrApproveClaim(claimId); 
         	   }
            }
		});
	}
	
	/**
	 * Builds the default claim options
	 * @param builder The builder that has built the base of the alert dialog
	 * @param claimId The claim's Id
	 * @param claimPos The claim's position within
	 */
	private void buildNoCommentsOption(AlertDialog.Builder builder, final int claimId, final long claimPos) {
		builder.setItems(R.array.claim_menu_claimant_no_comment, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
         	   //when edit/view claim is pressed
         	   if (which == 0){ 
         		   viewClaimPressed(claimId);
         	   }
         	   //when list expenses is pressed
         	   else if (which == 1){
         		   viewExpensesForClaimPressed(claimId);
         	   }
         	   //when add expense is pressed
         	   else if(which == 2){
         		   addExpenseToClaimPressed(claimPos, claimId);
         	   }
         	   //when delete claim is pressed
         	   else if (which == 3){
         		   deleteClaimSelected(claimId, claimPos);
         	   }
            }
		});
	}
	
	/**
	 * Builds the claim options with an option to view the attached comment.
	 * @param builder The builder that has built the base of the alert dialog
	 * @param claimId The claim's Id
	 * @param claimPos The claim's position within
	 */
	private void buildWithCommentsOption(AlertDialog.Builder builder, final int claimId, final long claimPos) {
		builder.setItems(R.array.claim_menu_claimant_comment, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

          	   //when edit/view claim is pressed
          	   if (which == 0){ 
          		   viewClaimPressed(claimId);
          	   }
          	   //when list expenses is pressed
          	   else if (which == 1){
          		   viewExpensesForClaimPressed(claimId);
          	   }
          	   //when add expense is pressed
          	   else if(which == 2){
          		   addExpenseToClaimPressed(claimPos, claimId);
          	   }
          	   //when delete claim is pressed
          	   else if (which == 3){
          		   deleteClaimSelected(claimId, claimPos);
          	   }
         	   //when view comments is pressed
         	   else if (which == 4) {
         		   ClaimListActivity.this.showCommentsDialog(claimPos);
         		   return;
         	   }
            }
		});
	}
	
	/**
	 * Deletes the claim at the selected position of the claims list view
	 * @param claimId The claim's Id
	 * @param claimPos The claim's position within
	 */
	private void deleteClaimSelected(final int claimId, final long claimPos) {
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
     		   deleteAlert = builder.create();
     		   deleteAlert.show();
		   }
		   else {
			   Toast.makeText(getApplicationContext(), 
					   		  currentClaim.getStatus() + " claims can not be edited.", 
					   		  Toast.LENGTH_SHORT).show();
		   }
	}
	
	/**
	 * View the claim at the selected position of the claims list view
	 * @param claimId The claim's Id
	 * @param claimPos The claim's position within
	 */
	private void viewClaimPressed(int claimId) {
		Intent intent = new Intent(ClaimListActivity.this, EditClaimActivity.class);
		intent.putExtra(Constants.claimIdLabel, claimId);
		startActivity(intent);
	}
	
	/**
	 * Shows expenses for the claim at the selected position of the claims list view
	 * @param claimId The claim's Id
	 * @param claimPos The claim's position within
	 */
	private void viewExpensesForClaimPressed(int claimId) {
		Intent intent = new Intent(ClaimListActivity.this, ExpenseListActivity.class);
	    intent.putExtra(Constants.claimIdLabel, claimId);
	    startActivity(intent);
	}
	
	/**
	 * Adds an expense to the claim selected from the listview.
	 * @param claimId The claim's Id
	 * @param claimPos The claim's position within
	 */
	private void addExpenseToClaimPressed(final long claimPos, int claimId) {
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
	
	/**
	 * Allows the approver to reject or approve the claim associated with the claimId passed
	 * @param claimId The claim's Id
	 */
	private void rejectOrApproveClaim(final int claimId) {
	   statusTemp = -1;
	   if (currentUser.getMode() == 0) {
		   Toast.makeText(getApplicationContext(), 
			   		 "Can only change claim status in approver mode!", 
			   		  Toast.LENGTH_SHORT).show();
	   }
	   if (currentUser.getMode() == 1) {
		   if (!(new Claim(claimId).getApproverName().equals(""))) {
			   if (!new Claim(claimId).getApproverName().equals(currentUser.getName())) {
				   Toast.makeText(getApplicationContext(), "Only " + new Claim(claimId).getApproverName() 
						   + " is allowed to change the status of this claim",Toast.LENGTH_LONG).show();
				   return;
			   }
		   }
		   final EditText input = new EditText(ClaimListActivity.this);
		   input.setHint("Add comment here");
		   input.setLines(10);
		   AlertDialog.Builder b = new AlertDialog.Builder(ClaimListActivity.this);
		   b.setTitle("Change Claim Status")
		   .setCancelable(true)
		   .setView(input)
		   .setSingleChoiceItems(R.array.approver_choices, -1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						statusTemp = 0;
					}
					if (which == 1) {
						statusTemp = 1;
					}
				}
		   	})
		   	.setPositiveButton("Cancel", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				})
				.setNegativeButton("Confirm", null)
		   	;
		   	final AlertDialog alert = b.create();
		   	alert.show();  
		   	//http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked 2015-03-28
		   	alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ClaimController claimController = new ClaimController(new Claim(claimId));
						if (statusTemp == -1) {
							Toast.makeText(getApplicationContext(), "You need to pick a status to change the claim to"
									,Toast.LENGTH_SHORT).show();
							alert.show();
							return;
						}
						Editable value = input.getText();
						if (value.toString().equals("")) {
							input.setError("A comment needs to be included");
							input.requestFocus();
							alert.show();
							return;
						}
						// Change the claim status to Approved
						if (statusTemp == 0) {
							claimController.approveClaim(currentUser.getName(), value.toString(), claimId);
						}
						// Change the claim status to Returned
						else if (statusTemp == 1) {
							claimController.returnClaim(currentUser.getName(), value.toString(), claimId);
						}
						// should never reach this, just in case
						else {
							try {
								throw new Exception();
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}
						claimListController = new ClaimListController(currentUser, Constants.APPROVER_MODE);
						claimListController.addView(ClaimListActivity.this);
						claimListController.sortLastFirst();
						claimListAdapter = new ClaimListAdapter(ClaimListActivity.this, claimListController.getClaimList(), true);
						claimsListView.setAdapter(claimListAdapter);
						update();
						alert.dismiss();
					}
				});
	   }  
	}
	
	/**
	 * Shows the comments dialog for the selected claim in the list view. 
	 * @param claimPos The claim's position within
	 */
	private void showCommentsDialog(final long claimPos) {
		int currentClaimId = claimListController.getClaim((int)claimPos).getId();
		ClaimController cc = new ClaimController(new Claim(currentClaimId));
		String comments = cc.getApproverCommentsString();
		//check that there are comments to display
		if (comments.length() != 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ClaimListActivity.this);
     		builder.setMessage(comments)
     		.setCancelable(true)
     		.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                 	   
                    }
                });
     		 commentAlert = builder.create();
     		 commentAlert.show();
		   } else {
			   Toast.makeText(getApplicationContext(), "No comments to display",Toast.LENGTH_SHORT).show();
		   }
		 // only comments of returned or approved claims should be viewable
		 if (currentUser.getMode() == 0) {
			 if (cc.getStatus().equals("In progress") 
					 || (cc.getStatus().equals("Submitted"))) {
				 Toast.makeText(getApplicationContext(), "Comments can only be viewed on " +
				   "returned or approved claims",Toast.LENGTH_SHORT).show();
			   }
		   }
	}
	
	/**
	 * @return the list of every tag from all claims in the list
	 */
	private ArrayList<String> getAllTagsSequence() {
		ClaimListMapper clm = new ClaimListMapper(getApplicationContext(), currentUser);
		tagsList = clm.getAllTags();
		
		tagsBooleanArray = new boolean[tagsList.size()];
		
		return tagsList;
	}
	
	/**
	 * 	Sets the app user to the correct user that the user picked.
	 */
	private void setApplicationUser() {
		ClaimApplication app = (ClaimApplication) getApplicationContext();
		Intent intent = getIntent();
		int userId = intent.getIntExtra("userId", 0);
		app.setUser(new User(userId));
	}
	
	/**
	 * 	Changes the claim list depending on if the user is in claimant mode or approver mode.
	 */
	private void setScreenMode() {
		// Claimant mode
		if (currentUserController.getMode() == 0) { 
			claimListController = new ClaimListController(currentUser);
			claimListController.addView(this); // Testing to add view for claimsLists
			claimListController.sortNewFirst();
			claimListAdapter = new ClaimListAdapter(this, claimListController.getClaimList(), false);
			claimsListView.setAdapter(claimListAdapter);
		}
		
		// Approver mode
		else if (currentUserController.getMode() == 1) {
			claimListController = new ClaimListController(currentUser, Constants.APPROVER_MODE);
			claimListController.addView(this);
			claimListController.sortLastFirst();
			claimListAdapter = new ClaimListAdapter(this, claimListController.getClaimList(), true);
			claimsListView.setAdapter(claimListAdapter);
		}
		
		claimListController.addView(this); // Testing to add view for claimsLists
	}

	// Methods that are used for testing
	/**
	 * 	Used for testing
	 * @return the current tag alert dialog
	 */
	public AlertDialog getTagDialog() {
		return tagSelectDialog;
	}
	
	/**
	 * 	Used for testing
	 * @return the alert dialog of the current claim selected
	 */
	public AlertDialog getClaimOptionsDialog() {
		return claimAlert;
	}
	
	/**
	 * 	Used for testing
	 * @return the alert dialog for deleting a claim
	 */
	public AlertDialog getDeleteDialog() {
		return deleteAlert;
	}
	
	/**
	 *  Used for testing
	 * @return the user mode alert dialog
	 */
	public AlertDialog getUserModeDialog() {
		return userModeAlert;
	}
	
	/**
	 *  Used for testing
	 * @return the current tags list of all claims
	 */
	public ArrayList<String> getAllTagsList() {
		return tagsList;
	}
	
	/**
	 *  Used for testing
	 * @return the tags that were selected to filter the claims list by
	 */
	public ArrayList<String> getSelectedTags() {
		return selectedTags;
	}
}

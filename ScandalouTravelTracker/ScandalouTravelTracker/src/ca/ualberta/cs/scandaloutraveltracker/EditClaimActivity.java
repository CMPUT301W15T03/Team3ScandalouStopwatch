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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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


/**
 *  This activity contains a claim that was selected from the ClaimListActivity
 *  and allows you to edit the claim.
 * @author Team3ScandalouStopwatch
 *
 */
public class EditClaimActivity extends Activity implements ViewInterface {

	private int claimId;
	private Context context;
	private ClaimController claimController;
	
	private TextView statusDisplay;
	private EditText startDateDisplay;
	private EditText endDateDisplay;
	private EditText descriptionDisplay;
	private TextView tagsDisplay;
	private ImageButton newDestinationButton;
	private ListView destinationList;
	private EditText tagsInput;
	private Button editTagsButton;
	private Button updateButton;
	private Button sendButton;
	
	private DestinationListAdapter destinationsAdapter;
	

	private Date startDate;
	private Date endDate;
	private String description;
	private ArrayList<Destination> destinations;
	private boolean canEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);

		context = this;
		
		Intent intent;
		
	    // Get the message from the intent
	    intent = getIntent();
	    claimId = intent.getIntExtra(Constants.claimIdLabel, 0);
		
		// Get view elements
		startDateDisplay = (EditText) findViewById(R.id.edit_claim_start_date);
		endDateDisplay = (EditText) findViewById(R.id.edit_claim_end_date);
		descriptionDisplay = (EditText) findViewById(R.id.edit_claim_descr);
		tagsDisplay = (TextView) findViewById(R.id.edit_claim_tags);
		newDestinationButton = (ImageButton) findViewById(R.id.edit_claim_new_destination);
		destinationList = (ListView) findViewById(R.id.edit_claim_destinations);
		editTagsButton = (Button) findViewById(R.id.edit_claim_edit_tags);
		sendButton = (Button) findViewById(R.id.edit_claim_send);	
		updateButton = (Button) findViewById(R.id.edit_claim_update);
	    statusDisplay = (TextView) findViewById(R.id.edit_claim_status);		
		
	    claimController = new ClaimController(new Claim(claimId));
	    canEdit = claimController.getCanEdit();
	    
	    claimController.addView(this);
		update();		
		
		// Disable clicking on descriptionDisplay if can't edit
		// Remove update button from the screen
		if (!canEdit) {
		    
			updateButton.setVisibility(View.INVISIBLE);
			newDestinationButton.setVisibility(View.INVISIBLE);
			sendButton.setVisibility(View.INVISIBLE);
		    statusDisplay.setText(Constants.statusSubmitted);
		    statusDisplay.setVisibility(View.VISIBLE);			
			
			descriptionDisplay.setFocusable(false);
			descriptionDisplay.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
						Toast.makeText(getApplicationContext(),
								claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
				}
				
			});
		} else {
			updateButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v){
					
					// Get fields
					String description = descriptionDisplay.getText().toString();
					boolean canEdit = true;
					
					claimController.updateClaim(startDate, endDate, description, destinations, canEdit);

					ClaimListController claimListController = new ClaimListController();
					claimListController.removeClaim(claimId);
					claimListController.addClaim(new Claim(claimId));
					
					Toast.makeText(getApplicationContext(),
							"Changes saved.", Toast.LENGTH_SHORT).show();
				}
				
			});
			newDestinationButton.setOnClickListener(new View.OnClickListener() {
				@SuppressLint("InflateParams") @Override
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
			sendButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v){
					if (canEdit) {
						boolean canSend = canClaimBeSent();
						if (canSend) {
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
										
										claimController.submitClaim(Constants.statusSubmitted, false);
										
										ClaimListController claimListController = new ClaimListController();
										claimListController.removeClaim(claimId);
										claimListController.addClaim(new Claim(claimId));
										
										finish();
									}  
							   });
							AlertDialog alert = builder.create();
							alert.show();
						} 
					} else {
						Toast.makeText(getApplicationContext(),
								claimController.getStatus() + " claims cannot be sent.", Toast.LENGTH_SHORT).show();
					}
					
				}
				
			});			
		}

        // startDate dialog picker
		startDateDisplay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canEdit) {
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
							claimController.setStartDate(date);
						}
					};
					newFragment.show(getFragmentManager(), "datePicker");
				}
				else {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        // endDate dialog picker
		endDateDisplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (canEdit) {
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
							claimController.setEndDate(date);
						}
					};
						newFragment.show(getFragmentManager(), "datePicker");
				}
				else {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
				}
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
				builder.setMessage("Please separate claims by commas.")
						
				   .setView(tagsInput)
				   .setCancelable(true)
				   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				       	public void onClick(DialogInterface dialog, int i) {
				       	}
				   })
				   .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int i) {

							String tagsString = tagsInput.getText().toString();
							ArrayList<String> tagsList = getTagsList(tagsString);							
	
							claimController.updateTags(tagsList);
							
							ClaimListController claimListController = new ClaimListController();
							claimListController.removeClaim(claimId);
							claimListController.addClaim(new Claim(claimId));							
						}  
				   });
				AlertDialog alert = builder.create();
				alert.show();				
				
			}
			
		});					
		
	}
	
	/**
	 * Checks if the Claim has enough information to be sent to
	 * an approver.
	 * @return boolean if claim can be sent
	 */
	private boolean canClaimBeSent() {
		if (claimController.getDestinations().size() < 1) {
			Toast.makeText(getApplicationContext(), "Please add a destination before submitting",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (claimController.getExpenseList().size() < 1) {
			Toast.makeText(getApplicationContext(), "Please add an expense before submitting",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (claimController.getDescription() == null) {
			return false;
		} 
		
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_claim, menu);
		return true;
	}

	@Override
	public void update() {
		
		//status = claimController.getStatus();
		startDate = claimController.getStartDate();
		endDate = claimController.getEndDate();
		description = claimController.getDescription();
		ArrayList<String> tagsList = claimController.getTags();
		destinations = claimController.getDestinations();
		
		if (tagsList == null) {
			return;
		}
		
		// Process more complicated claim info
		String tagsString = getTagsString(tagsList);
		
		// Update view elements with claim info
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
		//statusDisplay.setText(status);
		startDateDisplay.setText(sdf.format(startDate));
		endDateDisplay.setText(sdf.format(endDate));
		descriptionDisplay.setText(description);
		tagsDisplay.setText(tagsString);
		destinationsAdapter = new DestinationListAdapter(this, "editClaim", destinations, canEdit);
		destinationList.setAdapter(destinationsAdapter);
		
	}
	
	/**
	 * Parses the tagString to give an ArrayList of tags
	 * @param tagsString String of tags separated by commas
	 * @return ArrayList with just the tag names
	 */
	public ArrayList<String> getTagsList(String tagsString){
		
		String[] temp = tagsString.split(", ");
		// CITATION http://stackoverflow.com/questions/10530353/convert-string-array-to-arraylist
		// 2015-03-13
		// Matten's answer
		ArrayList<String> tags = new ArrayList<String>(Arrays.asList(temp));
		
		return tags;
	}
	
	/**
	 * Given the list of tags this changes it into a string
	 * @param tagsList
	 * @return string of tags
	 */
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

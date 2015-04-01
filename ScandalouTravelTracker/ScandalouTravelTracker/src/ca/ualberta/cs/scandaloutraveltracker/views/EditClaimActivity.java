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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.DatePickerFragment;
import ca.ualberta.cs.scandaloutraveltracker.DestinationListAdapter;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.TagParser;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Destination;
import ca.ualberta.cs.scandaloutraveltracker.models.IntegerPair;

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
	private Button addTagsButton;
	private Button updateButton;
	private Button sendButton;
	private AlertDialog alert;
	
	private DestinationListAdapter destinationsAdapter;
	
	private SpannableString spannableString;
	private ArrayList<IntegerPair> indices;
	private int toastsShown;
	
	private Date startDate;
	private Date endDate;
	private String description;
	private ArrayList<Destination> destinations;
	private boolean canEdit;
	private boolean alertReady;
	
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
		startDateDisplay = (EditText) findViewById(R.id.appr_edit_claim_start_date);
		endDateDisplay = (EditText) findViewById(R.id.edit_claim_end_date);
		descriptionDisplay = (EditText) findViewById(R.id.edit_claim_descr);
		tagsDisplay = (TextView) findViewById(R.id.edit_claim_tags);
		newDestinationButton = (ImageButton) findViewById(R.id.edit_claim_new_destination);
		destinationList = (ListView) findViewById(R.id.edit_claim_destinations);
		addTagsButton = (Button) findViewById(R.id.edit_claim_add_tag);
		sendButton = (Button) findViewById(R.id.edit_claim_send);	
		updateButton = (Button) findViewById(R.id.edit_claim_update);
	    statusDisplay = (TextView) findViewById(R.id.edit_claim_status);		
		
	    claimController = new ClaimController(new Claim(claimId));
	    canEdit = claimController.getCanEdit();
	    toastsShown = 0;
	    
	    claimController.addView(this);
	    if (claimId != 0) {
	    	update();		
	    }	
		
		// Disable clicking on descriptionDisplay if can't edit
		// Remove update button from the screen
		if (!canEdit) {
			setCantEdit();
		} else {
			setCanEdit();
		}

		setButtons();
	}
	
	private void setButtons() {
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
					toastsShown++;
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
					toastsShown++;
				}
			}
		});	
		
		addTagsButton.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v){
			    tagsInput = new EditText(EditClaimActivity.this);	
				
				//http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog 2015-02-01
				//http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked 2015-03-22
				alert = new AlertDialog.Builder(EditClaimActivity.this)
				   .setMessage("Enter name of tag (no spaces): ")	
				   .setView(tagsInput)
				   .setCancelable(true)
				   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				       	public void onClick(DialogInterface dialog, int i) {
				       	}
				   })
					.setPositiveButton("Add", null)
					.create();			
				
				alert.setOnShowListener(new DialogInterface.OnShowListener() {
					
					@Override
					public void onShow(DialogInterface dialog) {
						if (alertReady == false) {
							Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
							button.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {

									String tagString = "#"+tagsInput.getText().toString();		
									
									// Check if the tag contains a space
									if (tagString.contains(" ")) {
										Toast.makeText(getApplicationContext(), "Please remove the space from your tag.", 
													   Toast.LENGTH_SHORT).show();
									} else {
										// Add tag to current claim and then update/save tags
										claimController.addTag(tagString);
										claimController.updateTags(claimController.getTags());
										
										// Updates the ClaimList on the main screen and saves the ClaimList
										ClaimListController claimListController = new ClaimListController();
										claimListController.removeClaim(claimId);
										claimListController.addClaim(new Claim(claimId));		
										update();
										alert.dismiss();
									}

								}
							});
						}
						
					}
				});
				
				alert.show();
			}
			
		});			
	}
	
	private void setCantEdit() {
		updateButton.setVisibility(View.INVISIBLE);
		newDestinationButton.setVisibility(View.INVISIBLE);
		sendButton.setVisibility(View.INVISIBLE);
	    statusDisplay.setText(claimController.getStatus());
	    statusDisplay.setVisibility(View.VISIBLE);			
		
		descriptionDisplay.setFocusable(false);
		descriptionDisplay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
					toastsShown++;
			}
			
		});
	}
	
	private void setCanEdit() {
		updateButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v){
				
				// Get fields
				description = descriptionDisplay.getText().toString();
				canEdit = true;
				
				try {
					
					// Throws exception
					claimController.updateClaim(startDate, endDate, description, destinations, canEdit);
					
					ClaimListController claimListController = new ClaimListController();
					claimListController.removeClaim(claimId);
					claimListController.addClaim(new Claim(claimId));
					
					Toast.makeText(getApplicationContext(),
							"Changes saved.", Toast.LENGTH_SHORT).show();
					
				} catch (UserInputException e) {

					System.out.println(e.getMessage());
					Toast.makeText(getApplicationContext(),
							e.getMessage(), Toast.LENGTH_SHORT).show();
				}

				
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
				 nameInput.setLines(2);
				 final EditText descriptionInput = (EditText) newDestView.findViewById(R.id.edit_destination_description);
				 descriptionInput.setLines(2);
		
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
					boolean canSend = claimController.canClaimBeSent();
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
									
									//check for flagged expense(s) first
									if (claimController.checkIncompleteExpenses()) {	//expense(s) are flagged
										//http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog 2015-02-01
										//http://stackoverflow.com/questions/8227820/alert-dialog-two-buttons 2015-02-01
										AlertDialog.Builder builder2 = new AlertDialog.Builder(EditClaimActivity.this);
										builder2.setMessage("Expense(s) are flagged as incomplete. Submit anyway?")
										   .setCancelable(true)
										   .setNegativeButton("Don't Submit", new DialogInterface.OnClickListener() {
										       	public void onClick(DialogInterface dialog, int i) {
											    	dialog.cancel();
										       	}
										   })
										   .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int i) {
													
													//save claim details
													description = descriptionDisplay.getText().toString();
													try {
														claimController.updateClaim(startDate, endDate, description, destinations, canEdit);
														//submit claim
														claimController.submitClaim();
														
														ClaimListController claimListController = new ClaimListController();
														claimListController.removeClaim(claimId);
														claimListController.addClaim(new Claim(claimId));
														
														finish();
														
													} catch (UserInputException e) {
														// TODO Auto-generated catch block
														System.out.println(e.getMessage());
														Toast.makeText(getApplicationContext(),
																e.getMessage(), Toast.LENGTH_SHORT).show();
													}
													
												}  
										   });
										alert = builder2.create();
										alert.show();
									} else {
										//save claim details
										description = descriptionDisplay.getText().toString();
										try {
											claimController.updateClaim(startDate, endDate, description, destinations, canEdit);
											//submit claim
											claimController.submitClaim();
											
											ClaimListController claimListController = new ClaimListController();
											claimListController.removeClaim(claimId);
											claimListController.addClaim(new Claim(claimId));
											
											finish();
											
										} catch (UserInputException e) {
											// TODO Auto-generated catch block
											System.out.println(e.getMessage());
											Toast.makeText(getApplicationContext(),
													e.getMessage(), Toast.LENGTH_SHORT).show();
										}
										
									}
								}  
						   });
						alert = builder.create();
						alert.show();
					} 
				} else {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be sent.", Toast.LENGTH_SHORT).show();
				}
				
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses
		switch(item.getItemId()) {
		// Go to user select menu
		case R.id.action_user:
			Intent intent = new Intent(getApplicationContext(), UserSelectActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
            return true;
		
		// Go to activity that displays all the tags
		case R.id.action_view_all_tags:
			
		// Default do nothing
		default: 
			return false;
		}
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
		String tagsString = claimController.getTagsString();
		
		// Update view elements with claim info
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
		//statusDisplay.setText(status);

		startDateDisplay.setText(sdf.format(startDate));
		endDateDisplay.setText(sdf.format(endDate));
		descriptionDisplay.setText(description);
		destinationsAdapter = new DestinationListAdapter(this, "editClaim", destinations, canEdit);
		destinationList.setAdapter(destinationsAdapter);
		
		// Update the tags to be clickable
		// http://stackoverflow.com/questions/10696986/how-to-set-the-part-of-the-text-view-is-clickable 03/19/2015
		setClickableTags(tagsString);
		
		
	}
	
	private void setClickableTags(String tagsString) {
		spannableString = new SpannableString(tagsString);

		TagParser parser = new TagParser();
		indices = parser.parse(tagsString);

		for (int i = 0; i < indices.size(); i++) {
			IntegerPair currentIndex = indices.get(i);
			spannableString.setSpan(new ClickableSpan() {

				// Sets the onClick for the clickable span (tags)
				// When the tag is clicked it will bring up an alert dialog
				@Override
				public void onClick(View widget) {
					TextView tv = (TextView) widget;
					Spanned s = (Spanned) tv.getText();
					int start = s.getSpanStart(this);
					int end = s.getSpanEnd(this);
					final String currentTag = s.subSequence(start, end).toString();
					
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Options for " + s.subSequence(start, end).toString())
					.setCancelable(true)
					.setItems(R.array.tag_menu, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == 0) {
								final EditText tagRename = new EditText(EditClaimActivity.this);
								alert = new AlertDialog.Builder(EditClaimActivity.this)
								   .setMessage("Enter new tag name (no spaces): ")	
								   .setView(tagRename)
								   .setCancelable(true)
								   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								       	public void onClick(DialogInterface dialog, int i) {
								       	}
								   })
									.setPositiveButton("Rename", null)
									.create();			
								
								alert.setOnShowListener(new DialogInterface.OnShowListener() {
									
									@Override
									public void onShow(DialogInterface dialog) {
										Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
										button.setOnClickListener(new View.OnClickListener() {
											
											@Override
											public void onClick(View v) {

												String tagString = "#"+tagRename.getText().toString();		
												
												// Check if the tag contains a space
												if (tagString.contains(" ")) {
													Toast.makeText(getApplicationContext(), "Please remove the space from your tag.", 
																   Toast.LENGTH_SHORT).show();
												} else {
													// Remove current tag from current claim
													claimController.removeTag(currentTag);
													
													// Add tag to current claim and then update/save tags
													claimController.addTag(tagString);
													claimController.updateTags(claimController.getTags());
													
													// Updates the ClaimList on the main screen and saves the ClaimList
													ClaimListController claimListController = new ClaimListController();
													claimListController.removeClaim(claimId);
													claimListController.addClaim(new Claim(claimId));		
													update();
													alert.dismiss();
												}

											}
										});
									}
								});
								
								alert.show();
							} else if (which == 1) {
								claimController.removeTag(currentTag);
								claimController.updateTags(claimController.getTags());
								
								// Updates the ClaimList on the main screen and saves the ClaimList
								ClaimListController claimListController = new ClaimListController();
								claimListController.removeClaim(claimId);
								claimListController.addClaim(new Claim(claimId));		
								update();
							}
						}
					});
					
					alert = builder.create();
					alert.show();
				}
				
			}, currentIndex.getX(), 
			   currentIndex.getY(), 0);
		}
		tagsDisplay.setText(spannableString);
		tagsDisplay.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	// TESTING METHODS
	public AlertDialog getAlertDialog() {
		return alert;
	}
	
	public SpannableString getSpannableString() {
		return spannableString;
	}
	
	public void editClaim(Date startDate, Date endDate, ArrayList<Destination> destinations) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.destinations = destinations;
	}
	
	public int getToastCount() {
		return toastsShown;
	}
	
}
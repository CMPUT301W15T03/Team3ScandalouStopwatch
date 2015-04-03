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
import java.util.Calendar;
import java.util.Date;

import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.DatePickerFragment;
import ca.ualberta.cs.scandaloutraveltracker.DestinationListAdapter;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.TagParser;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.R.array;
import ca.ualberta.cs.scandaloutraveltracker.R.id;
import ca.ualberta.cs.scandaloutraveltracker.R.layout;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Destination;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.models.IntegerPair;
import ca.ualberta.cs.scandaloutraveltracker.models.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  Activity that allows the user to add a new claim to the claim list.
 *  Accessed through the add button on the ClaimListActivity.
 * @author Team3ScandalouStopwatch
 *
 */
public class NewClaimActivity extends MenuActivity implements ViewInterface{
	
	private ClaimController claimController;
	private ArrayList<Destination> destinations;
	private Date startDate;
	private Date endDate;
	
	private Context context;
	private TextView tagsTV;
	private EditText sDateSet;
	private EditText eDateSet;
	private EditText descriptionSet;
	private Button claimOkButton;
	private ImageButton addDestButton;
	private Button addTagsButton;
	private ArrayList<String> tagsList;
	
	private ListView destList;
	private DestinationListAdapter destinationListAdapter;
	
	// For the destination alert
	private EditText nameInput;
	private EditText descriptionInput;
	private AlertDialog alertDialog;
	private View newDestView;
	
	// For the tag alert
	private EditText tagsInput;
	private AlertDialog alert;
	private boolean alertReady;
	private SpannableString spannableString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_claim);
		
		// Initialize new claim and destinations
		claimController = new ClaimController(new Claim());
		destinations = new ArrayList<Destination>();
		
		context = this;
		sDateSet = (EditText)findViewById(R.id.start_date);	
		eDateSet = (EditText)findViewById(R.id.end_date);	
		descriptionSet = (EditText)findViewById(R.id.edit_claim_description);
		
		tagsTV = (TextView)findViewById(R.id.new_claim_tags_tv);
		addTagsButton = (Button)findViewById(R.id.new_claim_add_tag);
		tagsList = new ArrayList<String>();
		
		addDestButton = (ImageButton) findViewById(R.id.add_dest_button);		
		claimOkButton = (Button) findViewById(R.id.claim_ok_button);
		
		destList = (ListView)findViewById(R.id.destinations_lv);
		destinationListAdapter = new DestinationListAdapter(this, "newClaim", destinations, true);
		
		update();

		claimOkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// Get fields
				String description = descriptionSet.getText().toString();
				String status = Constants.statusInProgress;
				boolean canEdit = true;
				ArrayList<Expense> expenses = new ArrayList<Expense>();
				
				try {
					// makes sure every destination has a location attached to them per requirements
					for (Destination temp : destinations) {
						if (temp.getLocation() == null) {
							Toast.makeText(getApplicationContext(),
									"Every destination has to have a location attached to it. " +
									"Click on a destination to attach a location.", Toast.LENGTH_SHORT).show();
							return;
						}
					}
					ClaimListController claimListController = new ClaimListController();
					
					// Get user
					Context context = NewClaimActivity.this;
					ClaimApplication app = (ClaimApplication) context.getApplicationContext();
					User user = app.getUser();
					
					// Create the claim
					int newClaimId = claimListController.createClaim(startDate, endDate, description, destinations, 
							tagsList, status, canEdit, expenses, user);
					
					// Add the claim to list
					claimListController.addClaim(new Claim(newClaimId));
					
					finish();
					
				} catch (UserInputException e) {

					System.out.println(e.getMessage());
					Toast.makeText(getApplicationContext(),
							e.getMessage(), Toast.LENGTH_SHORT).show();
				}		

			}
		});			
		
		setListeners();
	
	}

	@Override
	public void update() {
		destList.setAdapter(destinationListAdapter);
		String tagsString = getTagsString(tagsList);
		setClickableTags(tagsString);
	}
	
	private void setListeners() {
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
						claimController.setStartDate(date);
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
					claimController.setEndDate(date);
				}
			};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
		
		addDestButton.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InflateParams") @Override
			public void onClick(View v) {
			// http://newtoknow.blogspot.ca/2011/08/android-alert-dialog-with-multi-edit.html 13/3/15
				 LayoutInflater newDestInf = LayoutInflater.from(context);

				 //text_entry is an Layout XML file containing two text field to display in alert dialog
				 newDestView= newDestInf.inflate(R.layout.edit_destination, null);
				 nameInput = (EditText) newDestView.findViewById(R.id.edit_destination_name);
				 nameInput.setLines(2);
				 descriptionInput = (EditText) newDestView.findViewById(R.id.edit_destination_description);
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
								claimController.setDestinationViews(NewClaimActivity.this);

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
					 	
				alertDialog = newDest.create();
				alertDialog.show();
			}
		});
		
		// Add tag button
		addTagsButton.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v){
			    tagsInput = new EditText(NewClaimActivity.this);	
				
				alert = new AlertDialog.Builder(NewClaimActivity.this)
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
										tagsList.add(tagString);
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
	
	private void setClickableTags(String tagsString) {
		spannableString = new SpannableString(tagsString);
		Log.d("TAG", tagsString);

		TagParser parser = new TagParser();
		ArrayList<IntegerPair> indices = parser.parse(tagsString);

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
								final EditText tagRename = new EditText(NewClaimActivity.this);
								alert = new AlertDialog.Builder(NewClaimActivity.this)
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
													tagsList.remove(currentTag);
													
													// Add tag to current claim and then update/save tags
													tagsList.add(tagString);
															
													update();
													alert.dismiss();
												}

											}
										});
									}
								});
								
								alert.show();
							} else if (which == 1) {
								tagsList.remove(currentTag);
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
		tagsTV.setText(spannableString);
		tagsTV.setMovementMethod(LinkMovementMethod.getInstance());
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
				tags += tagsList.get(i) + " ";
			} else {
				tags += tagsList.get(i);
			}
		}
		
		return tags;
	}
	
	/**
	 * when a destination is being updated with the map various intents will be
	 * passed back so the correct destination and location value can be updated.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	        	Location location;
	        	int destinationPos;
	        	location = new Location("Expense Location");
	        	location.setLatitude(data.getDoubleExtra("latitude", 999));
	        	location.setLongitude(data.getDoubleExtra("longitude", 999));
	        	destinationPos = data.getIntExtra("destination", -1);
	        	
	        	Destination temp;
	        	temp = destinations.get(destinationPos);
	        	temp.setLocation(location);
	        	destinations.set(destinationPos, temp);
	        	destinationListAdapter.notifyDataSetChanged();
	        }
	        if (resultCode == RESULT_CANCELED) {
	        }
	    }
	}
	
	// TEST METHODS BELOW
	public void setStartDate(Date date) {
		startDate = date;
	}
	
	public void setEndDate(Date date) {
		endDate = date;
	}
	
	public AlertDialog getDestinationDialog() {
		return alertDialog;
	}
	
	public ArrayList<Destination> getDestinationsList() {
		return destinations;
	}
	
	public AlertDialog getAlert() {
		return alert;
	}
	
	public ArrayList<String> getTagsList() {
		return tagsList;
	}
	
	public SpannableString getSpannableString() {
		return spannableString;
	}
	
	public View getNewDestView() {
		return newDestView;
	}
}


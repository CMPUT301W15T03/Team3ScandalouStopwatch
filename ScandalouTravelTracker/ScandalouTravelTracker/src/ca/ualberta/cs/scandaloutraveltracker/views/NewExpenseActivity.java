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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.cs.scandaloutraveltracaker.mappers.ClaimMapper;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.DatePickerFragment;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.R.id;
import ca.ualberta.cs.scandaloutraveltracker.R.layout;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ExpenseController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  This is the activity that provides functionality for adding a new
 *  expense to the current claim.
 * @author Team3ScandalouStopwatch
 *
 */
public class NewExpenseActivity extends MenuActivity implements ViewInterface {
	private Button addExpenseButton;
	private Date date;
	private ClaimController CController;
	private ExpenseController EController;
	private ClaimListController claimListController;
	private EditText dateEditText;
	private Spinner categorySpinner;
	private EditText amountEditText;
	private Spinner currencySpinner;
	private EditText descriptionEditText;
	private EditText locationTextView;
	private ClaimMapper mapper;
	private Location location = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense);
		
		//create ClaimMapper for saving data
		mapper = new ClaimMapper(this.getApplicationContext());
		
		//create date picker
		dateEditText = (EditText)findViewById(R.id.date_expense2);
	
		//get widgets
		categorySpinner = (Spinner)findViewById(R.id.category);
		amountEditText = (EditText)findViewById(R.id.amount2);
		currencySpinner = (Spinner)findViewById(R.id.currency);
		descriptionEditText = (EditText)findViewById(R.id.description2);
		locationTextView = (EditText) findViewById(R.id.new_location_edit_text);
		locationTextView.setEnabled(false);
		
		setUpListeners();
	}
	
	private void setUpListeners() {
		// Set date onClickListener
				dateEditText.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						DialogFragment newFragment = new DatePickerFragment() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								String dateString = convertToString(year, monthOfYear, dayOfMonth);
								Calendar cal = Calendar.getInstance();
								cal.clear(Calendar.MILLISECOND);
								cal.set(year, monthOfYear, dayOfMonth);
								date = cal.getTime();
								dateEditText.setText(dateString);
							}
						};
						newFragment.show(getFragmentManager(), "datePicker");
					}
				});
				
				//create listener for Add button
				addExpenseButton = (Button)findViewById(R.id.add_expense_button);
				addExpenseButton.setOnClickListener(new View.OnClickListener() {
					
					@SuppressLint("DefaultLocale")
					@Override
					public void onClick(View v) {
						Intent intent = getIntent();
					    int claimId = intent.getIntExtra(Constants.claimIdLabel, 0);
					    CController = new ClaimController(new Claim(claimId));

					    if (dateEditText.getText().length()==0) {
							Toast.makeText(getApplicationContext(), "Please include a date", Toast.LENGTH_SHORT).show();
					    }
					    else if(date.before(CController.getStartDate())){
							Toast.makeText(getApplicationContext(), "Please include a date after Claim's Start Date", Toast.LENGTH_SHORT).show();
						}
					    else{
						Calendar cal = Calendar.getInstance();
					    cal.setTime(date);
					    cal.add(Calendar.DATE, -1);
					    Date date = cal.getTime();
						if(date.after(CController.getEndDate())){
							
							SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
							
							
							String endDate=(sdf.format(date));
							
							Toast.makeText(getApplicationContext(),endDate , Toast.LENGTH_SHORT).show();
						}
						
						else {
						cal.add(Calendar.DATE, 1);
						date = cal.getTime();
						//create new Expense, fill in values, attach to claim, close activity
						
						//make controller for current claim
									    			    
					    //make controller for new expense
						EController = new ExpenseController(new Expense());
						
						//fill in category
						String category = (String)categorySpinner.getSelectedItem();
						EController.setCategory(category);
						
						//fill in date
						EController.setDate(date);
						
						//fill in amount
						String costString = amountEditText.getText().toString();
						if (costString.equals(".")) {
							costString = "0";
						}
						else if(costString.isEmpty()){
							costString = "0";
						}
						costString = String.format("%.2f", Double.valueOf(costString));
						double amount = Double.valueOf(costString);
						EController.setCost(amount);
						
						//fill in currency
						String currency = (String)currencySpinner.getSelectedItem();
						EController.setCurrency(currency);
						
						//fill in description
						String description = descriptionEditText.getText().toString();
						EController.setDescription(description);
						
<<<<<<< HEAD
						//set location
						EController.setLocation(location);
=======
						// make an empty receipt path
						String receiptPath = null;
						EController.setReceiptPath(receiptPath);
>>>>>>> bc99b91505b1f79d7772847e3a402ddbc07e01ca
						
						//add new expense to claim and exit
						CController.addExpense(EController.getExpense());
						mapper.saveClaimData(claimId, "expenses", CController.getExpenseList());
						
						// Reload the claim list for the ClaimListActivity
						claimListController = new ClaimListController();
						claimListController.removeClaim(claimId);
						claimListController.addClaim(new Claim(claimId));
						
						setResult(RESULT_OK);
						finish();
						}
					}
					}
				});
	}
	
	@Override
	public void update() {
		//leave empty, never need to update
	}
	
	// TESTING METHODS
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void addLocation(View v) {
		Intent intent = new Intent(getApplicationContext(), SetExpenseLocationActivity.class);
		startActivityForResult(intent, 1);
	}
	
	//http://stackoverflow.com/questions/10407159/how-to-manage-start-activity-for-result-on-android/10407371#10407371 2015-03-31
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	        	location = new Location("Expense Location");
	        	location.setLatitude(data.getDoubleExtra("latitude", 0));
	        	location.setLongitude(data.getDoubleExtra("longitude", 0));
	        	locationTextView.setText("Lat: " + String.format("%.4f", location.getLatitude()) 
	        			+ "\nLong: " + String.format("%.4f", location.getLongitude()));
	        	
	        }
	        if (resultCode == RESULT_CANCELED) {
	        	location = null;
	        	locationTextView.setText(null);
	        	locationTextView.setHint("Location not set");
	        }
	    }
	}

}
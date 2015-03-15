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

/* AddExpenseActivity.java Basic Info:
 *  This is the activity that provides functionality for adding a new
 *  expense to the current claim.
 */
	
package ca.ualberta.cs.scandaloutraveltracker;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddExpenseActivity extends Activity implements ViewInterface {
	private Button addExpenseButton;
	private Date date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense);
		
		//create ClaimMapper for saving data
		final ClaimMapper mapper = new ClaimMapper(this.getApplicationContext());
		
		//create date picker
		final EditText dateEditText = (EditText)findViewById(R.id.date_expense2);
		dateEditText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						String dateString = convertToString(year, monthOfYear, dayOfMonth);
						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						date = cal.getTime();
						dateEditText.setText(dateString);
					}
				};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
		
		//get widgets
		final Spinner categorySpinner = (Spinner)findViewById(R.id.category);
		final EditText amountEditText = (EditText)findViewById(R.id.amount2);
		final Spinner currencySpinner = (Spinner)findViewById(R.id.currency);
		final EditText descriptionEditText = (EditText)findViewById(R.id.description2);
		
		//create listener for Add button
		addExpenseButton = (Button)findViewById(R.id.add_expense_button);
		addExpenseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//show warning if fields are left empty
				if (categorySpinner.getSelectedItem().toString().equals( "--Choose Category--")) {
					Toast.makeText(getApplicationContext(), "Please include a category", Toast.LENGTH_SHORT).show();
				}
				else if (amountEditText.getText().length()==0) {
					amountEditText.setError("Please include an amount");
					amountEditText.requestFocus();
				}
				else if (dateEditText.getText().length()==0) {
					Toast.makeText(getApplicationContext(), "Please include a date", Toast.LENGTH_SHORT).show();
				}
				else if ( currencySpinner.getSelectedItem().toString().equals( "--Choose Currency--")) {
					Toast.makeText(getApplicationContext(), "Please include a currency", Toast.LENGTH_SHORT).show();
				}
				else if (descriptionEditText.getText().length()==0) {
					descriptionEditText.setError("Please include a description");
					descriptionEditText.requestFocus();
				}
				
				else {
				
				//create new Expense, fill in values, attach to claim, close activity
				
				//make controller for current claim
				Intent intent = getIntent();
			    int claimId = intent.getIntExtra(Constants.claimIdLabel, 0);
			    Claim claim = new Claim(claimId);
			    ClaimController CController = new ClaimController(claim);
			    
			    //make controller for new expense
				Expense expense = new Expense();
				ExpenseController EController = new ExpenseController(expense);
				
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
				double amount = Double.valueOf(costString);
				EController.setCost(amount);
				
				//fill in currency
				String currency = (String)currencySpinner.getSelectedItem();
				EController.setCurrency(currency);
				
				//fill in description
				String description = descriptionEditText.getText().toString();
				EController.setDescription(description);
				
				//add new expense to claim and exit
				CController.addExpense(expense);
				mapper.saveClaimData(claimId, "expenses", CController.getExpenseList());
				
				// Reload the claim list for the ClaimListActivity
				ClaimListController claimListController = new ClaimListController();
				claimListController.removeClaim(claimId);
				claimListController.addClaim(new Claim(claimId));
				
				setResult(RESULT_OK);
				finish();
				}
			}
		});
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_expense, menu);
		return true;
	}*/
	
	@Override
	public void update() {
		//leave empty, never need to update
	}

}
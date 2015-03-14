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

/* EditExpenseActivity.java Basic Info:
 *  Activity takes an expense from the ExpenseListActivity and allows 
 *  the user to edit information about the expense.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditExpenseActivity extends Activity implements ViewInterface {

	private ClaimController claimController;
	private Claim currentClaim;
	private int claimId;
	private int expenseId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		//makes sure that the position of the claim and corresponding 
		//expense to be edited are actually passed to this activity
		Bundle extras = getIntent().getExtras();
		Intent intent = getIntent();
		claimId = (int) intent.getIntExtra(Constants.claimIdLabel, -1);
		expenseId = (int) extras.getLong("expenseId", -1);
		if (claimId == -1) {
			Toast.makeText(this, "The Claim Position needs to be added to the " +
					"EditExpenseActivity intent before startActivity(intent) is called",
					Toast.LENGTH_LONG).show();
			finish();
		}
		else if (expenseId == -1) {
			Toast.makeText(this, "The Expense Position needs to be added to the " +
					"EditExpenseActivity intent before startActivity(intent) is called",
					Toast.LENGTH_LONG).show();
			finish();
		}
		
		//TODO - Delete Toast Later
		Toast.makeText(this, "Expense Position " + expenseId,
				Toast.LENGTH_SHORT).show();
		
		//Set currentClaim to the claim that was selected via intent
		currentClaim = new Claim(claimId);
		claimController = new ClaimController(currentClaim);
		
		//initialize fields 
		EditText description = (EditText) findViewById(R.id.description);
		EditText date = (EditText) findViewById(R.id.date_expense);
		EditText cost = (EditText) findViewById(R.id.amount);
		Spinner category = (Spinner) findViewById(R.id.catspinner);
		Spinner currencyType = (Spinner) findViewById(R.id.currencyspinner);
		String categoryString = claimController.getExpense(expenseId).getCategory();
		String currencyString = claimController.getExpense(expenseId).getCurrencyType();
	
		//set fields to correct values
		description.setText(claimController.getExpense(expenseId)
				.getDescription());
		date.setText(claimController.getExpense(expenseId)
				.getDateString());
		cost.setText(""+claimController.getExpense(expenseId)
				.getCost());
		category.setSelection(getIndex(category, categoryString));
		currencyType.setSelection(getIndex(currencyType, currencyString));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_expense, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		final EditText dateSet = (EditText) findViewById(R.id.date_expense);
        //date dialog picker
		dateSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						String dateString = convertToString(year, monthOfYear, dayOfMonth);
						dateSet.setHint(dateString);
						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						Date date = cal.getTime();
						dateSet.setText(dateString);
					}
				};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
	}
	
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	//is called when edit button is clicked
	public void confirmEdit(View v) {
		//get the EditText fields
		EditText description = (EditText) findViewById(R.id.description);
		EditText date = (EditText) findViewById(R.id.date_expense);
		EditText cost = (EditText) findViewById(R.id.amount);
		Spinner category = (Spinner) findViewById(R.id.catspinner);
		Spinner currencyType = (Spinner) findViewById(R.id.currencyspinner);
		
		//parse the string input from user
		String descrString = description.getText().toString();
		String dateString = date.getText().toString();
		String costString = cost.getText().toString();
		String categoryString = category.getSelectedItem().toString();
		String currencyTypeString = currencyType.getSelectedItem().toString();
		
		//check multiple user input errors and get them to correct accordingly
		//category is required
		if (categoryString.equals("--Choose Category--")) {
			Toast.makeText(this, "Category Type is Required", Toast.LENGTH_SHORT).show();
			return;
		}
		//cost is required
		else if (costString.equals("")) {
			cost.setError("Cost is Required");
			cost.requestFocus();
			return;
		}
		//date is required
		else if (dateString.equals("")) {
			date.setError("Date is Required");
			date.requestFocus();
			return;
		}
		//currency is required
		else if (currencyTypeString.equals("--Choose Currency--")) {
			Toast.makeText(this, "Currency Type is Required", Toast.LENGTH_SHORT).show();
			return;
		}
		//description is required
		else if (descrString.equals("")) {
			description.setError("Description is Required");
			description.requestFocus();
			return;
		}
		//everything is good to be added (will need to check date though)
		else {
			Toast.makeText(this, "Good to go", Toast.LENGTH_SHORT).show();
		}
	}
	
	//http://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position 2015-03-14
	private int getIndex(Spinner spinner, String string) {
		int index = 0;
		for (int i=0;i<spinner.getCount();i++){
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)){
				index = i;
				break;
			}
		}
		return index;
	 }  

}

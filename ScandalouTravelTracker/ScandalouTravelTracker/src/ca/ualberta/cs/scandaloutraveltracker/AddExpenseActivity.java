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

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddExpenseActivity extends Activity implements ViewInterface {
	private Button addExpenseButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense);
		
		//create listener for Add button
		addExpenseButton = (Button)findViewById(R.id.add_expense_button);
		addExpenseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
				Spinner categorySpinner = (Spinner)findViewById(R.id.category);
				String category = (String)categorySpinner.getSelectedItem();
				EController.setCategory(category);
				// TODO: fill in date
				//fill in amount
				EditText amountEditText = (EditText)findViewById(R.id.amount2);
				double amount = Double.valueOf(amountEditText.getText().toString());
				EController.setCost(amount);
				//fill in currency
				Spinner currencySpinner = (Spinner)findViewById(R.id.currency);
				String currency = (String)currencySpinner.getSelectedItem();
				EController.setCurrency(currency);
				//fill in description
				EditText descriptionEditText = (EditText)findViewById(R.id.description2);
				String description = descriptionEditText.getText().toString();
				EController.setDescription(description);
				//add new expense to claim and exit
				CController.addExpense(expense);
				setResult(RESULT_OK);
				finish();
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
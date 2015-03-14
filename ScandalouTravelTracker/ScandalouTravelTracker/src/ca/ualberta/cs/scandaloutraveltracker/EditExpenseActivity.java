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

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditExpenseActivity extends Activity implements ViewInterface {

	private ClaimController claimController;
	private Claim currentClaim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		//makes sure that the position of the claim and corresponding 
		//expense to be edited are actually passed to this activity
		Bundle extras = getIntent().getExtras();
		int claimId =-1;
		int expenseId =-1;
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
		currentClaim = new Claim((int)claimId);
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
	}
	
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	//is called when edit button is clicked
	public void confirmEdit(View v) {
		Toast.makeText(this, "Editting done bro!",
				Toast.LENGTH_SHORT).show();
	}
	
	//http://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position 2015-03-14
	private int getIndex(Spinner spinner, String myString) {
		int index = 0;
		for (int i=0;i<spinner.getCount();i++){
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
				index = i;
				break;
			}
		}
		return index;
	 }  

}

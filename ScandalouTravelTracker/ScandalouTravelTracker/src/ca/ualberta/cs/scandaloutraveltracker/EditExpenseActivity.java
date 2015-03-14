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
		
		//// Set currentClaim to the claim that was selected via intent
		currentClaim = new Claim((int)claimId);
	    claimController = new ClaimController(currentClaim);
		
		EditText description = (EditText) findViewById(R.id.description);
		EditText date = (EditText) findViewById(R.id.date_expense);
		EditText cost = (EditText) findViewById(R.id.amount);
		Spinner category = (Spinner) findViewById(R.id.catspinner);
		Spinner currencyType = (Spinner) findViewById(R.id.currencyspinner);
		String categoryString = claimController.getExpense(expenseId).getCategory();
		String currencyString = claimController.getExpense(expenseId).getCurrencyType();
		
		
		ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.Category, 
				android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    category.setAdapter(adapter);
	    if (!categoryString.equals(null)) {
	        int spinnerPostion = adapter.getPosition(categoryString);
	        category.setSelection(spinnerPostion);
	        spinnerPostion = 0;
	    }
		
		Toast.makeText(this, "Expense Position " + expenseId,
				Toast.LENGTH_LONG).show();
		description.setText(claimController.getExpense(expenseId)
				.getDescription());
		/*date.setText(claimListController.getClaimList()
				.getClaim(claimId).getExpense(expenseId).getExpenseDateString());
		cost.setText(claimListController.getClaimList()
				.getClaim(claimId).getExpense(expenseId).getExpenseCostString());
		category.setSelection(claimListController.getClaimList()
				.getClaim(claimId).getExpense(expenseId).getCategoryInt());
		currencyType.setSelection(claimListController.getClaimList()
				.getClaim(claimId).getExpense(expenseId).getCurrencyInt());*/
		//get the correct category
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
	}
	
	//get the correct category spinner int value
	public int getCategoryInt(String cat) {
		if (cat.equals("air fare")){
			
		}
		else if (cat.equals("ground transport")){
			
		}
		else if (cat.equals("vehicle rental")){
			
		}
		else if (cat.equals("private automobile")){
			
		}
		else if (cat.equals("")){
			
		}
		else if (cat.equals("")){
			
		}
		else if (cat.equals("")){
			
		}
		else if (cat.equals("")){
			
		}
		else if (cat.equals("")){
			
		}
		else if (cat.equals("")){
			
		}
		else if (cat.equals("")){
			
		}
		else if (cat.equals("")){
			
		}
		return 0;
	}
	
	//get the correct currency spinner int value
	public int getCurrencyInt(String cur) {
		return 0;
	}

}

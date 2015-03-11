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

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditExpenseActivity extends Activity implements ViewInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		
		//makes sure that the position of the claim and corresponding 
		//expense to be edited are actually passed to this activity
		Bundle extras = getIntent().getExtras();
		long claimPos =-1;
		long expensePos =-1;
		claimPos = extras.getLong("claimPos" , -1);
		expensePos = extras.getLong("expensePos", -1);
		if (claimPos == -1) {
			Toast.makeText(this, "The Claim Position needs to be added to the " +
					"EditExpenseActivity intent before startActivity(intent) is called",
					Toast.LENGTH_LONG).show();
			finish();
		}
		else if (expensePos == -1) {
			Toast.makeText(this, "The Expense Position needs to be added to the " +
					"EditExpenseActivity intent before startActivity(intent) is called",
					Toast.LENGTH_LONG).show();
			finish();
		}
		
		EditText description = (EditText) findViewById(R.id.description);
		EditText date = (EditText) findViewById(R.id.date_expense);
		EditText cost = (EditText) findViewById(R.id.amount);
		Spinner category = (Spinner) findViewById(R.id.catspinner);
		Spinner currencyType = (Spinner) findViewById(R.id.currencyspinner);
		ClaimListController c = new ClaimListController(new ClaimList());
		
		/*
		description.setText(ClaimListController.getClaimList()
				.getClaim(claimPos).getExpense(expensePos).getExpenseDescription());
		date.setText(ClaimListController.getClaimList()
				.getClaim(claimPos).getExpense(expensePos).getExpenseDateString());
		cost.setText(ClaimListController.getClaimList()
				.getClaim(claimPos).getExpense(expensePos).getExpenseCostString());
		category.setSelection(ClaimListController.getClaimList()
				.getClaim(claimPos).getExpense(expensePos).getCategoryInt());
		currencyType.setSelection(ClaimListController.getClaimList()
				.getClaim(claimPos).getExpense(expensePos).getCurrencyInt());*/
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

}

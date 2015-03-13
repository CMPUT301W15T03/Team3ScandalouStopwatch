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

/* ExpenseListActivity.java Basic Info:
 *  This activity displays the list of expenses that is related to the
 *  current claim. From here you can edit expenses.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ExpenseListActivity extends Activity implements ViewInterface {
	private Button addExpenseButton;
	private ListView expenseListView ;
	private ExpenseListAdapter expenseListAdapter;
	private Claim currentClaim;
	private ClaimController claimController;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_list);

		//set layout elements
		addExpenseButton=(Button) findViewById(R.id.add_expense);
		//add button on click
		addExpenseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Add button below
				//Intent intent=new Intent(ExpenseListActivity.this, NewExpenseActivity.class);
				//startActivity(intent);
		
				
			}
		});
		

		
		// Set currentClaim to the claim that was selected via intent (currently fake claim)
		currentClaim = createTestClaim();
		addExpenses();
		
		expenseListView = (ListView) findViewById(R.id.expenselistView);
		expenseListAdapter = new ExpenseListAdapter(this, currentClaim.getExpenses());
		expenseListView.setAdapter(expenseListAdapter);
		
		expenseListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, final long expensePos) {
				
				//http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog 2015-03-11
				//http://stackoverflow.com/questions/8227820/alert-dialog-two-buttons 2015-03-11
				AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseListActivity.this);
				builder.setMessage("Expense Options")
				   .setCancelable(true)
				   .setNegativeButton("Edit/View Expense", new DialogInterface.OnClickListener(){
					   public void onClick(DialogInterface dialog, int i) {
					    	//edit claim
					   		Intent intent = new Intent(ExpenseListActivity.this, EditExpenseActivity.class);
					   		intent.putExtra("id", expensePos);
					   		startActivity(intent);
					   }
				   })
				  .setPositiveButton("Delete Expense", new DialogInterface.OnClickListener(){
					  
						public void onClick(DialogInterface dialog, int i) {
							//delete correct expense
						  Expense expense= currentClaim.getExpense(position);
						  currentClaim.deleteExpense(expense);
						  expenseListAdapter.notifyDataSetChanged();
					  }
				  })
				  .setNeutralButton("Flag/Unflag", new DialogInterface.OnClickListener(){
					  @Override
						public void onClick(DialogInterface dialog, int i) { 
						Expense expense=currentClaim.getExpense(position);
						expense.setFlag(true);
						Toast.makeText(getApplicationContext(), "flag", Toast.LENGTH_SHORT).show();
					  }
				  
			});
				AlertDialog alert = builder.create();
				alert.show();		
		}
	});
	}
	
	// TODO: DELETE THIS METHOD. USED FOR TESTING EXPENSELISTACTIVITY.
	public void addExpenses() {
		Expense expense1 = new Expense();
		Expense expense2 = new Expense();
		Expense expense3 = new Expense();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
		Date date;
		
		// Expense 1
		ExpenseController expenseController = new ExpenseController(expense1);
		expenseController.setCategory("Meal");
		expenseController.setCost(15.00);
		expenseController.setDescription("Meal at a super dope place");

		try {
			date = sdf.parse("01/01/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		expenseController.setDate(date);
		
		// Expense 2
		expenseController = new ExpenseController(expense2);
		expenseController.setCategory("Accomodation");
		expenseController.setCost(20.00);
		expenseController.setDescription("Stayed at a hostel");

		try {
			date = sdf.parse("02/02/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		expenseController.setDate(date);
		
		// Expense 3
		expenseController = new ExpenseController(expense3);
		expenseController.setCategory("Travel");
		expenseController.setCost(150.00);
		expenseController.setDescription("Took an expensive taxi");

		try {
			date = sdf.parse("03/03/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		expenseController.setDate(date);
		
		ClaimController claimController = new ClaimController(currentClaim);
		claimController.addExpense(expense3);
		claimController.addExpense(expense1);
		claimController.addExpense(expense2);
	}
	
	// For making a test claim (holding expenses)
	private Claim createTestClaim() {
		Claim newClaim = new Claim();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
		Date startDate;
		try {
			startDate = sdf.parse("01/01/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		Date endDate;
		try {
			endDate = sdf.parse("02/02/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		ClaimController claimController = new ClaimController(newClaim);
		claimController.setName("Test Claim");
		claimController.setStartDate(startDate);
		claimController.setEndDate(endDate);
		
		return newClaim;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_list, menu);
		return true;
	}

	@Override
	public void update() {
		expenseListAdapter.notifyDataSetChanged();
	}

}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  This activity displays the list of expenses that is related to the
 *  current claim. From here you can edit expenses.
 * @author Team3ScandalouStopwatch
 *
 */
public class ExpenseListActivity extends Activity implements ViewInterface {
	private Button addExpenseButton;
	private ListView expenseListView ;
	private ExpenseListAdapter expenseListAdapter;
	private int claimId;
	private Claim currentClaim;
	private ClaimController claimController;
	private ClaimMapper mapper;
	private boolean canEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_list);
		
		// Set currentClaim to the claim that was selected via intent
		Intent intent = getIntent();
	    claimId = intent.getIntExtra(Constants.claimIdLabel, 0);
	    currentClaim = new Claim(claimId);
	    canEdit = currentClaim.getCanEdit();
	    claimController = new ClaimController(currentClaim);
	    mapper = new ClaimMapper(this.getApplicationContext());
	    setViews();
	    
	    // Currently checking for expenseList being null (Until Mapper can map expenses)
	    if (claimController.getExpenseList() == null) {
	    	claimController.setExpenses(new ArrayList<Expense>());
	    }

		//set layout elements
		addExpenseButton = (Button) findViewById(R.id.add_expense);
		//add button on click
		addExpenseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//add expense
				Intent intent = new Intent(ExpenseListActivity.this, AddExpenseActivity.class);
				intent.putExtra(Constants.claimIdLabel, claimId);
				startActivity(intent);
			}
		});
		
		
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
					    	//edit expense
					   		Intent intent = new Intent(ExpenseListActivity.this, EditExpenseActivity.class);
					   		intent.putExtra("expenseId", expensePos);
					   		intent.putExtra(Constants.claimIdLabel, claimId);
					   		startActivity(intent);
					   }
				   })
				  .setPositiveButton("Delete Expense", new DialogInterface.OnClickListener(){
					  
						public void onClick(DialogInterface dialog, int i) {
						  if (canEdit) {
							  //delete correct expense
							  Expense expense = claimController.getExpense(position);
							  claimController.removeExpense(expense);
							  expense.notifyViews();
							  mapper.saveClaimData(claimId, "expenses", claimController.getExpenseList());
							  setViews();  
						  }
						  else {
	            			  Toast.makeText(getApplicationContext(), 
	            					   		  currentClaim.getStatus() + " claims can not be edited.", 
	            					   		  Toast.LENGTH_SHORT).show();
	            		  }
					    }

				  })
				  .setNeutralButton("Flag/Unflag", new DialogInterface.OnClickListener(){
					  @Override
						public void onClick(DialogInterface dialog, int i) {
							Expense expense = claimController.getExpense(position);
							if (canEdit) {
								//flag/unflag expense
								if (expense.getFlag() == false) {
									expense.setFlag(true);
									expense.notifyViews();
									mapper.saveClaimData(claimId, "expenses", claimController.getExpenseList());
									setViews();
									Toast.makeText(getApplicationContext(), "Expense Flagged", Toast.LENGTH_SHORT).show();
								}
								else {
									expense.setFlag(false);
									expense.notifyViews();
									mapper.saveClaimData(claimId, "expenses", claimController.getExpenseList());
									setViews();
									Toast.makeText(getApplicationContext(), "Expense Un-flagged", Toast.LENGTH_SHORT).show();
								}	
							}
							else {
		            			   Toast.makeText(getApplicationContext(), 
		            					   		  currentClaim.getStatus() + " claims can not be edited.", 
		            					   		  Toast.LENGTH_SHORT).show();
		            		}
					    }
				  
			      });
				  AlertDialog alert = builder.create();
				  alert.show();		
			}
		});
	}
	
	// Know that the expense list will always be ArrayList<Expense> so
	// warning is supressed.
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		//refresh expense list
		claimController.setExpenses((ArrayList<Expense>)mapper.loadClaimData(claimId, "expenses"));
		expenseListAdapter = new ExpenseListAdapter(this, currentClaim.getExpenses());
		expenseListView.setAdapter(expenseListAdapter);
		setViews();
	}
	
	/**
	 * Need to set the views for expenses again after the expenses are saved. 
	 */
	private void setViews() {
		for (Expense expense : claimController.getExpenseList()) {
			expense.addView(this);
		}
		updateTotals();
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
	
	/**
	 * Function updates the totals displayed at the bottom of the activity.
	 */
	private void updateTotals(){
		TextView totalView = (TextView) findViewById(R.id.totals);
		HashMap<String,Double> totals = currentClaim.computeTotal();
		String totalString = "Total Currency Values:" + "\n";
		for (Entry<String, Double> entry : totals.entrySet()) {
		    String key = entry.getKey();
		    Double value = entry.getValue();
		    totalString = totalString + key + " = " + value + "\n"; 
		}
		totalView.setText(totalString);
		totalView.setMovementMethod(new ScrollingMovementMethod());
	}

}
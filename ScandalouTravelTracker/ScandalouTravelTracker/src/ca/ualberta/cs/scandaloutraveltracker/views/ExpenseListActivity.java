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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.ExpenseListAdapter;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ExpenseController;
import ca.ualberta.cs.scandaloutraveltracker.mappers.ClaimMapper;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;

/**
 *  This activity displays the list of expenses that is related to the
 *  current claim. From here you can edit expenses.
 * @author Team3ScandalouStopwatch
 *
 */
public class ExpenseListActivity extends Activity implements ViewInterface {
	private ListView expenseListView ;
	private ExpenseController expenseController;
	private ExpenseListAdapter expenseListAdapter;
	private int claimId;
	private ClaimController claimController;
	private ClaimMapper mapper;
	private AlertDialog alert;
	private boolean canEdit;
	
	/**
	 *	Called when the activity is created. 
	 *	Sets up listeners and the views for the list of expenses. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_list);
		
		// Set currentClaim to the claim that was selected via intent
		Intent intent = getIntent();
	    claimId = intent.getIntExtra(Constants.claimIdLabel, 0);
	    claimController = new ClaimController(new Claim(claimId));
	    canEdit = claimController.getCanEdit();
	    mapper = new ClaimMapper(this.getApplicationContext());


	    if (claimId != 0) {
	    	setViews();
	    }
	    
	    // Currently checking for expenseList being null (Until Mapper can map expenses)
	    if (claimController.getExpenseList() == null) {
	    	claimController.setExpenses(new ArrayList<Expense>());
	    }
		
		expenseListView = (ListView) findViewById(R.id.expenselistView);
		expenseListAdapter = new ExpenseListAdapter(this, claimController.getExpenseList());
		expenseListView.setAdapter(expenseListAdapter);
		
		//give user edit options when an expense is clicked
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
							  claimController.removeExpense(claimController.getExpense(position));
							  mapper.saveClaimData(claimId, "expenses", claimController.getExpenseList());
							  ClaimListController claimListController = new ClaimListController();
							  claimListController.removeClaim(claimId);
							  claimListController.addClaim(new Claim(claimId));
							  update();
							  setViews();  
						  }
						  else {
	            			  Toast.makeText(getApplicationContext(), 
	            					  		  claimController.getStatus() + " claims can not be edited.", 
	            					   		  Toast.LENGTH_SHORT).show();
	            		  }
					    }

				  })
				  .setNeutralButton("Flag/Unflag", new DialogInterface.OnClickListener(){
					  @Override
						public void onClick(DialogInterface dialog, int i) {
						  	expenseController = new ExpenseController(claimController.getExpense(position));
							if (canEdit) {
								//flag/unflag expense
								if (expenseController.getFlag() == false) {
									expenseController.setFlag(true);
									expenseController.notifyViews();
									mapper.saveClaimData(claimId, "expenses", claimController.getExpenseList());
									setViews();
									Toast.makeText(getApplicationContext(), "Expense Flagged", Toast.LENGTH_SHORT).show();
								}
								else {
									expenseController.setFlag(false);
									expenseController.notifyViews();
									mapper.saveClaimData(claimId, "expenses", claimController.getExpenseList());
									setViews();
									Toast.makeText(getApplicationContext(), "Expense Un-flagged", Toast.LENGTH_SHORT).show();
								}	
								update();
							}
							else {
		            			   Toast.makeText(getApplicationContext(), 
		            					   		  claimController.getStatus() + " claims can not be edited.", 
		            					   		  Toast.LENGTH_SHORT).show();
		            		}
					    }
				
			      });
				  alert = builder.create();
				  alert.show();		
			}
		});
	}
	
	/**
	 * refreshes the view of expenses
	 */
	// Know that the expense list will always be ArrayList<Expense> so
	// warning is supressed.
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		//refresh expense list
		if (claimId != 0) {
			claimController.setExpenses((ArrayList<Expense>)mapper.loadClaimData(claimId, "expenses"));
			expenseListAdapter = new ExpenseListAdapter(this, claimController.getExpenseList());
			expenseListView.setAdapter(expenseListAdapter);
			setViews();	
		}
	}
	
	/**
	 * 	Sets the action bar to include the option to add an expense in the 
	 * 	dropdown menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.expense_list, menu);
        
        MenuItem addAction = menu.findItem(R.id.action_add_expense);
		if (canEdit) {
			addAction.setVisible(true);
		} else {
			addAction.setVisible(false);
		}
        
        return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 	Executes options depending on which option the user picked from the action bar dropdown
	 * 	menu.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items.
	    switch (item.getItemId()) {
	    	case R.id.action_add_expense:
				Intent intent = new Intent(ExpenseListActivity.this, NewExpenseActivity.class);
				intent.putExtra(Constants.claimIdLabel, claimId);
				startActivity(intent);
	    		return true;
			default:
	        	return false;
	    }
	}
	
	/**
	 * Need to set the views for expenses again after the expenses are saved. 
	 */
	private void setViews() {
		for (Expense expense : claimController.getExpenseList()) {
			expenseController = new ExpenseController(expense);
			expenseController.addView(this);
		}
		updateTotals();
	}

	/**
	 * notifies the ListView of any data changes for the list of expenses
	 */
	@Override
	public void update() {
		expenseListAdapter = new ExpenseListAdapter(this, claimController.getExpenseList());
		expenseListView.setAdapter(expenseListAdapter);
		expenseListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Function updates the totals displayed at the bottom of the activity.
	 */
	private void updateTotals(){
		TextView totalView = (TextView) findViewById(R.id.totals);
		totalView.setText(claimController.getUpdatedTotalsString());
		totalView.setMovementMethod(new ScrollingMovementMethod());
	}
	
	// Testing Methods
	/**
	 * 	Used for testing
	 * @return the alert dialog that is used with an expense is clicked on
	 */
	public AlertDialog getAlert() {
		return alert;
	}
	
	/**
	 *	Used for testing
	 * @return the adapter for the expense list
	 */
	public ExpenseListAdapter getAdapter() {
		return expenseListAdapter;
	}

}
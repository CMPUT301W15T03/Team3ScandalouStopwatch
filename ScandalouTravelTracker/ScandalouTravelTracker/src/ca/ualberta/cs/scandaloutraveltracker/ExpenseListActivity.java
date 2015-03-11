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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ExpenseListActivity extends Activity implements ViewInterface {
	private Button addExpenseButton;
	private ListView expenseListView ;
	private ExpenseListAdapter expenseListAdapter;
	private Claim currentClaim;
	
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
				// TODO Auto-generated method stub
				
				
			}
		});
		

		
		// Set currentClaim to the claim that was selected via intent (currently fake claim)
		currentClaim = createTestClaim();
		addExpenses();
		
		expenseListView = (ListView) findViewById(R.id.expenselistView);
		expenseListAdapter = new ExpenseListAdapter(this, currentClaim.getExpenses());
		expenseListView.setAdapter(expenseListAdapter);
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

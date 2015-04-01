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

Icons on this adapter (filledflag.png, mapmarker.png, and pictureicon.png)
were used from http://icons4android.com/ under the Creative Commons 3.0
Unported license. None of these icons were modified from their original
state.

*/

package ca.ualberta.cs.scandaloutraveltracker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import ca.ualberta.cs.scandaloutraveltracker.models.Expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  An adapter that allows an expense to display its essential information
 *  in a ListView.
 * @author Team3ScandalouStopwatch
 *
 */
public class ExpenseListAdapter extends BaseAdapter {
	private ArrayList<Expense> expenses;
	private Context context;
	private boolean flagOn;
	
	public ExpenseListAdapter(Context context, ArrayList<Expense> expenses) {
		this.expenses = expenses;
		this.context = context;
		this.flagOn = false;
	}

	@Override
	public int getCount() {
		return expenses.size();
	}

	@Override
	public Object getItem(int position) {
		return expenses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_expense_display, parent, false);
		}
		
		// Make layout elements
		TextView expenseCategory = (TextView) convertView.findViewById(R.id.expenseCategoryExpenseListTV); 
		TextView expenseDate = (TextView) convertView.findViewById(R.id.expenseDateExpenseListTV);
		TextView expenseDescription = (TextView) convertView.findViewById(R.id.expenseDescriptionTV);
		TextView expenseTotal = (TextView) convertView.findViewById(R.id.expenseTotalsExpenseListTV);
		ImageView expenseLocation = (ImageView) convertView.findViewById(R.id.expenseLocationIcon);
		ImageView expenseReceipt = (ImageView) convertView.findViewById(R.id.expensePictureIcon);
		ImageView expenseFlag = (ImageView) convertView.findViewById(R.id.expenseFlagIcon);
		
		Expense currentExpense = expenses.get(position);
		
		// Setting the ImageView for flag
		// Setting the adapter flag visibility
		if (currentExpense.getFlag()) {
			flagOn = true;
		}
		else if (!currentExpense.getFlag()) {
			expenseFlag.setVisibility(View.INVISIBLE);
			flagOn = false;
		}
		
		// Setting the receipt indicator
		if (!currentExpense.getReceiptAttached()) {
			expenseReceipt.setVisibility(View.INVISIBLE);
		}
		
		// Setting the destination indicator
		if (!currentExpense.getLocationAttached()) {
			expenseLocation.setVisibility(View.INVISIBLE);
		}
		
		String currency=null;
		
		if (currentExpense.getCurrencyType().contentEquals("--Choose Currency--")){
			
			currency= "N/A";
		}
		// Formatting cost
		else{
		currency= currentExpense.getCurrencyType();
		}
		
		NumberFormat formatter = new DecimalFormat("#0.00");
		String cost = "Cost: " + formatter.format(currentExpense.getCost()) + " " +
				  currency;
		//setting category
		if (currentExpense.getCategory().contentEquals("--Choose Category--")){
			expenseCategory.setText("N/A");
		}
		else{
			expenseCategory.setText(currentExpense.getCategory());
		}
		// Set layout elements	
		
		expenseDate.setText(currentExpense.getDateString());
		expenseDescription.setText(currentExpense.getDescription());
		expenseTotal.setText(cost);

		return convertView;
	}

	public boolean getFlag() {
		return flagOn;
	}
}

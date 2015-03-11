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

/* ExpenseListAdapter.java Basic Info:
 *  An adapter that allows an expense to display its essential information
 *  in a ListView.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpenseListAdapter extends BaseAdapter {
	private ArrayList<Expense> expenses;
	private Context context;
	
	public ExpenseListAdapter(Context context, ArrayList<Expense> expenses) {
		this.expenses = expenses;
		this.context = context;
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
		ImageView expenseFlag = (ImageView) convertView.findViewById(R.id.expenseListFlag);
		
		// Set layout elements	
		expenseCategory.setText(expenses.get(position).getCategory());
		expenseDate.setText(expenses.get(position).getDate().toString());
		expenseDescription.setText(expenses.get(position).getDescription());
		// expenseTotal.setText(expenses.get(position).getDescription());

		return convertView;
	}

}

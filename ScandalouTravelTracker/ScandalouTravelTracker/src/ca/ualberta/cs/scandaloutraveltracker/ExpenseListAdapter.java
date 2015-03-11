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
		expenseCategory.setText(expenses.get(position).getDescription());
		expenseDate.setText(expenses.get(position).getDate().toString());
		expenseDescription.setText(expenses.get(position).getDescription());
		// expenseTotal.setText(expenses.get(position).getDescription());

		return convertView;
	}

}

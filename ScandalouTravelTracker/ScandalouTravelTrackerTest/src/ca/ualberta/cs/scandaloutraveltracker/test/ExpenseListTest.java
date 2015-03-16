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

package ca.ualberta.cs.scandaloutraveltracker.test;

import java.util.Date;

import ca.ualberta.cs.scandaloutraveltracker.ExpenseListActivity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.R;

public class ExpenseListTest extends
		ActivityInstrumentationTestCase2<ExpenseListActivity> {

	public ExpenseListTest() {
		super(ExpenseListActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	// Test UC 05.01.01
	public void testExpenseDisplayed() {
	    Date date = new Date(123);
	    String cat = "Category1";
	    String des = "Description";
	    double spent = 45.23;
	    String cur = "CAD";
	    boolean complete = false;
	    boolean reciept = false;

	    ExpenseListActivity activity = startWithExpense(date, cat, des, spent,
	                                                            cur, complete, reciept);
	    /*
	    View allViews = activity.getWindow().getDecorView();
	    TextView categoryView = (TextView) activity.findViewById(R.id.expenseCategoryExpenseListTV);
	    TextView descripView = (TextView) activity.findViewById(R.id.expenseDescriptionTV);
	    TextView dateView = (TextView) activity.findViewById(R.id.expenseDateExpenseListTV);
	    TextView spentView = (TextView) activity.findViewById(R.id.expenseTotalsExpenseListTV);
	    TextView currencyView = (TextView) activity.findViewById(R.id.expenseTotalsExpenseListTV);
	    //TextView completeView = (TextView) activity.findViewById(R.id.);
	    TextView recieptView = (TextView) activity.findViewById(R.id.expenseReceiptIndicator);

	    ViewAsserts.assertOnScreen(allViews, (View) categoryView);
	    ViewAsserts.assertOnScreen(allViews, (View) descripView);
	    ViewAsserts.assertOnScreen(allViews, (View) dateView);
	    ViewAsserts.assertOnScreen(allViews, (View) spentView);
	    ViewAsserts.assertOnScreen(allViews, (View) currencyView);
	    //ViewAsserts.assertOnScreen(allViews, (View) completeView);
	    ViewAsserts.assertOnScreen(allViews, (View) recieptView);
	    */
	}
	
	public ExpenseListActivity startWithExpense(Date date, String category, String description, Double spent,
								   String currency, boolean complete, boolean reciept)
	{
		//return (ExpenseListActivity)getActivity();
		return null;
		
	}
	
}

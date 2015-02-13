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

import android.test.ActivityInstrumentationTestCase2;

public class ExpenseListTest extends
		ActivityInstrumentationTestCase2<ExpenseList> {

	public void testExpenseDisplayed() {
	    Date date = new Date(123);
	    String cat = "Category1";
	    String des = "Description";
	    double spent = 45.23;
	    String cur = "CAD";
	    boolean complete = false;
	    boolean reciept = false;

	    ViewExpenseListActivity activity = startWithExpense(date, cat, des, spent
	                                                            cur, complete, reciept);
	    View allViews = activity.getWindow().getDecorView();
	    TextView categoryView = (TextView) activity.findViewById(R.id.categoryText);
	    TextView descripView = (TextView) activity.findViewById(R.id.descriptionText);
	    TextView dateView = (TextView) activity.findViewById(R.id.expenseDateText);
	    TextView spentView = (TextView) activity.findViewById(R.id.amountSpentText);
	    TextView currencyView = (TextView) activity.findViewById(R.id.currencyText);
	    TextView completeView = (TextView) activity.findViewById(R.id.completeText);
	    TextView recieptView = (TextView) activity.findViewById(R.id.recieptText);

	    ViewAsserts.assertOnScreen(allViews, (View) categoryView);
	    ViewAsserts.assertOnScreen(allViews, (View) descripView);
	    ViewAsserts.assertOnScreen(allViews, (View) dateView);
	    ViewAsserts.assertOnScreen(allViews, (View) spentView);
	    ViewAsserts.assertOnScreen(allViews, (View) currencyView);
	    ViewAsserts.assertOnScreen(allViews, (View) completeView);
	    ViewAsserts.assertOnScreen(allViews, (View) recieptView);
	}
	
}
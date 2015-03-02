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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EditExpenseActivity extends Activity {
	Spinner category_select;
	Spinner currency_select;
	private String currency;
	private String category;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		//category spinner setup
		category_select=(Spinner) findViewById(R.id.catspinner);
		ArrayAdapter<CharSequence> category_adapter= ArrayAdapter.createFromResource
				(this,R.array.Category, android.R.layout.simple_list_item_1);
		// Specifies layout to use when list choices appear
		category_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		category_select.setAdapter(category_adapter);
		
		// Selecting items in spinner, setting up listeners
		category_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id){
					if (pos >0){
						category= (String) parent.getItemAtPosition(pos);
					}
				}
			
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		//currency spinner setup
		currency_select=(Spinner) findViewById(R.id.currencyspinner);
		ArrayAdapter<CharSequence> currency_adapter = ArrayAdapter.createFromResource
				(this,R.array.Currency, android.R.layout.simple_list_item_1);
		// Specifies layout to use when list choices appear
		currency_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		currency_select.setAdapter(currency_adapter);
		
		// Selecting items in spinner, setting up listeners
		currency_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
					if (pos >0){
						currency = (String) parent.getItemAtPosition(pos);
					}
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
			});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_expense, menu);
		return true;
		
	}

	private void addExpense(){
		
	}
	
	private void editExpense(){
		
	}
}

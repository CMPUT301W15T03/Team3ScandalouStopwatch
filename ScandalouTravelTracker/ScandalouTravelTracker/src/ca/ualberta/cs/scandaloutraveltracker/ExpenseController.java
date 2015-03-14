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

/* ExpenseController.java Basic Info:
 *  When supplied with an expense the controller allows you to edit 
 *  expense information (expense should not be edited directly).
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.io.File;
import java.util.Date;

import android.view.View;

public class ExpenseController {
	private Expense currentExpense;
	
	public ExpenseController(Expense currentExpense) {
		this.currentExpense = currentExpense;
	}
	
	public void setDescription(String description) {
		currentExpense.setDescription(description);
	}
	
	public void setCategory(String category) {
		currentExpense.setCategory(category);
	}
	
	public void setCurrency(String currency) {
		currentExpense.setCurrencyType(currency);
	}
	
	public void setFlag(boolean flag) {
		currentExpense.setFlag(flag);
	}
	
	public void setCost(double cost) {
		currentExpense.setCost(cost);
	}
	
	public void setDate(Date date) {
		currentExpense.setDate(date);
	}
	
	public void addView(ViewInterface view) {
		currentExpense.addView(view);
	}
	
	public void removeView(ViewInterface view) {
		currentExpense.removeView(view);
	}
	
	public void setPhoto(File photo) {
		currentExpense.setPhoto(photo);
	}
	
}

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

package ca.ualberta.cs.scandaloutraveltracker.controllers;

import java.util.Date;

import android.location.Location;

import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.models.Receipt;
import ca.ualberta.cs.scandaloutraveltracker.views.ViewInterface;

/**
 *  When supplied with an expense the controller allows you to edit 
 *  expense information (expense should not be edited directly).
 * @author Team3ScandalouStopwatch
 *
 */
public class ExpenseController {
	private Expense currentExpense;
	
	/**
	 * ExpenseController needs the expense it is to edit as a paramter.
	 * @param currentExpense
	 */
	public ExpenseController(Expense currentExpense) {
		this.currentExpense = currentExpense;
	}
	
	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		currentExpense.setDescription(description);
	}
	
	/**
	 * 
	 * @param category
	 */
	public void setCategory(String category) {
		currentExpense.setCategory(category);
	}

	
	
	/**
	 * 
	 * @param currency
	 */
	public void setCurrency(String currency) {
		currentExpense.setCurrencyType(currency);
	}
	
	/**
	 * 
	 * @param flag
	 */
	public void setFlag(boolean flag) {
		currentExpense.setFlag(flag);
	}
	
	/**
	 * 
	 * @return True if the expense is flagged and false otherwise
	 */
	public boolean getFlag() {
		return currentExpense.getFlag();
	}
	
	/**
	 * 
	 * @param cost
	 */
	public void setCost(double cost) {
		currentExpense.setCost(cost);
	}
	
	/**
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		currentExpense.setDate(date);
	}
	
	/**
	 * 
	 * @return the location the expense occurred
	 */
	public Location getLocation() {
		return currentExpense.getLocation();
	}

	/**
	 * 
	 * @param location where the expense occurred
	 */
	public void setLocation(Location location) {
		currentExpense.setLocation(location);
	}
	
	/**
	 * 
	 * @param view
	 */
	public void addView(ViewInterface view) {
		currentExpense.addView(view);
	}
	
	/**
	 * 
	 * @param view
	 */
	public void removeView(ViewInterface view) {
		currentExpense.removeView(view);
	}


	public String getReceiptPath() {
		return currentExpense.getReceiptPath();
	}
	
	/**
	 * 
	 * @param receipt
	 */
	public void setReceiptPath(String receiptPath) {
		currentExpense.setReceiptPath(receiptPath);
	}	
	
	/**
	 * This method is specifically used for passing the newly created
	 * expense into the ClaimController so it can be added to the
	 * claim's expenses list.
	 * @return The current expense
	 */
	public Expense getExpense() {
		return currentExpense;
	}
	
	/**
	 *  Notifies the currentExpense views that it has been updated
	 */
	public void notifyViews() {
		currentExpense.notifyViews();
	}
	
	public boolean getReceiptStatus() {
		return currentExpense.getReceiptAttached();
	}
	
	public void setReceiptStatus(boolean status) {
		currentExpense.setReceiptAttached(status);
	}
	
}

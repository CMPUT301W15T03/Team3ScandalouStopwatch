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
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
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
	 * ExpenseController needs the expense it is to edit as a parameter.
	 * @param currentExpense
	 */
	public ExpenseController(Expense currentExpense) {
		this.currentExpense = currentExpense;
	}
	
	/**
	 * Set the description of the expense
	 * @param description
	 */
	public void setDescription(String description) {
		currentExpense.setDescription(description);
	}
	
	/**
	 * Set the category of the expense
	 * @param category
	 */
	public void setCategory(String category) {
		currentExpense.setCategory(category);
	}

	
	
	/**
	 * Set the currency of the expense
	 * @param currency
	 */
	public void setCurrency(String currency) {
		currentExpense.setCurrencyType(currency);
	}
	
	/**
	 * Set if the expense is flagged or not
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
	 * Set the cost of the current expense
	 * @param cost
	 */
	public void setCost(double cost) {
		currentExpense.setCost(cost);
	}
	
	/**
	 * Set the date the current expense occurred
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
	 * Add a view to the expense (listener)
	 * @param view
	 */
	public void addView(ViewInterface view) {
		currentExpense.addView(view);
	}
	
	/**
	 * Remove a view from the expense
	 * @param view
	 */
	public void removeView(ViewInterface view) {
		currentExpense.removeView(view);
	}


	/**
	 * Get the receipt path associated with the expense
	 * @return String that is a path to the receipt photo
	 */
	public String getReceiptPath() {
		return currentExpense.getReceiptPath();
	}
	
	/**
	 * Set the path to the expense's receipt
	 * @param receipt
	 */
	public void setReceiptPath(String receiptPath) {
		currentExpense.setReceiptPath(receiptPath);
	}
	
	/**
	 * 
	 * @return Receipt photo as an encoded string
	 */
	public String getReceiptPhoto() {
		return currentExpense.getReceiptPhoto();
	}
	
	/**
	 * Set the receipt photo as an encoded string
	 * @param receipt
	 */
	public void setReceiptPhoto(String receiptPhoto) {
		currentExpense.setReceiptPhoto(receiptPhoto);
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
	
	/**
	 * Returns the receipts status (if one is attached)
	 * @return boolean
	 */
	public boolean getReceiptStatus() {
		return currentExpense.getReceiptAttached();
	}
	
	/**
	 * Set that a receipt has been attached to the expense
	 * @param status
	 */
	public void setReceiptStatus(boolean status) {
		currentExpense.setReceiptAttached(status);
	}
	
}

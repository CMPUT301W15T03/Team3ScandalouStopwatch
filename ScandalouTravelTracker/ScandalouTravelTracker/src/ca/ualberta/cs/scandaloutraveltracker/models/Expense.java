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

package ca.ualberta.cs.scandaloutraveltracker.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.location.Location;
import android.os.Environment;

import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.views.ViewInterface;

/**
 * Class that contains the Expense model. Any changes done to an expense
 * should be done through the Expense Controller
 * @author Team3ScandalouStopwatch
 */
public class Expense extends SModel {
	
	private Date date;
	private String category;
	private String description;
	private Double cost;
	private String currencyType;
	private boolean flag;
	private boolean receiptAttached;
	private boolean locationAttached;
	private String receiptPath;
	private String receiptPhoto;
	private Location location;
	
	/**
	 * Constructor creates an empty expense for the user to populate.
	 */
	public Expense() {
		this.date = null;
		this.category = null;
		this.description = null;
		this.cost = null;
		this.currencyType = null;
		this.flag = false;
		this.receiptAttached = false;
		this.receiptPath = null;
		this.receiptPhoto = null;
		this.location = null;
		this.locationAttached = false;
	}

	/**
	 * 
	 * @return Date the expense occurred
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 
	 * @param date Date the expense occurred
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * 
	 * @return True if there is a receipt attached
	 */
	public boolean getReceiptAttached() {
		return receiptAttached;
	}
	
	/**
	 * Set the boolean to be true if there is a receipt
	 * attached and false if there is not.
	 * @param attached
	 */
	public void setReceiptAttached(boolean attached) {
		receiptAttached = attached;
	}
	
	/**
	 * 
	 * @return Category that expense belongs to
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 
	 * @param category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 
	 * @return Expenses' description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return Cost of the expense
	 */
	public Double getCost() {
		return cost;
	}

	/**
	 * 
	 * @param cost
	 */
	public void setCost(Double cost) {
		this.cost = cost;
	}

	/**
	 * 
	 * @return Currency of the expense
	 */
	public String getCurrencyType() {
		return currencyType;
	}

	/**
	 * 
	 * @param currencyType
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	/**
	 * 
	 * @return false if not flagged and true if flagged (for incompleteness)
	 */
	public boolean getFlag() {
		return flag;
	}

	/**
	 * 
	 * @param flag
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
		notifyViews();
	}
	
	/**
	 * 
	 * @return Path of the receipt photo
	 */
	public String getReceiptPath() {
		return receiptPath;
	}
	
	/**
	 * 
	 * @param cost
	 */
	public void setReceiptPath(String receiptPath){
		this.receiptPath = receiptPath;
	}
	
	/**
	 * 
	 * @return Photo of the receipt photo
	 */
	public String getReceiptPhoto() {
		return receiptPhoto;
	}
	
	/**
	 * 
	 * @param cost
	 */
	public void setReceiptPhoto(String receiptPhoto){
		this.receiptPhoto = receiptPhoto;
	}		
	
	/**
	 * 
	 * @return the location the expense occurred
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location where the expense occurred
	 */
	public void setLocation(Location location) {
		this.location = location;
		if (location != null) {
			this.locationAttached = true;
		} else { 
			this.locationAttached = false;
		}
	}
	
	/**
	 * Converts the date of the expense into a string
	 * with the format: MM/dd/yyyy.
	 * @return Date as a string
	 */
	public String getDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		return sdf.format(this.date);
	}

	/**
	 * Removes all the views that are associated with the expense.
	 * This is done before saving an expense as converting a list
	 * of views to JSON causes the app to crash.
	 */
	public void removeAllViews() {
		for (ViewInterface view : views) {
			views.remove(view);
		}
	}
	/**
	 * 
	 * @return the locationattached
	 */
	public boolean getLocationAttached() {
		return this.locationAttached;
	}
	/**
	 * 
	 * @param location where the expense occurred
	 */
	public void setLocationAttached(boolean attached) {
		this.locationAttached = attached;
	}
}

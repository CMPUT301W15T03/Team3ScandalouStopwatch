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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *  The Claim Controller class is used for making changes to a Claim that
 *  is supplied to the controller. You should not be making changes to a
 *  claim object directly but through this controller.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimController {
	private Claim currentClaim;
	
	/**
	 * Constructor that allows the ClaimController to edit the details of
	 * the claim that is passed to it.
	 * @param currentClaim
	 */
	public ClaimController(Claim currentClaim) {
		this.currentClaim = currentClaim;
	}
	
	/**
	 * Set an ArrayList of expenses for the claim.
	 * @param expenses
	 */
	public void setExpenses(ArrayList<Expense> expenses) {
		currentClaim.setExpenses(expenses);
	}

	/**
	 * Updates the expense (e) located at position i. 
	 * @param i
	 * @param e
	 */
	public void updateExpense(int i, Expense e) {
		ArrayList<Expense> newExpenseList = currentClaim.getExpenses();
		newExpenseList.set(i, e);
		currentClaim.setExpenses(newExpenseList);
	}
	
	/**
	 * 
	 * @return Name of the user associated with the claim.
	 */
	public String getName() {
		return currentClaim.getName();
	}
	
	/**
	 * Set name of user associated with claim. Should be set
	 * via the User's name.
	 * @param name
	 */
	public void setName(String name) {
		currentClaim.setName(name);
	}
	
	/**
	 * 
	 * @return True if the claim can be edited and false otherwise
	 */
	public boolean getCanEdit() {
		return currentClaim.getCanEdit();
	}
	
	/**
	 * 
	 * @param edit
	 */
	public void setCanEdit(boolean edit) {
		currentClaim.setCanEdit(edit);
	}
	
	/**
	 * 
	 * @return Claim's description
	 */
	public String getDescription(){
		return currentClaim.getDescription();
	}
	
	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		currentClaim.setDescription(description);
	}

	/**
	 * 
	 * @return Start date for claim
	 */
	public Date getStartDate(){
		return currentClaim.getStartDate();
	}
	/**
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		currentClaim.setStartDate(startDate);
	}

	/**
	 * 
	 * @return End date for claim
	 */
	public Date getEndDate(){
		return currentClaim.getEndDate();
	}
	
	/**
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		currentClaim.setEndDate(endDate);
	}

	/**
	 * 
	 * @return ArrayList of destinations associated with claim
	 */
	public ArrayList<Destination> getDestinations() {
		return currentClaim.getDestinations();
	}	
	
	/**
	 * 
	 * @return ArrayList of Strings that are tags associated with claim
	 */
	public ArrayList<String> getTags(){
		return currentClaim.getTags();
	}	
	
	/**
	 * 
	 * @param tag
	 */
	public void addTag(String tag) {
		currentClaim.getTags().add(tag);
	}
	
	/**
	 * 
	 * @param tag
	 */
	public void removeTag(String tag) {
		currentClaim.getTags().remove(tag);
	}
	
	/**
	 * 
	 * @param destination
	 */
	public void addDestination(Destination destination) {
		currentClaim.addDestination(destination);
	}
	
	/**
	 * 
	 * @param destination
	 */
	public void removeDestination(Destination destination) {
		currentClaim.removeDestination(destination);
	}
	
	/**
	 * 
	 * @return Status of the claim
	 */
	public String getStatus() {
		return currentClaim.getStatus();
	}
	
	/**
	 * Should use constants located in the Constants class to
	 * set status'.
	 * @param status
	 */
	public void setStatus(String status) {
		currentClaim.setStatus(status);
	}
	
	/**
	 * 
	 * @param expense
	 */
	public void addExpense(Expense expense) {
		currentClaim.addExpense(expense);
	}
	
	/**
	 * 
	 * @param expense
	 */
	public void removeExpense(Expense expense) {	
						
		currentClaim.deleteExpense(expense);
	}

	/**
	 * 
	 * @return ArrayList of expenses associated with claim
	 */
	public ArrayList<Expense> getExpenseList() {
		return currentClaim.getExpenses();
	}
	
	/**
	 * 
	 * @param position
	 * @return Get expense located at position
	 */
	public Expense getExpense(int position){
		return currentClaim.getExpense(position);
	}
	
	/**
	 * 
	 * @param view
	 */
	public void addView(ViewInterface view) {
		currentClaim.addView(view);
	}
	
	/**
	 * Updates the claim with the paramters passed.
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param destinations
	 * @param canEdit
	 * @throws UserInputException 
	 */
	public void updateClaim(Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, boolean canEdit) throws UserInputException{
		
		currentClaim.updateClaim(startDate, endDate, description, destinations, canEdit);
	}
	
	/**
	 * Submits the claim and edits the details of the claim to 
	 * reflect a submitted claim.
	 * @param status
	 * @param canEdit
	 */
	public void submitClaim(String status, boolean canEdit){
		currentClaim.submitClaim(status, canEdit);
	}
	
	/**
	 * Approves the claim and edits the details of the claim to 
	 * reflect an approved claim.
	 * @param status
	 * @param canEdit
	 * @param approverName
	 */
	public void approveClaim(String status, boolean canEdit, String approverName){
		currentClaim.approveClaim(status, canEdit, approverName);
	}
	
	/**
	 * Returns the claim and edits the details of the claim to 
	 * reflect a returned claim.
	 * @param status
	 * @param canEdit
	 * @param approverName
	 */
	public void returnClaim(String status, boolean canEdit, String approverName){
		currentClaim.returnClaim(status, canEdit, approverName);
	}	
	
	public HashMap<String, Double> computeTotal() {
		return currentClaim.computeTotal();
	}
	
	/**
	 * 
	 * @param tags
	 */
	public void updateTags(ArrayList<String> tags){
		currentClaim.updateTags(tags);	
	}
}

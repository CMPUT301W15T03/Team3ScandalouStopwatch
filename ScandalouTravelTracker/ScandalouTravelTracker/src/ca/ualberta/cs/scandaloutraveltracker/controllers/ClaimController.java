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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Destination;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.views.ViewInterface;

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
	 * 
	 * @return User Id for user that created the claim
	 */
	public int getUserId() {
		return currentClaim.getUser().getId();
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
	public void addExpense(Expense expense) throws UserInputException {
		
		// Get the updated list
		ArrayList<Expense> newExpenseList = currentClaim.getExpenses();
		newExpenseList.add(expense);
		
		// Save the new list in storage
		currentClaim.updateExpenses(newExpenseList);
		
		// Save the new list in the current claim
		currentClaim.setExpenses(newExpenseList);		
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
	 * Updates the expense (e) located at position i. 
	 * @param i
	 * @param e
	 */
	public void updateExpense(int expensePos, Expense newExpense) throws UserInputException {
		
		// Get the updated list
		ArrayList<Expense> newExpenseList = currentClaim.getExpenses();
		newExpenseList.set(expensePos, newExpense);
		
		// Save the new list in storage
		currentClaim.updateExpenses(newExpenseList);
		
		// Save the new list in the current claim
		currentClaim.setExpenses(newExpenseList);
	}	
	
	/**
	 * Submits the claim and edits the details of the claim to 
	 * reflect a submitted claim.
	 */
	public void submitClaim(){
		currentClaim.submitClaim();
	}
	
	/**
	 * Approves the claim and edits the details of the claim to 
	 * reflect an approved claim.
	 * @param approverName
	 * @param comment
	 * @param claimId
	 */
	public void approveClaim(String approverName, String comment, int claimId){
		currentClaim.approveClaim(approverName, comment, claimId);
	}
	
	/**
	 * Returns the claim and edits the details of the claim to 
	 * reflect a returned claim.
	 * @param approverName
	 * @param comment 
	 * @param claimId
	 */
	public void returnClaim(String approverName, String comment, int claimId){
		currentClaim.returnClaim(approverName, comment, claimId);
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
	
	/**
	 * Checks if the Claim has enough information to be sent to
	 * an approver.
	 * @return boolean if claim can be sent
	 */
	public boolean canClaimBeSent() {
		return currentClaim.canClaimBeSent();
	}
	
	/**
	 * Checks if the Claim has any incomplete/flagged Expenses
	 * @return boolean if an incomplete/flagged Expense exists
	 */
	public boolean checkIncompleteExpenses() {
		return currentClaim.checkIncompleteExpenses();
	}
	
	/**
	 * Gets the claim's tags and produces a string to display
	 * the tags.
	 * @return string of tags
	 */
	public String getTagsString(){
		return currentClaim.getTagsString();
	}

	/**
	 * Parses the tagString to give an ArrayList of tags
	 * @param tagsString String of tags separated by commas
	 * @return ArrayList with just the tag names
	 */
	public ArrayList<String> getTagsList(String tagsString){
		return currentClaim.getTagsList(tagsString);
	}
	
	/**
	 * Calculates a totals string to be displayed
	 */
	public String getUpdatedTotalsString(){
		return currentClaim.getUpdatedTotalsString();
	}
	
	/**
	 * Sets the views for all the destinations after they have been removed. 
	 */
	public void setDestinationViews(ViewInterface currentView) {
		
		currentClaim.setDestinationViews(currentView);
	}
	
	/**
	 * 
	 * @return String that contains the Approver(s) comments. 
	 */
	public String getApproverCommentsString() {
		return currentClaim.getApproverCommentsString();
	}
}

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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import ca.ualberta.cs.scandaloutraveltracaker.mappers.ClaimMapper;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.views.ViewInterface;

import android.widget.Toast;

/**
 *  Class that contains the Claim model.
 *   Edits to any instance of a Claim should be done through the ClaimController class.
 * @author Team3ScandalouStopwatch
 */
public class Claim extends SModel implements Comparable<Claim> {
	
	private int id;
	
	private String description;
	private Date startDate;
	private Date endDate; 
	private ArrayList<Expense> expenses;
	private ArrayList<Destination> destinations;
	private ArrayList<String> tags; 
	private Boolean canEdit;
	private String status;
	private String approverName;
	private ArrayList<String> approverComments;
	private User user;

	/**
	 * Fetches a claim corresponding to the id passed to it. 
	 * Uses the ClaimMapper class to retrieve the saved claim. 
	 *
	 * @param id    The id of the claim. This is created whenever a
	 *              new Claim is created and is how the ClaimMapper
	 *              class knows which claim is which when saved.
	 * @see ClaimMapper#loadClaimData(int, String)
	 */
	@SuppressWarnings("unchecked")
	public Claim(int id){
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());

		this.id = (Integer)mapper.loadClaimData(id, "id");
		this.description = (String)mapper.loadClaimData(id,  "description");
		this.startDate = (Date)mapper.loadClaimData(id, "startDate");
		this.endDate = (Date)mapper.loadClaimData(id, "endDate");
		this.destinations = (ArrayList<Destination>)mapper.loadClaimData(id, "destinations");
		this.tags = (ArrayList<String>)mapper.loadClaimData(id, "tags");
		this.status = (String)mapper.loadClaimData(id, "status");
		this.approverName = (String)mapper.loadClaimData(id, "approverName");
		this.approverComments = (ArrayList<String>)mapper.loadClaimData(id, "approverComments");
		this.expenses = (ArrayList<Expense>)mapper.loadClaimData(id, "expenses");
		this.canEdit = (Boolean)mapper.loadClaimData(id, "canEdit");
		int userId = (Integer)mapper.loadClaimData(id, "userId");
		this.user = new User(userId);
	}
	
	/**
	 * Constructor that is used to create empty claims to fill
	 * for the NewClaimActivity
	 */
	public Claim() {
		this.canEdit = true;
		this.expenses = new ArrayList<Expense>();
		this.destinations = new ArrayList<Destination>();
		this.tags = new ArrayList<String>();
		this.approverComments = new ArrayList<String>();
		this.status = "In Progress";
	}
	
	/**
	 * Gets the user for the current claim
	 */
	
	public User getUser() {
		return this.user;
	}
	/**
	 * Sets the user for the current claim
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * @return Claim id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id Claim's ID for the current claim
	 */
	public void setId(int id) {
		this.id = id;
	}	
	
	/**
	 * 
	 * @return Claim's description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @param description Claim's description for the current claim
	 * if the claim status is in progress or returned
	 */
	public void setDescription(String description) {
		if (canEdit) {
			this.description = description;
		}
	}

	/**
	 * 
	 * @return Start date for the claim
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * 
	 * @param startDate Claim's start date for the current claim 
	 * if the claim status is in progress or returned
	 */
	public void setStartDate(Date startDate) {
		if (canEdit) {
			this.startDate = startDate;
		}
	}
	
	/**
	 * 
	 * @return Start date for the claim as a string in sdf format
	 */
	public String getStartDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
		return sdf.format(this.startDate);
	}
	
	/**
	 * 
	 * @return End date for the claim
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * 
	 * @param endDate Claim's end date for the current claim 
	 * if the claim status is in progress or returned
	 */
	public void setEndDate(Date endDate) {
		if (canEdit) {
			this.endDate = endDate;
		}
	}
	
	/**
	 * 
	 * @return End date for the claim as a string in sdf format
	 */
	public String getEndDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
		return sdf.format(this.endDate);
	}

	/**
	 * 
	 * @return ArrayList of destinations associated with the claim
	 */
	public ArrayList<Destination> getDestinations() {
		return destinations;
	}
	
	/**
	 * 
	 * @param destinations Claim's destinations for the current claim 
	 */
	public void setDestinations(ArrayList<Destination> destinations) {
		this.destinations = destinations;
	}	
	
	/**
	 * 
	 * @return ArrayList of expenses associated with the claim
	 */
	public ArrayList<Expense> getExpenses() {
		return expenses;
	}
	
	/**
	 * 
	 * @param expenses Claim's expenses for the current claim 
	 */
	public void setExpenses(ArrayList<Expense> expenses) {
		this.expenses = expenses;
	}
	
	/**
	 * 
	 * @return ArrayList of strings called tags that are associated
	 * 		   with the claim.
	 */
	public ArrayList<String> getTags() {
		return tags;
	}
	
	/**
	 * 
	 * @param tags Claim's tags for the current claim 
	 */
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * 
	 * @return Claim's status
	 */
	public String getStatus(){
		return status;
	}
	
	/**
	 * 
	 * @param status Claim's status for the current claim 
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return Name of the user that approved the claim
	 */
	public String getApproverName(){
		return approverName;
	}
	
	/**
	 * 
	 * @param approverName Name of approver who approved Claim for the current claim 
	 */
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	/**
	 * 
	 * @return Comment that the approver left on the claim
	 */
	public ArrayList<String> getApproverComments(){
		return approverComments;
	}
	
	/**
	 * 
	 * @param approverComments List of every comment made by the approver
	 */
	public void setApproverComment(ArrayList<String> approverComments) {
		this.approverComments = approverComments;
	}	
	
	/**
	 * 
	 * @param approverComment Comment approver left
	 */
	public void addApproverComment(String approverComment) {
		this.approverComments.add(approverComment);
	}	
	
	/**
	 * 
	 * @param approverComment Comment approver left
	 */
	public void removeApproverComment(String approverComment) {
		this.approverComments.remove(approverComment);
	}
	
	/**
	 * 
	 * @param canEdit Boolean for if claim can be edited
	 */
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	/**
	 * 
	 * @return A boolean to see if the claim can be edited.
	 */
	public boolean getCanEdit() {
		return this.canEdit;
	}
	
	/**
	 * Returns an expense in the list of expenses associated
	 * with the position passed.
	 * @param position Position associated with claim
	 * @return Expense located in position
	 */
	public Expense getExpense(int position) {
		return expenses.get(position);
	}
	
	// Modification methods
	
	/**
	 * The updateClaim method takes any of the fields that can
	 * be changed by a claimant and updates the claim by using
	 * the claim's id along with a claimMapper.
	 * 
	 * @param startDate Claim's start date
	 * @param endDate Claim's end date
	 * @param description Claim's description
	 * @param destinations Claim's destinations
	 * @param canEdit If the claim can be edited
	 * @see ClaimMapper#updateClaim(int, Date, Date, String, ArrayList, boolean)
	 */
	public void updateClaim(Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, boolean canEdit) throws UserInputException {
		
		setStartDate(startDate);
		setEndDate(endDate);
		setDescription(description);
		setDestinations(destinations);
		setCanEdit(canEdit);
		
		if (endDate.before(startDate)){
			throw new UserInputException("The end date can't be before the start date.");
		} else {
			ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
			mapper.updateClaim(this.id, startDate, endDate, description, destinations, canEdit);
		}
		
		// You should still see the changes you made, even if some were illegal
		notifyViews();
	}
	
	public void updateExpenses(ArrayList<Expense> expenses) throws UserInputException {
		
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.updateExpenses(this.id, expenses);

	}
	
	/**
	 * Updates the list of tags associated with the claim. 
	 * Uses the ClaimMapper to save the updated tag values
	 * and notifies the views associated with the Claim to
	 * update.
	 * @param tags Claim's tags
	 * @see ClaimMapper#updateTags(int, ArrayList)
	 */
	public void updateTags(ArrayList<String> tags){
		
		setTags(tags);
		
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.updateTags(this.id, tags);
		
		notifyViews();
	}
	
	/**
	 * Updates the Claim to the properties that are
	 * associated with a submitted claim. This is, setting
	 * the status to submitted and canEdit boolean to false.
	 * Uses the ClaimMapper to save updated data.
	 */
	public void submitClaim(){
		
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.changeClaimStatus(this.id, Constants.statusSubmitted, false);	
		
		notifyViews();
	}
	/**
	 * Approves the Claim. This is, setting
	 * the status to approved and canEdit boolean to false or .
	 * Uses the ClaimMapper to save updated data.
	 */
	public void approveClaim(String approverName, String comment, int claimId){
		
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.changeClaimStatus(this.id, Constants.statusApproved, false);	
		mapper.changeApproverName(this.id, approverName);
		this.approverComments.add(comment);
		mapper.updateComments(claimId, this.approverComments);
		notifyViews();
	}
	/**
	 * Approves the Claim. This is, setting
	 * the status to returned and canEdit boolean to true or .
	 * Uses the ClaimMapper to save updated data.
	 */
	public void returnClaim(String approverName, String comment, int claimId){
		
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.changeClaimStatus(this.id, Constants.statusReturned, true);	
		mapper.changeApproverName(this.id, approverName);
		this.approverComments.add(comment);
		mapper.updateComments(claimId, this.approverComments);
		notifyViews();
	}
	
	
	/**
	 * 
	 * @param destination Claim's destination if claim status is returned or in progress
	 */
	public void addDestination(Destination destination) {
		if (canEdit) {
			this.destinations.add(destination);
		}
	}

	/**
	 * 
	 * @param destination Claim's destination if claim status is returned or in progress
	 */
	public void removeDestination(Destination destination) {
		if (canEdit) {
			this.destinations.remove(destination);
		}
	}
	
	/**
	 * 
	 * @param expense Claim's expense if claim status is returned or in progress
	 */
	public void addExpense(Expense expense) {
		if (canEdit) {
			this.expenses.add(expense);			
		}
	}
	
	/**
	 * 
	 * @param expense Claim's expense if claim status is returned or in progress
	 */
	public void deleteExpense(Expense expense) {
		if (canEdit) {
			this.expenses.remove(expense);
			notifyViews();
		}
	}
	
	// String Conversion Methods
	
	/**
	 * Uses a HashMap to calculate the total cost of the claim. 
	 * The string value is associated with the currency and the
	 * double is associated with the cost.
	 *  If the currency types match,
	 *  they are added together,
	 *  else if they dont match a new group is opened fo that currency type
	 *  else if no currency selected, the amount is added to other. 
	 * @return HashMap with totaled costs from expenses
	 */
	public HashMap<String, Double> computeTotal() {
		
		HashMap<String, Double> totals = new HashMap<String, Double>();
		double old_total = 0;	
		
		// Sort and categorize all expenses
		for (Expense expense : expenses) {
			if (expense.getCurrencyType() != null) {
				
				if (totals.containsKey(expense.getCurrencyType())){
					old_total = totals.get(expense.getCurrencyType());
				} else {
					old_total = 0;
				}
				if (expense.getCurrencyType().contentEquals("--Choose Currency--")){
					totals.put("Other", old_total + expense.getCost());
				}
				else{
				totals.put(expense.getCurrencyType(), old_total + expense.getCost());				
				}
			}
		}
		
		return totals;
	}

	/**
	 * Converts all the destinations associated with a claim
	 * into a string. The format of this string is:
	 * destination1, destination2, ..., destination x
	 * @return A string with the Claim's destination names
	 */
	public String destinationsToString() {
		String destinations = "";
		int count = 1;
		
		if (this.getDestinations().size() > 0) {
			for (Destination dest : this.getDestinations()) {
				if (count == this.getDestinations().size()) {
					destinations += dest.getName();
				}
				else {
					destinations += dest.getName() + ", ";
				}
				count++;
			}
		}
		
		return destinations;
	}

	@Override
	public int compareTo(Claim another) {
		return startDate.compareTo(another.getStartDate());
	}

	/**
	 * Takes all the tags associated with the claim and
	 * prints them out as a string in the format:
	 * tag1, tag2, tag3, ..., tag x
	 * @return String that displays tags
	 */
	public String tagsToString() {
		String tags = "";
		int counter = 1;
		
		if (this.tags.size() > 0) {
			for (String tag : this.tags) {
				if (counter == this.tags.size()) {
					tags += tag;
				}
				else {
					tags += tag + " ";
				}
				counter++;
			}
		}
		
		return tags;
	}
	
	/**
	 * returns a string of all comments made to the claim with the newest appearing first
	 * @return String that displays comments with newest appearing first
	 */
	public String getApproverCommentsString() {
		String commentString = "";
		int i = 0;
		for (String temp : this.approverComments) {
			if (i == 0) {
				commentString = commentString + "- " + temp;
			}
			else {
				commentString = "- " + temp + "\n" + commentString;
			}
			i++;
		}
		if (this.approverComments.size() != 0) {
			commentString = "Approver Name: " + this.approverName + "\n" + commentString;
		}
		return commentString;
	}
	
	/**
	 * Checks if the Claim has enough information to be sent to
	 * an approver.
	 * @return boolean if claim can be sent
	 */
	public boolean canClaimBeSent() {
		if (this.getDestinations().size() < 1) {
			Toast.makeText(ClaimApplication.getContext(), "Please add a destination before submitting",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (this.getExpenses().size() < 1) {
			Toast.makeText(ClaimApplication.getContext(), "Please add an expense before submitting",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (this.getDescription() == null) {
			return false;
		} 
		
		return true;
	}
	
	/**
	 * Checks if the Claim has any incomplete/flagged Expenses
	 * @return boolean if an incomplete/flagged Expense exists
	 */
	public boolean checkIncompleteExpenses() {
		ArrayList<Expense> expenses = this.getExpenses();
		for (int i = 0; i < expenses.size(); i++) {
			Expense expense = expenses.get(i);
			if (expense.getFlag() == true) {
				return true;
			} else if (expense.getCategory().equals("--Choose Category--")) {
				return true;
			} else if (expense.getCost() == 0) {
				return true;
			} else if (expense.getCurrencyType().equals("--Choose Currency--")) {
				return true;
			} else if (expense.getDescription().equals("")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Given the list of tags this changes it into a string
	 * @param tagsList
	 * @return string of tags
	 */
	public String getTagsString(){
		String tags = "";
		
		for (int i = 0; i < this.tags.size(); i++){
			if (i != this.tags.size() - 1){
				tags += this.tags.get(i) + " ";
			} else {
				tags += this.tags.get(i);
			}
		}
		
		return tags;
	}

	/**
	 * Parses the tagString to give an ArrayList of tags
	 * @param tagsString String of tags separated by commas
	 * @return ArrayList with just the tag names
	 */
	public ArrayList<String> getTagsList(String tagsString){
		
		String[] temp = tagsString.split(", ");
		// CITATION http://stackoverflow.com/questions/10530353/convert-string-array-to-arraylist
		// 2015-03-13
		// Matten's answer
		ArrayList<String> tags = new ArrayList<String>(Arrays.asList(temp));
		
		return tags;
	}
	
	/**
	 * Calculates a totals string to be displayed
	 */
	public String getUpdatedTotalsString(){
		HashMap<String,Double> totals = computeTotal();
		String totalString = "Total Currency Values:" + "\n";
		for (Entry<String, Double> entry : totals.entrySet()) {
		    String key = entry.getKey();
		    Double value = entry.getValue();
		    totalString = totalString + key + " = " + String.format("%.2f", 
		    		Double.valueOf(value)) + "\n"; 
		}
		
		return totalString;
	}
	
	/**
	 * Sets the views for all the destinations after they have been removed. 
	 */
	public void setDestinationViews(ViewInterface currentView) {
		for (Destination dest : this.getDestinations()) {
			dest.addView(currentView);
		}
	}
}

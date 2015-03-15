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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 *  Class that contains the Claim model. Edits to any instance of a Claim
 *  should be done through the ClaimController class.
 * @author Team3ScandalouStopwatch
 */
public class Claim extends SModel implements Comparable<Claim> {
	
	private int id;
	
	private String name;
	private String description;
	private Date startDate;
	private Date endDate; 
	private ArrayList<Expense> expenses;
	private ArrayList<Destination> destinations;
	private ArrayList<String> tags; 
	private Boolean canEdit;
	private String status;
	private String approverName;
	private String approverComment;
	// private User user;

	/**
	 * Fetches a claim corresponding to the id passed to it. Uses
	 * the ClaimMapper class to retrieve the saved claim. 
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
		this.name = (String)mapper.loadClaimData(id, "name");
		this.description = (String)mapper.loadClaimData(id,  "description");
		this.startDate = (Date)mapper.loadClaimData(id, "startDate");
		this.endDate = (Date)mapper.loadClaimData(id, "endDate");
		this.destinations = (ArrayList<Destination>)mapper.loadClaimData(id, "destinations");
		this.tags = (ArrayList<String>)mapper.loadClaimData(id, "tags");
		this.status = (String)mapper.loadClaimData(id, "status");
		this.approverName = (String)mapper.loadClaimData(id, "approverName");
		this.approverComment = (String)mapper.loadClaimData(id, "approverComment");
		this.expenses = (ArrayList<Expense>)mapper.loadClaimData(id, "expenses");
		this.canEdit = (Boolean)mapper.loadClaimData(id, "canEdit");
	}
	
	/**
	 * Constructor that is used to create claims quickly for
	 * testing the claims.
	 *
	 */
	public Claim() {
		this.canEdit = true;
		this.expenses = new ArrayList<Expense>();
		this.destinations = new ArrayList<Destination>();
		this.tags = new ArrayList<String>();
		this.status = "In Progress";
	}
	
	// Constructor to quickly make a claim
	public Claim(String name, Date sDate, Date eDate) {
		this.name = name;
		this.startDate = sDate;
		this.endDate = eDate;
		this.canEdit = true;
	}
	
	// Getter and Setter Methods
	
	/**
	 * @return Claim id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}	
	
	/**
	 * 
	 * @return Claimant's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Currently incomplete. Should set the name to the name
	 * associated with user and not have a setName method
	 * within the Claim class.
	 * @param name Claimant's name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * 
	 * @return Start date for the claim as a string
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
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * 
	 * @return End date for the claim as a string
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
	 * @param destinations
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
	 * @param expenses
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
	 * @param tags
	 */
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStatus(){
		return status;
	}
	
	/**
	 * 
	 * @param status
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
	 * @param approverName
	 */
	public void setApproverName(String approverName) {
	}

	/**
	 * 
	 * @return Comment that the approver left on the claim
	 */
	public String getApproverComment(){
		return approverComment;
	}
	
	/**
	 * 
	 * @param approverComment
	 */
	public void setApproverComment(String approverComment) {
		this.approverComment = approverComment;
	}	
	
	/**
	 * 
	 * @param canEdit
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
	 * @param position
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
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param destinations
	 * @param canEdit
	 * @see ClaimMapper#updateClaim(int, Date, Date, String, ArrayList, boolean)
	 */
	public void updateClaim(Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, boolean canEdit){
		
		setStartDate(startDate);
		setEndDate(endDate);
		setDescription(description);
		setDestinations(destinations);
		setCanEdit(canEdit);
		setExpenses(expenses);
		
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.updateClaim(this.id, startDate, endDate, description, destinations, canEdit);
		
		notifyViews();
	}
	
	/**
	 * Updates the list of tags associated with the claim. 
	 * Uses the ClaimMapper to save the updated tag values
	 * and notifies the views associated with the Claim to
	 * update.
	 * @param tags
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
	 * @param status
	 * @param canEdit
	 * @see ClaimMapper#submitClaim(int, String, boolean)
	 */
	public void submitClaim(String status, boolean canEdit){
		
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.submitClaim(this.id, Constants.statusSubmitted, false);	
		
		notifyViews();
	}
	
	/**
	 * 
	 * @param destination
	 */
	public void addDestination(Destination destination) {
		this.destinations.add(destination);
	}

	/**
	 * 
	 * @param destination
	 */
	public void removeDestination(Destination destination) {
		this.destinations.remove(destination);
	}
	
	/**
	 * 
	 * @param expense
	 */
	public void addExpense(Expense expense) {
		this.expenses.add(expense);
	}
	
	/**
	 * 
	 * @param expense
	 */
	public void deleteExpense(Expense expense) {
		this.expenses.remove(expense);
		notifyViews();
	}
	
	// String Conversion Methods
	
	/**
	 * Uses a HashMap to calculate the total cost of the claim. 
	 * The string value is associated with the currency and the
	 * double is associated with the cost.
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
				totals.put(expense.getCurrencyType(), old_total + expense.getCost());				
				
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
	
	// Methods for the Approver Only
	
	/**
	 * Sets the claim as approved and sets the claim's approver
	 * name. 
	 * Currently incomplete. Should get the approverName from
	 * user. Perhaps pass in the Approver rather than String.
	 * @param approverName
	 */
	public void approveClaim(String approverName){
		this.status = "Approved";
		this.approverName = approverName;
		this.canEdit = false;
	}
	
	/**
	 * Sets the claim as returned and sets the claim's approver
	 * name.
	 * Currently incomplete. Should get the approverName from
	 * user. Perhaps pass in the Approver rather than String.
	 * @param approverName
	 */
	public void returnClaim(String approverName){
		this.status = "Returned";
		this.approverName = approverName;
		this.canEdit = true; 
	}

	/**
	 * 
	 * @param another A second claim to be compared to
	 * @return 0 if dates equal, >0 if date is after another, <0 if date is before another
	 */
	@Override
	public int compareTo(Claim another) {
		return another.getStartDate().compareTo(startDate);
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
					tags += tag + ", ";
				}
				counter++;
			}
		}
		
		return tags;
	}

}

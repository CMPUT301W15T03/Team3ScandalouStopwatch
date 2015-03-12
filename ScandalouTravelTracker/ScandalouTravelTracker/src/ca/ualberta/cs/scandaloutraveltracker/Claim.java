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

/* Claim.java Basic Info:
 *  Class that contains the Claim model. Edits to any instance of a Claim
 *  should be done through the ClaimController class.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;

public class Claim extends SModel implements Comparable<Claim> {
	
	private int id;
	
	private Context context;
	
	private String name;
	private String description;
	private Date startDate;
	private Date endDate; 
	private ArrayList<Expense> expenses;
	private ArrayList<Destination> destinations;
	private ArrayList<String> tags; 
	private HashMap<String, Double> totals;
	private Boolean canEdit;
	private String status;
	private String approverName;
	private String approverComment;

	public Claim(int id){
		ClaimMapper mapper = new ClaimMapper(context.getApplicationContext());
		
		this.name = (String)mapper.loadClaimData(id, "name");
		this.description = (String)mapper.loadClaimData(id,  "description");
		this.startDate = (Date)mapper.loadClaimData(id, "startDate");
		this.endDate = (Date)mapper.loadClaimData(id, "endDate");
		this.destinations = (ArrayList)mapper.loadClaimData(id, "destinations");
		this.canEdit = true;
	}
	
	// Constructor to make a more complete claim (feel free to add parameters as needed)
	public Claim(String name, String description, Date sDate, Date eDate, ArrayList<Destination> destinations) {
		this.name = name;
		this.description = description;
		this.startDate = sDate;
		this.endDate = eDate;
		this.destinations = destinations;
		this.canEdit = true;
		this.expenses = new ArrayList<Expense>();
	}	
	
	// Constructor to quickly make a claim
	public Claim(String name, Date sDate, Date eDate) {
		this.name = name;
		this.startDate = sDate;
		this.endDate = eDate;
		this.canEdit = true;
	}
	
	public Claim() {
		// Constructor for empty claim to test
		this.canEdit = true;
		this.expenses = new ArrayList<Expense>();
		this.destinations = new ArrayList<Destination>();
		this.tags = new ArrayList<String>();
		this.status = "In Progress";
	}
	
	// Getter and Setter Methods
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public String getStartDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
		return sdf.format(this.startDate);
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public String getEndDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
		return sdf.format(this.endDate);
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public ArrayList<Destination> getDestinations() {
		return destinations;
	}
	public void setDestinations(ArrayList<String> destinations2) {
		// TODO Auto-generated method stub
	}	
	
	public ArrayList<Expense> getExpenses() {
		return expenses;
	}
	public void setExpenses(ArrayList<Expense> expenses) {
		this.expenses = expenses;
	}
	
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public HashMap<String, Double> getTotals() {
		return totals;
	}
	public void setTotals(HashMap<String, Double> totals) {
		this.totals = totals;
	}
	
	public String getStatus(){
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getApproverName(){
		return approverName;
	}
	public void setApproverName(String approverName) {
	}

	public String getApproverComment(){
		return approverComment;
	}
	public void setApproverComment(String approverComment) {
	}	
	
	
	// Other Methods
	
	public String toString() {
		// TODO: Converting the claim with computeTotal to display on listView
		
		return null;
	}
	
	public String computeTotal() {
		// TODO: Using the HashMap to help compute the total for each type of currency
		
		return null;
	}

	public void setCanEdit(boolean b) {
		this.canEdit = b;
	}
	
	public boolean getCanEdit() {
		return this.canEdit;
	}
	
	public void addDestination(Destination destination) {
		this.destinations.add(destination);
	}

	public void removeDestination(Destination destination) {
		this.destinations.remove(destination);
	}
	
	public void addExpense(Expense expense) {
		this.expenses.add(expense);
	}
	
	public void removeExpense(Expense expense) {
		this.expenses.remove(expense);
	}
	
	public String getWarning(){
		return null;
	}
	
	public void raiseWarning(String warning){
		// TODO: Do something
	}
	
	// Converts the destinations into a string
	// String format is destination1, destination2, ..., destination x 
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
	
	
	// Approval Methods
	
	public void approveClaim(String approverName){
		// TODO: Change the status to approved
	}
	
	public void returnClaim(String approverName){
		// TODO: Change the status to returned
	}

	// Method for comparing claims to one another (comparing dates)
	@Override
	public int compareTo(Claim another) {
		return another.getStartDate().compareTo(startDate);
	}

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

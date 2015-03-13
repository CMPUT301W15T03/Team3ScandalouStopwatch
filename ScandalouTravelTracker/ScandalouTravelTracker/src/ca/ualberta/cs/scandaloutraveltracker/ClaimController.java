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

/* ClaimController.java Basic Info:
 *  The Claim Controller class is used for making changes to a Claim that
 *  is supplied to the controller. You should not be making changes to a
 *  claim object directly but through this controller.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ClaimController {
	private Claim currentClaim;
	private ArrayList<String> tags;
	private ArrayList<Expense> expenses;
	
	public ClaimController(Claim currentClaim) {
		this.currentClaim = currentClaim;
		this.tags = currentClaim.getTags();
		this.expenses = currentClaim.getExpenses();
	}

	public String getName() {
		return currentClaim.getName();
	}
	public void setName(String name) {
		currentClaim.setName(name);
	}
	
	public String getDescription(){
		return currentClaim.getDescription();
	}
	public void setDescription(String description) {
		currentClaim.setDescription(description);
	}

	public Date getStartDate(){
		return currentClaim.getStartDate();
	}
	public void setStartDate(Date startDate) {
		currentClaim.setStartDate(startDate);
	}

	public Date getEndDate(){
		return currentClaim.getEndDate();
	}
	public void setEndDate(Date endDate) {
		currentClaim.setEndDate(endDate);
	}

	public ArrayList<Destination> getDestinations() {
		return currentClaim.getDestinations();
	}	
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public void removeTag(String tag) {
		tags.remove(tag);
	}
	
	public void addDestination(Destination destination) {
		currentClaim.addDestination(destination);
	}
	
	public void removeDestination(Destination destination) {
		currentClaim.removeDestination(destination);
	}
	
	public String getStatus() {
		return currentClaim.getStatus();
	}
	
	public void setStatus(String status) {
		currentClaim.setStatus(status);
	}
	
	public void addExpense(Expense expense) {
		expenses.add(expense);
	}
	
	
	public void removeExpense(int expensePos) {	
		
		
		
		// Delete the claim from the claim list
		currentClaim.deleteExpense((int) expensePos);			
		
		
	}
	
	public Expense getExpensePos(int position) {
		return currentClaim.getExpense(position);
	}
	
	public void setCanEdit(Boolean canEdit) {
		currentClaim.setCanEdit(canEdit);
	}
	
	public String calculateTotal() {
		return null;
	}

	public ArrayList<Expense> getExpenseList() {
		// TODO Auto-generated method stub
		return currentClaim.getExpenses();
	}
	public Expense getExpense(int position){
		return Claim.getExpense(position);
	}

	public void notifyViews() {
		// TODO Auto-generated method stub
		
	}
	
}

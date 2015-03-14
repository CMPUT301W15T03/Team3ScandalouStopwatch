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
	
	public ClaimController(Claim currentClaim) {
		this.currentClaim = currentClaim;
	}
	
	public void setExpenses(ArrayList<Expense> expenses) {
		currentClaim.setExpenses(expenses);
	}

	public String getName() {
		return currentClaim.getName();
	}
	public void setName(String name) {
		currentClaim.setName(name);
	}
	
	public boolean getCanEdit() {
		return currentClaim.getCanEdit();
	}
	
	public void setCanEdit(boolean edit) {
		currentClaim.setCanEdit(edit);
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
	
	public ArrayList<String> getTags(){
		return currentClaim.getTags();
	}	
	
	public void addTag(String tag) {
		currentClaim.getTags().add(tag);
	}
	
	public void removeTag(String tag) {
		currentClaim.getTags().remove(tag);
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
		currentClaim.addExpense(expense);
		notifyViews();
	}
	
	
	public void removeExpense(Expense expense) {	
						
		currentClaim.deleteExpense(expense);
		notifyViews();
	}
		
	
	public String calculateTotal() {
		return null;
	}

	public ArrayList<Expense> getExpenseList() {
		// TODO Auto-generated method stub
		return currentClaim.getExpenses();
	}
	public Expense getExpense(int position){
		return currentClaim.getExpense(position);
	}
	
	public void updateClaim(String name, Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, boolean canEdit){
		
		currentClaim.updateClaim(name, startDate, endDate, description, destinations, canEdit);
	}
	
	public void submitClaim(String status, boolean canEdit){
		currentClaim.submitClaim(status, canEdit);
	}
	
	public void updateTags(ArrayList<String> tags){
		currentClaim.updateTags(tags);	
	}
	
	public void deleteClaim(int claimId){
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.deleteClaim(claimId);		
	}

	public void notifyViews() {
		// TODO Auto-generated method stub
		
	}
	
}

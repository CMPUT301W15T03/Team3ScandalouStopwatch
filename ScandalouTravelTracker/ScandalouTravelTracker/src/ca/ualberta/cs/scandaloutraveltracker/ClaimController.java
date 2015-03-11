package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;
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
	
	public void addExpense(Expense expense) {
		expenses.add(expense);
	}
	
	public void removeExpense(Expense expense) {
		expenses.remove(expense);
	}
	
	public void setCanEdit(Boolean canEdit) {
		currentClaim.setCanEdit(canEdit);
	}
	
	public String calculateTotal() {
		return null;
	}
}

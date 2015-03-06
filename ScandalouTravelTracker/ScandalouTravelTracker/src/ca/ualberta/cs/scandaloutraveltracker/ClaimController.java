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
	
	public void setName(String name) {
		currentClaim.setName(name);
	}
	
	public void setDescription(String description) {
		currentClaim.setDescription(description);
	}
	
	public void setStartDate(Date startDate) {
		currentClaim.setStartDate(startDate);
	}
	
	public void setEndDate(Date endDate) {
		currentClaim.setEndDate(endDate);
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

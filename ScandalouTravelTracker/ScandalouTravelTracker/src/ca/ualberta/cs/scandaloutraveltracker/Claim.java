package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Claim {
	private String name;
	private String description;
	private Date startDate;
	private Date endDate; 
	//private ArrayList<Expense> expenses;
	//private ArrayList<Destination> destinations;
	private ArrayList<String> tags; 
	private HashMap<String, Double> total;
	private Boolean canEdit;
	
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
	}
	
	// Getter and Setter Methods
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
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
/*	public ArrayList<Expense> getExpenses() {
		return expenses;
	}
	public void setExpenses(ArrayList<Expense> expenses) {
		this.expenses = expenses;
	}*/
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public HashMap<String, Double> getTotal() {
		return total;
	}
	public void setTotal(HashMap<String, Double> total) {
		this.total = total;
	}
	
	// Other Methods
	public String toString() {
		// TODO: Converting the claim with computeTotal to display on listView
		
		return null;
	}
	
	public HashMap<String, Double> computeTotal() {
		// TODO: Using the HashMap to help compute the total for each type of currency
		
		return null;
	}

	public void setCanEdit(boolean b) {
		this.canEdit = b;
	}
	
}

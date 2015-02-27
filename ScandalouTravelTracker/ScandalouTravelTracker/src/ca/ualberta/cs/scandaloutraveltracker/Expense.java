package ca.ualberta.cs.scandaloutraveltracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Expense {
	private Date date;
	private String category;
	private String description;
	private Double cost;
	private String currencyType;
	private boolean flag;
	
	public Expense() {
		
	}
	
	public Expense(Date date, String category, String description, Double cost, String currencyType) {
		this.date = date;
		this.category = category;
		this.description = description;
		this.cost = cost;
		this.currencyType = currencyType;
		this.flag = false;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
		return this.category + " - " + sdf.format(this.date) + "\n"
				+ this.description + "\n"
				+ String.format("%.2f", this.cost) + " " + this.getCurrencyType();
	}

}

package ca.ualberta.cs.scandaloutraveltracker;

import java.io.File;
import java.util.Date;

public class ExpenseController {
	private Expense currentExpense;
	
	public ExpenseController(Expense currentExpense) {
		this.currentExpense = currentExpense;
	}
	
	public void setDescription(String description) {
		currentExpense.setDescription(description);
	}
	
	public void setCategory(String category) {
		currentExpense.setCategory(category);
	}
	
	public void setCurrency(String currency) {
		currentExpense.setCurrencyType(currency);
	}
	
	public void setFlag(boolean flag) {
		currentExpense.setFlag(flag);
	}
	
	public void setCost(double cost) {
		currentExpense.setCost(cost);
	}
	
	public void setDate(Date date) {
		currentExpense.setDate(date);
	}
	
	public void setPhoto(File photo) {
		currentExpense.setPhoto(photo);
	}
	
}

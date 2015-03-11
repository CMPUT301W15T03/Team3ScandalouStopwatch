package ca.ualberta.cs.scandaloutraveltracker;

import android.app.Application;

public class ClaimApplication extends Application {
	private User user;
	private ClaimListController claimListController;
	private ClaimController claimController;
	private ExpenseController expenseController;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ClaimListController getClaimListController() {
		return claimListController;
	}
	public void setClaimListController(ClaimListController claimListController) {
		this.claimListController = claimListController;
	}
	public ClaimController getClaimController() {
		return claimController;
	}
	public void setClaimController(ClaimController claimController) {
		this.claimController = claimController;
	}
	public ExpenseController getExpenseController() {
		return expenseController;
	}
	public void setExpenseController(ExpenseController expenseController) {
		this.expenseController = expenseController;
	}
	
}
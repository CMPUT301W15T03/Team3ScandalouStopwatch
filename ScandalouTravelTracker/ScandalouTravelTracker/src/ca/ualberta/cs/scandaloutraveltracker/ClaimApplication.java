package ca.ualberta.cs.scandaloutraveltracker;

import android.app.Application;
import android.content.Context;

public class ClaimApplication extends Application {
	
	private User user;
	private ClaimListController claimListController;
	private ClaimController claimController;
	private ExpenseController expenseController;
	private static Context context;
	
	// CITATION http://stackoverflow.com/questions/21818905/get-application-context-from-non-activity-singleton-class, 2015-03-12
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }		
	
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
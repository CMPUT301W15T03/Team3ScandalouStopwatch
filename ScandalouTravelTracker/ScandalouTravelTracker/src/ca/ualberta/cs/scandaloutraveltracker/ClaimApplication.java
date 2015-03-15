package ca.ualberta.cs.scandaloutraveltracker;

import android.app.Application;
import android.content.Context;

/**
 * Helps setup the application for when the app is launched. It
 * helps with setting the user associated with the app when
 * launched and helps get the context for the ClaimMapper. 
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimApplication extends Application {
	
	private User user;
	private ClaimListController claimListController;
	private static Context context;
	
	// CITATION http://stackoverflow.com/questions/21818905/get-application-context-from-non-activity-singleton-class, 2015-03-12
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * 
     * @return Context of the app at time method is called
     */
    public static Context getContext() {
        return context;
    }		
	
    /**
     * 
     * @return User associated with current run of app
     */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user to be associated with current run of the app.
	 * This helps sets the appropriate views for certain activities.
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * 
	 * @return ClaimListController
	 */
	public ClaimListController getClaimListController() {
		return claimListController;
	}
	
}
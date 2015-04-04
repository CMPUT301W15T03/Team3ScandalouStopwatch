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

package ca.ualberta.cs.scandaloutraveltracker;

import ca.ualberta.cs.scandaloutraveltracker.models.User;
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
	private static Context context;
	
	// CITATION http://stackoverflow.com/questions/21818905/get-application-context-from-non-activity-singleton-class, 2015-03-12
    @Override
    public void onCreate() {
        super.onCreate();
        
        context = getApplicationContext();
        
        ConnectivityChangeReceiver ccr = new ConnectivityChangeReceiver();
        Constants.CONNECTIVITY_STATUS = ccr.isOnline(context);
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
	
}
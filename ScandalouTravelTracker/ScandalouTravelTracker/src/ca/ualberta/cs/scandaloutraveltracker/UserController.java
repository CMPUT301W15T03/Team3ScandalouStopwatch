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

import android.location.Location;

/**
 *  The User Controller class is used for making changes to a User that
 *  is supplied to the controller. You should not be making changes to a
 *  user object directly but through this controller.
 * @author Team3ScandalouStopwatch
 *
 */
public class UserController {
	private User currentUser;
	
	/**
	 * Constructor that allows the UserController to edit the details of
	 * the user that is passed to it.
	 * @param currentUser
	 */
	public UserController(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public void setCurrentLocation(Location location) {
		this.currentUser.setCurrentLocation(location);
	}
	
	/**
	 * 
	 * @return Name of the user associated with the user.
	 */
	public String getName() {
		return currentUser.getName();
	}
	
	/**
	 * Set name of user associated with user. Should be set
	 * via the User's name.
	 * @param name
	 */
	public void setName(String name) {
		currentUser.setName(name);
	}
	
	/**
	 * 
	 * @return Status of the user associated with the user.
	 */
	public int getMode() {
		return currentUser.getMode();
	}
	
	/**
	 * Set status of user associated with user. Should be set
	 * via the User's status.
	 * @param status
	 */
	public void setMode(int mode) {
		currentUser.setMode(mode);
	}
	
}
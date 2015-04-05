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
import ca.ualberta.cs.scandaloutraveltracker.views.UserSelectActivity;

/**
 * The DialogCreator class helps to create dialogs throughout the application.
 * The only dialog currently being built in here is the user information alert.
 * @author Team3ScandalouStopwatch
 *
 */
public class DialogCreator {
	private UserInformationDialog dialog;

	/**
	 * Allows you to retrieve the information pertaining to a particular user via
	 * a dialog box.
	 * @return UserInformationDialog Dialog containing user's location and name information
	 */
	public UserInformationDialog getDialog() {
		return dialog;
	}

	/**
	 * Sets the dialog within the dialog creator to be the appropriate user
	 * dialog. 
	 * @param dialog The created dialog with user information
	 */
	public void setDialog(UserInformationDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * Creates a user information dialog containing the user's name and location's
	 * latitude and longitude values.
	 * @param currentUser The user for whose information we want to view
	 * @param userSelectActivity The activity where these dialogs can be shown
	 */
	public void buildUserInfoAlert(User currentUser,
			UserSelectActivity userSelectActivity) {
		String name = currentUser.getName();
		String latitude = "Not Set";
		String longitude = "Not Set";
		if (currentUser.getHomeLocation() != null) {
			latitude = "" + currentUser.getHomeLocation().getLatitude();
			longitude = "" + currentUser.getHomeLocation().getLongitude();
		}
		dialog = new UserInformationDialog();
		dialog.setViews(name, latitude, longitude);
		dialog.show(userSelectActivity.getFragmentManager(),
				"UserInformationDialogFragment");
	}
}
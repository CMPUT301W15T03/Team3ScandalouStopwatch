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


public class DialogCreator {
	private UserInformationDialog dialog;

	public UserInformationDialog getDialog() {
		return dialog;
	}

	public void setDialog(UserInformationDialog dialog) {
		this.dialog = dialog;
	}

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
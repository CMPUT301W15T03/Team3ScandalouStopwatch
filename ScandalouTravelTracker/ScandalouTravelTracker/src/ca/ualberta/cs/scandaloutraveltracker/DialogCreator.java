package ca.ualberta.cs.scandaloutraveltracker;


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
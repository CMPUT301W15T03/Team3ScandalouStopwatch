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

package ca.ualberta.cs.scandaloutraveltracker.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.DialogCreator;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.UserInformationDialog;
import ca.ualberta.cs.scandaloutraveltracker.UserListAdapter;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.ClaimList;
import ca.ualberta.cs.scandaloutraveltracker.models.User;

/**
 *	The first activity that is called when the app starts. The activity allows the user to pick, create, edit or 
 *	delete existing accounts on the device, assign a home location to them, and sign in with one of them
 * @author Team3ScandalouStopwatch
 *
 */
public class UserSelectActivity extends Activity implements ViewInterface, UserInformationDialog.UserInformationDialogListener {
	private DialogCreator dialogCreator = new DialogCreator();
	private Button newUserButton;
	private ListView usersLV; 
	private UserListAdapter adapter; 
	private UserListController ulc;
	private int newUserId;
	private int userPos;
	private UserController uc;
	private EditText userNameET;
	private AlertDialog alert;
	private LocationManager lm;
	private Location location;
	private ContextMenu contextMenu;
	/**
	 * 	Called when the activity is created. Sets up the gps geolocation and creates
	 *  the user list view and corresponding listeners
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_select);
		
		ulc = new UserListController();
		
		// Get most recent location
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		setUpDisplay();
		
	}
	
	/**
	 * 	Creates a context menu that is displayed whenever a user is clicked on. The menu allows the 
	 * 	user to add a geolocation through gps, a map or clear the existing geolocation. It also allows
	 *  them to delete the account they long clicked on.
	 */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	userPos = (int) info.id;
    	super.onCreateContextMenu(menu, v, menuInfo);
    	contextMenu = menu;
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.user_context_menu, menu);
    }
    
    /**
     * 	Executes actions depending on which menu item the user clicked on. These include the user 
     * 	adding a geolocation through gps, a map or clearing the existing geolocation. Or delete 
     * 	the account they long clicked on.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.user_context_add_location_gps:
			if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null) {
				Toast.makeText(getApplicationContext(),
						"GPS curently unavailable",Toast.LENGTH_SHORT).show();
				return true;
			}
			User selectedUser = (User) usersLV.getItemAtPosition(userPos);
			int selectedId = selectedUser.getId();
			ulc.removeUser(selectedId);
			uc = new UserController(new User(selectedUser.getId()));
			location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			uc.setCurrentLocation(location);
			ulc.addUser(new User(selectedId));
			update();
			return true;
			
		case R.id.user_context_add_location_map:
			Intent intent = new Intent(UserSelectActivity.this, SetHomeLocationActivity.class);
			intent.putExtra("userId", ulc.getUser(userPos).getId());
			startActivity(intent);
			return true;
			
		case R.id.user_context_clear_location:
			selectedUser = (User) usersLV.getItemAtPosition(userPos);
			selectedId = selectedUser.getId();
			ulc.removeUser(selectedId);
			uc = new UserController(new User(selectedUser.getId()));
			location = null;
			uc.setCurrentLocation(location);
			ulc.addUser(new User(selectedId));
			update();
			return true;
		case R.id.user_context_delete_user:
			ClaimList cl = new ClaimList();
			selectedUser = (User) usersLV.getItemAtPosition(userPos);
			selectedId = selectedUser.getId();
			ulc.removeUser(selectedId);
			cl.deleteUserClaims(selectedId);
			update();
			return true;
		default:
			return super.onContextItemSelected(item);
		}	
    }
	
    /**
     * 	Sets up the user ListView and button for adding a new user.
     */
	public void setUpDisplay() {
		
		// Setup display elements
		newUserButton = (Button) findViewById(R.id.userSelectCreateUserButton);
		usersLV = (ListView) findViewById(R.id.userSelectUsersLV);
		
		registerForContextMenu(usersLV);
		
		// Set newUserButton onClick functionality
		newUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userNameET = new EditText(UserSelectActivity.this);
				AlertDialog.Builder builder = new AlertDialog.Builder(UserSelectActivity.this);
				builder.setTitle("Enter New User's Name:")
				.setCancelable(true)
				.setView(userNameET)
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						newUserId = ulc.createUser(userNameET.getText().toString());
						ulc.addUser(new User(newUserId));
						
						update();
					}
				});
				alert = builder.create();
				alert.show();
				
			}
		});
		
		// Set up clicking on a user
		usersLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long claimPos) {
				
				// Display user information dialog
				User user = (User) usersLV.getItemAtPosition(position);
				userPos = position;
				dialogCreator.buildUserInfoAlert(user, UserSelectActivity.this);
			}
		});
		
		// Set up ListView and Adapter
		adapter = new UserListAdapter(this, ulc.getUserList());
		usersLV.setAdapter(adapter);
	}
	
	/**
	 * 
	 */
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// Launch the ClaimListActivity
		if ((ulc.getUser(userPos)).getHomeLocation() == null) {
			Toast.makeText(getApplicationContext(),
					"Home location needs to be set. Long click on user to set",Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(UserSelectActivity.this, ClaimListActivity.class);
		intent.putExtra("userId", ulc.getUser(userPos).getId());
		startActivity(intent);
	}

	/**
	 * 	Does not do anything except exiting the alert dialog.
	 */
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// Doesn't do anything (exits alert)
	}
	
	/**
	 * 	Creates the default action bar menu since it does not need to have a change user option.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.select_user, menu);
		return true;
	}

	/**
	 *	Refreshes the user ListView.
	 */
	@Override
	public void update() {
		ulc = new UserListController();
		adapter = new UserListAdapter(this, ulc.getUserList());
		usersLV.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * updates everything onResume
	 */
	@Override
	public void onResume() {
		super.onResume();
		update();
	}
	
	// Testing Methods
	/**
	 *  Used for testing
	 * @return the last AlertDialog displayed on the screen
	 */
	public AlertDialog getDialog() {
		return alert;
	}
	
	/**
	 * 	Used for testing
	 * 	Gets the phones actual location to see if it was 
	 * 	set properly for the user
	 * @return the phones actual location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * 	Used for testing
	 * 	Gets the context menu for testing deleting and
	 *  setting user locations.
	 * @return the context menu
	 */
	public ContextMenu getContextMenu() {
		return contextMenu;
	}
	
	/**
	 * 	Used for testing
	 * 	Gets the user information dialog to verify and
	 * 	check information set (location)
	 * @return the user information dialog
	 */
	public UserInformationDialog getUserDialog() {
		return dialogCreator.getDialog();
	}

}

package ca.ualberta.cs.scandaloutraveltracker;

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
import android.widget.TextView;

public class UserSelectActivity extends Activity implements ViewInterface, UserInformationDialog.UserInformationDialogListener {
	private Button newUserButton;
	private TextView selectUserTV;
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
	private DialogFragment dialog;

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
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	userPos = (int) info.id;
    	super.onCreateContextMenu(menu, v, menuInfo);
    	contextMenu = menu;
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.user_context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.user_context_add_location_gps:
			User selectedUser = (User) usersLV.getItemAtPosition(userPos);
			uc = new UserController(new User(selectedUser.getId()));
			location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			uc.setCurrentLocation(location);
		default:
			return super.onContextItemSelected(item);
		}	
    }
	
	public void setUpDisplay() {
		
		// Setup display elements
		newUserButton = (Button) findViewById(R.id.userSelectCreateUserButton);
		selectUserTV = (TextView) findViewById(R.id.userSelectCurrentUsersTV);
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
						UserController uc = new UserController(new User(newUserId));
						uc.setCurrentLocation(location);
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
				buildUserInfoAlert(user.getName());
				
				// Launch the ClaimListActivity
				Intent intent = new Intent(UserSelectActivity.this, ClaimListActivity.class);
				intent.putExtra("userId", ulc.getUser(position).getId());
				startActivity(intent);
			}
		});
		
		// Set up ListView and Adapter
		adapter = new UserListAdapter(this, ulc.getUserList());
		usersLV.setAdapter(adapter);
	}
	
	// USER INFORMATION ALERT METHODS STARTS HERE
	
	public void buildUserInfoAlert(String name) {
		dialog = new UserInformationDialog();
		dialog.show(getFragmentManager(), "UserInformationDialogFragment");
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
	
	// USER INFORMATION ALERT METHODS ENDS HERE

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.select_user, menu);
		return true;
	}

	@Override
	public void update() {
		adapter.notifyDataSetChanged();
	}
	
	// Testing Methods
	
	// Returns last AlertDialog displayed on the screen
	// Used for testing
	public AlertDialog getDialog() {
		return alert;
	}
	
	// Gets the phones actual location to see if it was 
	// set properly for the user
	public Location getLocation() {
		return location;
	}
	
	// Gets the context menu for testing deleting and
	// setting user locations
	public ContextMenu getContextMenu() {
		return contextMenu;
	}
	
	// Gets the user information dialog to verify and
	// check information set (location)
	public DialogFragment getDialogFragment() {
		return dialog;
	}

}

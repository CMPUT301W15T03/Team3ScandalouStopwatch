package ca.ualberta.cs.scandaloutraveltracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class UserSelectActivity extends Activity implements ViewInterface {
	private Button newUserButton;
	private TextView selectUserTV;
	private ListView usersLV; 
	private UserListAdapter adapter; 
	private UserListController ulc;
	private int newUserId;
	private EditText userNameET;
	private AlertDialog alert;
	private UserController uc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_select);
		
		ulc = new UserListController();
		
		setUpDisplay();
		
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
				
				// Set user in ClaimApplication
				ClaimApplication app = (ClaimApplication) getApplicationContext();
				app.setUser(ulc.getUser(position));
				
				// Launch the ClaimListActivity
				Intent intent = new Intent(UserSelectActivity.this, ClaimListActivity.class);
				startActivity(intent);
			}
		});
		
		// Set up ListView and Adapter
		adapter = new UserListAdapter(this, ulc.getUserList());
		usersLV.setAdapter(adapter);
	}

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

}

package ca.ualberta.cs.scandaloutraveltracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class UserSelectActivity extends Activity implements ViewInterface {
	private Button newUserButton;
	private TextView selectUserTV;
	private ListView usersLV; 
	private ArrayAdapter adapter; 
	private UserListController ulc;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_select);
		
		setUpDisplay();
		
		ulc = new UserListController();
		
	}
	
	public void setUpDisplay() {
		// Setup display elements
		newUserButton = (Button) findViewById(R.id.userSelectCreateUserButton);
		selectUserTV = (TextView) findViewById(R.id.userSelectCurrentUsersTV);
		usersLV = (ListView) findViewById(R.id.userSelectUsersLV);
		
		// Set newUserButton onClick functionality
		newUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Create dialog that asks for name and add that new user to the list
				
			}
		});
		
		// Set up clicking on a user
		usersLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long claimPos) {
				// TODO When user is selected, go to the ClaimListActivity screen
				
			}
		});
		
		// Set up ListView and Adapter
		adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ulc.getUserList());
		usersLV.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to textviewthe action bar if it is present.
		getMenuInflater().inflate(R.menu.select_user, menu);
		return true;
	}

	@Override
	public void update() {
		adapter.notifyDataSetChanged();
	}

}

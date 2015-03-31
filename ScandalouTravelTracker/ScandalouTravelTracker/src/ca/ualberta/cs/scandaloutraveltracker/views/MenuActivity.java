package ca.ualberta.cs.scandaloutraveltracker.views;

import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.R.id;
import ca.ualberta.cs.scandaloutraveltracker.R.menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class MenuActivity extends Activity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_items, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
    	// Goes to "main" menu of the app while clearing the activity stack.
        case R.id.action_user:
        	Intent intent = new Intent(getApplicationContext(), UserSelectActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

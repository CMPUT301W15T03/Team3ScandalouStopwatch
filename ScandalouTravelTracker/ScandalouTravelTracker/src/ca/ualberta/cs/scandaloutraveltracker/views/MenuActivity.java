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

import ca.ualberta.cs.scandaloutraveltracker.R;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Changes the action bar menu to have the option to change the user. Instead of extending activity 
 * every activity that doesn't implement their own action bar changes should extend this.
 * @author Team3ScandalouStopwatch
 *
 */
public abstract class MenuActivity extends Activity {
	/**
	 * changes the action bar drop down menu to have difficult menu than normal
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_items, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 	If change user is pressed the activity stack is cleared and the starting activity, UserSelectActivity,
	 *  is started
	 */
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

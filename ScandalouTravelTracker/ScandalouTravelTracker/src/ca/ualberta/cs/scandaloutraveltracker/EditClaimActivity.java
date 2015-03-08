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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class EditClaimActivity extends Activity implements ViewInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		
		TextView nameDisplay = (TextView) findViewById(R.id.edit_claim_name);
		Button startDateButton = (Button) findViewById(R.id.edit_claim_start_date);
		Button endDateButton = (Button) findViewById(R.id.edit_claim_end_date);
		TextView descriptionDisplay = (TextView) findViewById(R.id.edit_claim_description);
		Button updateButton = (Button) findViewById(R.id.edit_claim_update);
		Button sendButton = (Button) findViewById(R.id.edit_claim_send);
		
		update();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_claim, menu);
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}

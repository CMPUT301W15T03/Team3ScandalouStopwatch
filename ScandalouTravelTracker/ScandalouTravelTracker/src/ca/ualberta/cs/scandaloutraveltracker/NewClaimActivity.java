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

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;

import android.view.View.OnLongClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.content.Context;
import android.content.DialogInterface;
//import android.content.Context;

public class NewClaimActivity extends Activity implements ViewInterface {
	
	Claim claim;
	String name;
	String sDate;
	String eDate;
	ArrayList<Destination> dList;
	String description;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_claim);
		
		final EditText nameSet = (EditText)findViewById(R.id.claimant_name);
		final EditText sDateSet = (EditText)findViewById(R.id.start_date);	
		final EditText eDateSet = (EditText)findViewById(R.id.end_date);	
		final EditText descriptionSet = (EditText)findViewById(R.id.edit_claim_description);

		Button claimOkButton = (Button) findViewById(R.id.claim_ok_button);
			claimOkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//fills in most fields of claim from edit texts
				claim.setName(nameSet.getText().toString());
				claim.setDescription(descriptionSet.getText().toString());
				claim.setStartDate(((Date)sDateSet.getText()));
				claim.setEndDate(((Date)eDateSet.getText()));
				
			}
		});
		
		final Context context = this;
		ListView destList = (ListView)findViewById(R.id.destinations_lv);
		
		destList.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
			  public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
			    
				  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setMessage("Destination Options")
							.setCancelable(false)
							.setPositiveButton("Edit",new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog,int id) {
									//click edit
									dialog.cancel();
								}
						})
							
						.setNeutralButton("Delete", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.cancel();
								//click delete
								
								
							}
						})
						  .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// close the dialog box
								dialog.cancel();
							}
					});
			  		return true;
			  		}});
	
	}
		
		//todo: add destinations
			


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_claim, menu);
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
		  }



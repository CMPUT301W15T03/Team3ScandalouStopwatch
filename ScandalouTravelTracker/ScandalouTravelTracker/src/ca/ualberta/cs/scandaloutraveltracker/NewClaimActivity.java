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

/* NewClaimActivity.java Basic Info:
 *  Activity that allows the user to add a new claim to the claim list.
 *  Accessed through the add button on the ClaimListActivity.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
import android.content.Intent;

public class NewClaimActivity extends Activity implements ViewInterface {
	
	Claim c = new Claim();
	ClaimController claim = new ClaimController(c);
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
				
				String sdate = sDateSet.getText().toString();
				String edate = sDateSet.getText().toString();
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
				Date startDate;
				try {
					startDate = sdf.parse(sdate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e);
				}
				Date endDate;
				try {
					endDate = sdf.parse(edate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e);
				}
				
				claim.setStartDate(startDate);
				claim.setEndDate(endDate);
				

				Intent intent = new Intent(NewClaimActivity.this, ClaimListActivity.class);
				startActivity(intent);
				
			}
		});
		
		final Context context = this;
		ListView destList = (ListView)findViewById(R.id.destinations_lv);
		
		Button addDestButton = (Button) findViewById(R.id.add_dest_button);
			addDestButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					AlertDialog.Builder newDest = new AlertDialog.Builder(context);
					newDest.setCancelable(false);
					newDest.setMessage("New Destination");
					final EditText name = new EditText(context);
					newDest.setView(name);
					
					final EditText reason = new EditText(context);
					newDest.setView(reason);
					
					newDest.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
						public void onClick (DialogInterface dialog, int id){
							Destination d = new Destination("h", "g");
						//			name.getText().toString(), reason.getText().toString());
							claim.addDestination(d);
							dialog.cancel();
							
						}
					});
					newDest.show();
				}
			});
	
	
	
	}
		
	/*	DestinationListAdapter destListAdapter = new DestinationListAdapter(this, claim.getDestinations());
		destList.setAdapter(destListAdapter);
		
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
								//destList.remove(position);
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
	
	} */
		
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



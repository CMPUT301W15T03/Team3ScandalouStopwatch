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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *  The ClaimListAdapter allows essential information from a Claim to
 *  be displayed in a ListView.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimListAdapter extends BaseAdapter {
	protected ClaimList claimList;
	protected Context context;
	protected boolean approverMode;
	
	/**
	 * Context of the activity that list is to be displayed in and
	 * the ClaimList that is to be displayed.
	 * @param context
	 * @param claimList
	 * @param approverMode
	 */
	public ClaimListAdapter(Context context, ClaimList claimList, boolean approverMode) {
		this.context = context;
		this.claimList = claimList;
		this.approverMode = approverMode;
	}

	/**
	 * @return Count of the claim list
	 */
	@Override
	public int getCount() {
		return claimList.getCount();
	}

	/**
	 * @return Claim associated with position 
	 */
	@Override
	public Object getItem(int position) {
		return claimList.getClaim(position);
	}

	/**
	 * @return The Claim associated with position 
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	/**
	 * The getView method sets up the list to properly show the fields
	 * of a Claim that we want to show to the user.
	 * @param position To locate the right claim to show
	 * @param convertView To see if a view has been created already
	 * @param parent Where this view will be displayed
	 * @return View that is associated with one Claim item
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// If an instance of the view hasn't been created yet, create it.
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_claim_display, parent, false);
		}
		
		// Create TextViewscurrentClaim.get
		TextView claimNameTV = (TextView) convertView.findViewById(R.id.claimListNameTV);
		TextView claimApproverTV = (TextView) convertView.findViewById(R.id.claimListApproverTV);
		TextView claimCommentsTV = (TextView) convertView.findViewById(R.id.claimListCommentsTV);
		TextView claimDateTV = (TextView) convertView.findViewById(R.id.claimListDateTV);
		TextView claimDestinationTV = (TextView) convertView.findViewById(R.id.claimListDestinationsTV);
		TextView claimStatusTV = (TextView) convertView.findViewById(R.id.claimListStatusTV);
		TextView claimTotalTV = (TextView) convertView.findViewById(R.id.claimListTotalsTV);
		TextView claimTagsTV = (TextView) convertView.findViewById(R.id.claimListTagsTV);
		
		// Fetch current Claim
		Claim currentClaim = claimList.getClaim(position);
		
		// Build the total expenses string
		// CITATION http://stackoverflow.com/questions/46898/iterate-over-each-entry-in-a-map/46908#46908
		// 2015-01-29
		// ScArcher2's answer
		int i = 0;
		String totalsStr = "";
		NumberFormat formatter = new DecimalFormat("#0.00");	
		HashMap<String, Double> totals = currentClaim.computeTotal();
		for (Map.Entry<String, Double> entry : totals.entrySet()){
			totalsStr += entry.getKey() + " " + formatter.format(entry.getValue());
			if (i < totals.size() - 1){
				totalsStr += ", "; 
			}
		    i++;
		}		
		// show approver on returned claims
		if (currentClaim.getStatus().equals(Constants.statusReturned)) {
			claimNameTV.setVisibility(View.GONE);
			claimApproverTV.setVisibility(View.VISIBLE);
			claimCommentsTV.setVisibility(View.VISIBLE);
		}
		// show approver on approved claims
		else if (currentClaim.getStatus().equals(Constants.statusApproved)) {
			claimNameTV.setVisibility(View.GONE);
			claimApproverTV.setVisibility(View.VISIBLE);
			claimCommentsTV.setVisibility(View.VISIBLE);
		}
		// don't show approver on in progress or submitted claims
		else {
			claimNameTV.setVisibility(View.GONE);
			claimApproverTV.setVisibility(View.GONE);
			claimCommentsTV.setVisibility(View.GONE);
		}
		// approver can see previous approver and claimant name
		if (this.approverMode) {
			claimNameTV.setVisibility(View.VISIBLE);
			claimApproverTV.setVisibility(View.VISIBLE);
			claimCommentsTV.setVisibility(View.VISIBLE);
		}
		
		// Set TextViews
		claimNameTV.setText("Claimant Name: " + currentClaim.getUser().getName());
		claimApproverTV.setText("Approver Name: " + currentClaim.getApproverName());
		claimCommentsTV.setText("Previous Comments:\n" + currentClaim.getApproverCommentsString());
		claimDateTV.setText(currentClaim.getStartDateString() + 
							" - " + 
							currentClaim.getEndDateString());
		
		claimDestinationTV.setText("Destinations: " + currentClaim.destinationsToString());
		claimStatusTV.setText("Status: " + currentClaim.getStatus());
		claimTotalTV.setText(totalsStr);
		
		claimTagsTV.setText(currentClaim.tagsToString());
		
		// Setting default (empty) values
		if (currentClaim.destinationsToString().equals("")) {
			claimDestinationTV.setText("No destinations");
		}
		if (totalsStr.equals("")) {
			claimTotalTV.setText("Totals: ");
		}
		if (currentClaim.tagsToString().equals("")) {
			claimTagsTV.setText("Tags: ");
		}
		
		return convertView;
	}
	
}

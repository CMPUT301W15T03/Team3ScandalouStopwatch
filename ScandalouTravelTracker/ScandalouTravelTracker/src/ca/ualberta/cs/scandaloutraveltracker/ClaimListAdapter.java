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
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.ClaimList;

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
	private Claim currentClaim;
	
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
	 * of a Claim that we want to show to the user. The view will set
	 * visibility of elements based on the current mode that the user is
	 * in and the claim's status. How far this claim is from the user's
	 * home location is also set here.
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
		TextView claimUpperDateTV = (TextView) convertView.findViewById(R.id.claimListUpperDateTV);
		TextView claimLowerDateTV = (TextView) convertView.findViewById(R.id.claimListLowerDateTV);
		TextView claimDestinationTV = (TextView) convertView.findViewById(R.id.claimListDestinationsTV);
		TextView claimStatusTV = (TextView) convertView.findViewById(R.id.claimListStatusTV);
		TextView claimTotalTV = (TextView) convertView.findViewById(R.id.claimListTotalsTV);
		TextView claimTagsTV = (TextView) convertView.findViewById(R.id.claimListTagsTV);
		ProgressBar claimDistancePB = (ProgressBar) convertView.findViewById(R.id.locationBar);
		TextView progressTV = (TextView) convertView.findViewById(R.id.progressBarTextView);
		
		// Fetch current Claim
		currentClaim = claimList.getClaim(position);
		
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
		}
		// show approver on approved claims
		else if (currentClaim.getStatus().equals(Constants.statusApproved)) {
			claimNameTV.setVisibility(View.GONE);
			claimApproverTV.setVisibility(View.VISIBLE);
		}
		// don't show approver on in progress or submitted claims
		else {
			claimNameTV.setVisibility(View.GONE);
			claimApproverTV.setVisibility(View.GONE);
		}
		// approver can see previous approver and claimant name
		if (this.approverMode) {
			claimNameTV.setVisibility(View.VISIBLE);
			claimApproverTV.setVisibility(View.VISIBLE);
		}
		
		// Set TextViews
		claimNameTV.setText("Claimant: " + currentClaim.getUser().getName());
		claimApproverTV.setText("Approver: " + currentClaim.getApproverName());
		claimUpperDateTV.setText(currentClaim.getStartDateString() + " - ");
		claimLowerDateTV.setText(currentClaim.getEndDateString() + "    ");
		claimDestinationTV.setText(currentClaim.destinationsToString());
		claimStatusTV.setText("Status: " + currentClaim.getStatus());
		claimTotalTV.setText(totalsStr);
		claimTagsTV.setText(currentClaim.tagsToString());
		
		if (this.approverMode) {
			claimDistancePB.setVisibility(View.GONE);
		}
		// http://stackoverflow.com/questions/2020882/how-to-change-progress-bars-progress-color-in-android 2015-04-03
		else {
			int progress = currentClaim.getProgress();
			claimDistancePB.setProgress(progress);
			// setting appropriate colors
			if (progress == 0) {
				claimDistancePB.setVisibility(View.GONE);
				progressTV.setVisibility(View.GONE);
			}
			else if (0 < progress && progress < 25) {
				claimDistancePB.getProgressDrawable().setColorFilter(Color.parseColor("#00FF00"), Mode.SRC_IN);
				claimDistancePB.setVisibility(View.VISIBLE);
				progressTV.setVisibility(View.VISIBLE);
			}
			else if (25 < progress && progress < 50) {
				claimDistancePB.getProgressDrawable().setColorFilter(Color.parseColor("#FFFF00"), Mode.SRC_IN);
				claimDistancePB.setVisibility(View.VISIBLE);
				progressTV.setVisibility(View.VISIBLE);
			}
			else if (50 < progress && progress < 75) {
				claimDistancePB.getProgressDrawable().setColorFilter(Color.parseColor("#FFFF00"), Mode.SRC_IN);
				claimDistancePB.setVisibility(View.VISIBLE);
				progressTV.setVisibility(View.VISIBLE);
			}
			else if (progress > 75) {
				claimDistancePB.getProgressDrawable().setColorFilter(Color.parseColor("#FF0000"), Mode.SRC_IN);
				claimDistancePB.setVisibility(View.VISIBLE);
				progressTV.setVisibility(View.VISIBLE);
			}
		}
		
		// Setting default (empty) values and setting colors of non empty values
		Resources res = context.getResources();
		int color = res.getColor(R.color.LightGray);
		if (currentClaim.destinationsToString().equals("")) {
			
			claimDestinationTV.setText("No destinations");
		} else {
			claimDestinationTV.setTextColor(color);
		}
		if (totalsStr.equals("")) {
			claimTotalTV.setText("Totals: ");
		} else {
			claimTotalTV.setTextColor(color);
		}
		if (currentClaim.tagsToString().equals("")) {
			claimTagsTV.setText("Tags: ");
		} else {
			claimTagsTV.setTextColor(color);
		}
		
		return convertView;
	}
	
}

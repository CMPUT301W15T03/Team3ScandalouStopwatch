package ca.ualberta.cs.scandaloutraveltracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClaimListAdapter extends BaseAdapter {
	protected ClaimList claimList;
	protected Context context;
	
	public ClaimListAdapter(Context context, ClaimList claimList) {
		this.context = context;
		this.claimList = claimList;
	}

	@Override
	public int getCount() {
		return claimList.getCount();
	}

	@Override
	public Object getItem(int position) {
		return claimList.getClaim(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// If an instance of the view hasn't been created yet, create it.
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_claim_display, parent, false);
		}
		
		// Create TextViewscurrentClaim.get
		TextView claimDateTV = (TextView) convertView.findViewById(R.id.claimListDateTV);
		TextView claimDestinationTV = (TextView) convertView.findViewById(R.id.claimListDestinationsTV);
		TextView claimStatusTV = (TextView) convertView.findViewById(R.id.claimListStatusTV);
		TextView claimTotalTV = (TextView) convertView.findViewById(R.id.claimListTotalsTV);
		//TextView claimTagsTV = (TextView) convertView.findViewById(R.id.claimListTagsTV);
		
		// Fetch current Claim
		Claim currentClaim = claimList.getClaim(position);
		// The claim id needs to be accessible in the "Go to edit claim" listener, but I don't
		// know how else to do it besides making something "final". It's kind of ugly but it's 
		// fairly unobtrusive.
		final int currentClaimId = currentClaim.getId();

		
		// Set TextViews
		claimDateTV.setText(currentClaim.getStartDate().toString() + 
							" - " + 
							currentClaim.getEndDate().toString());
		
		claimDestinationTV.setText("NEED TOSTRING IN DESTINATION");
		claimStatusTV.setText("NEED GETSTATUS IN CLAIM");
		claimTotalTV.setText("NEED TOTALSTOSTRING IN CLAIM");
		//claimTagsTV.setText(currentClaim.getTags().toString());
		
		// Go to edit claim
		convertView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				
				Intent intent = new Intent(ClaimListAdapter.this.context.getApplicationContext(), EditClaimActivity.class);
				intent.putExtra(Constants.claimIdLabel, currentClaimId);				
				context.startActivity(intent);
			}
		});			
		
		return convertView;
	}
	
}

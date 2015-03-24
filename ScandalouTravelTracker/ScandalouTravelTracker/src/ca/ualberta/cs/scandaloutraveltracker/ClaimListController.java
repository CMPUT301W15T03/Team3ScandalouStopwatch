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
import java.util.Collections;
import java.util.Date;

/**
 *  When making edits to the Claim List model it should be done through
 *  this ClaimListController class.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimListController {
	
	private ClaimList claimList = null;
	
	/**
	 * Constructor just sets the claimList to the only ClaimList
	 * that exists in the app (ClaimList is a Singleton).
	 */
	public ClaimListController() {
		claimList = new ClaimList();
	}
	
	/**
	 * Adds a view that the ClaimList is on.
	 * @param view
	 */
	public void addView(ViewInterface view) {
		claimList.addView(view);
	}

	/**
	 * 
	 * @param view
	 */
	public void removeView(ViewInterface view) {
		claimList.removeView(view);
	}
	
	/**
	 * Notify all the views associated with the claimList that data
	 * has changed.
	 */
	public void notifyViews() {
		claimList.notifyViews();
	}	
	
	/**
	 * Creates a new claim.
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param destinations
	 * @param tagsList
	 * @param status
	 * @param canEdit
	 * @param expenses
	 * @return Newly created claim's ID
	 */
	public int createClaim(String name, Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, ArrayList<String> tagsList, String status,
			boolean canEdit, ArrayList<Expense> expenses, User user){
		
		int newClaimId = claimList.createClaim(name, startDate, endDate, description, 
				destinations, tagsList, status, canEdit, expenses, user);
		
		return newClaimId;
		
	}
	
	/**
	 * Deletes the claim from storage
	 * @param claimId
	 */
	public void deleteClaim(int claimId){
		claimList.deleteClaim(claimId);
	}	
	
	/**
	 * 
	 * @param claim
	 */
	public void addClaim(Claim claim) {
		claimList.addClaim(claim);
		Collections.sort(claimList.getClaims());
	}
	
	/**
	 * Removes the claim from the list
	 * @param claimId
	 */
	public void removeClaim(int claimId) {
		claimList.removeClaim((int) claimId);
	}
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public Claim getClaim(int position) {
		return claimList.getClaim(position);
	}
	
	/**
	 * 
	 * @return
	 */
	public ClaimList getClaimList() {
		return claimList;
	}
}

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

package ca.ualberta.cs.scandaloutraveltracker.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.ClaimList;
import ca.ualberta.cs.scandaloutraveltracker.models.Destination;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.models.User;
import ca.ualberta.cs.scandaloutraveltracker.views.ViewInterface;

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
	
	public ClaimListController(User user) {
		claimList = new ClaimList(user);
	}
	
	public ClaimListController(User user, String mode) {
		if (mode.equals(Constants.APPROVER_MODE)) {
			claimList = new ClaimList(user, mode);
		}
	}
	
	public ClaimListController(User user, String mode, ArrayList<String> selectedTags) {
		if (mode.equals(Constants.TAG_MODE)) {
			claimList = new ClaimList(user, mode, selectedTags);
		}
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
	public int createClaim(Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, ArrayList<String> tagsList, String status,
			boolean canEdit, ArrayList<Expense> expenses, User user) throws UserInputException {
		
		int newClaimId = claimList.createClaim(startDate, endDate, description, 
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
	}
	
	/**
)	 * Removes the claim from the list
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
	
	/**
	 * 
	 * Sorts the claim list with the newest claims appearing first
	 */
	public void sortNewFirst() {
		ArrayList<Claim> temp = claimList.getClaims();
		Collections.sort(temp, new Comparator<Claim>() {
			@Override
			public int compare(Claim first, Claim second) {
				return second.getStartDate().compareTo(first.getStartDate());
			}
		});
		claimList.claims = temp;
	}
	
	/**
	 * 
	 * Sorts the claim list with the oldest claims appearing first
	 */
	public void sortLastFirst() {
		ArrayList<Claim> temp = claimList.getClaims();
		Collections.sort(temp, new Comparator<Claim>() {
			@Override
			public int compare(Claim first, Claim second) {
				return first.getStartDate().compareTo(second.getStartDate());
			}
		});
		claimList.claims = temp;
	}
}

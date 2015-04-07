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

import android.location.Location;
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
	
	/**
	 * Constructor that is used in getting a specific user's Claim List
	 * @param user User whose Claim List you want
	 */
	public ClaimListController(User user) {
		claimList = new ClaimList(user);
	}
	
	/**
	 * ClaimController constructor that is used for when you are viewing
	 * the claims in Approver Mode.
	 * @param user The current User that is logged in
	 * @param mode The mode you wish to search in
	 */
	public ClaimListController(User user, String mode) {
		if (mode.equals(Constants.APPROVER_MODE)) {
			claimList = new ClaimList(user, mode);
		}
	}
	
	/**
	 * ClaimController constructor that is used for searching for tags
	 * @param user The current user whose claims you wish to search
	 * @param mode The mode you are searching in (Tags Mode)
	 * @param selectedTags An arraylist of tags that you wish to search
	 */
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
	 * Save a new claim in persistent storage
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
	 * Deletes the claim indexed by claimId from storage
	 * @param claimId
	 */
	public void deleteClaim(int claimId){
		claimList.deleteClaim(claimId);
	}	
	
	/**
	 * Adds the claim to the list
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
	 * @return claim located at the given position in the list
	 */
	public Claim getClaim(int position) {
		return claimList.getClaim(position);
	}
	
	/**
	 * 
	 * @return List of claims
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
	
	/**
	 * Deletes all of the claims associated with the passed userId from persistent storage.
	 * @param userId Id of user whose claims you wish to delete
	 */
	public void deleteUserClaims(int userId) {
		claimList.deleteUserClaims(userId);
	}

	/**
	 * 
	 * Get's the max distance for the claim list between the given user's home location 
	 * and the claim with the farthest away first destination 
	 * @param user of the current claim
	 * @return the distance, in meters, between the user's home location
	 *  and the claim with the farthest away first destination 
	 */
	public int getMaxLocation(User user) {
		int maxLocation = 0;
		for (Claim tempClaim : claimList.getClaims()) {
			float[] results = {0,0,0};
			ArrayList<Destination> tempDestinations = tempClaim.getDestinations();
			if (tempDestinations.size() == 0) {
				continue;
			}
			else {
				Location l2 = tempDestinations.get(0).getLocation();
				Location l1 = user.getHomeLocation();
				Location.distanceBetween(l1.getLatitude(), l1.getLongitude(), 
						l2.getLatitude(), l2.getLongitude(), results);
				if ((int)results[0] > maxLocation) {
					maxLocation = (int) results[0];
				}
			}
		}
		return maxLocation;
	}
}

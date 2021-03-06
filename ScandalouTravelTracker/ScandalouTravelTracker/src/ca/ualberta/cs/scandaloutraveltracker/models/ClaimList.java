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
package ca.ualberta.cs.scandaloutraveltracker.models;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.mappers.ClaimListMapper;
import ca.ualberta.cs.scandaloutraveltracker.mappers.ClaimMapper;

/**
 * 	A claim is a statement containing a name, location(s),
 *  starting and ending date(s), a description, a status, 
 *  and a list of expenses, which includes a total of each currency spent.
 *  
 *  ClaimList is a Class that creates a list that holds Claims. 
 *  When doing tasks that involve the  ClaimList class it should 
 *  be done through the ClaimListController class.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimList extends SModel {
	public ArrayList<Claim> claims;
    	
	/**
	 * Constructor is set to private as the ClaimList class uses the 
	 * Singleton design pattern.
	 */
	public ClaimList(){
		ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
		claims = mapper.loadClaims();
	}
	
	/**
	 * Constructor that is used in getting a specific user's Claim List
	 * @param user User whose Claim List you want
	 */
	public ClaimList(User user) {
		ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
		claims = mapper.loadUserClaims(user);
	}
	
	/**
	 * ClaimController constructor that is used for when you are viewing
	 * the claims in Approver Mode.
	 * @param user The current User that is logged in
	 * @param mode The mode you wish to search in
	 */
	public ClaimList(User user, String mode) {
		if (mode.equals(Constants.APPROVER_MODE)) {
			ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
			claims = mapper.loadNotUserClaims(user);
		} 
	}
	
	/**
	 * ClaimController constructor that is used for searching for tags
	 * @param user The current user whose claims you wish to search
	 * @param mode The mode you are searching in (Tags Mode)
	 * @param selectedTags An arraylist of tags that you wish to search
	 */
	public ClaimList(User user, String mode, ArrayList<String> selectedTags) {
		if (mode.equals(Constants.TAG_MODE)) {
			ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
			claims = mapper.getFilteredClaims(user, selectedTags);
		}
	}
	
	/**
	 * Constructor used for testing purposes
	 * @param test
	 */
	public ClaimList(boolean test) {
		claims = new ArrayList<Claim>();
	}

	public ArrayList<Claim> getClaims() {
		return claims;
	}
	
	/**
	 * Get claim from the claim list that is associated with claimPos 
	 * which is the position of the particular claim inside a claimlist.
	 * @param claimPos
	 * @return Claim at claimPos
	 */
	public Claim getClaim(int claimPos) {
		return claims.get(claimPos);
	}
	
	/**
	 * Takes all the values that a Claim can have and creates the claim
	 * within the ClaimMapper (claim is saved) and returns the newly created
	 * claims ID.
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param destinations
	 * @param tagsList
	 * @param status
	 * @param canEdit
	 * @param expenses
	 * @return newly created claim ID
	 * @see ClaimMapper#createClaim(String, Date, Date, String, ArrayList, ArrayList, String, boolean, ArrayList)
	 */
	public int createClaim(Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, ArrayList<String> tagsList, String status,
			boolean canEdit, ArrayList<Expense> expenses, User user) throws UserInputException {

		if (startDate == null){
			throw new UserInputException("Please include a start date");
		} else if (endDate == null){
			throw new UserInputException("Please include an end date");
		} else if (endDate.before(startDate)){
			throw new UserInputException("The end date can't be before the start date");
		} else {	
			ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
			int newClaimId = mapper.createClaim(startDate, endDate, description, destinations, 
					tagsList, status, canEdit, expenses, user.getId());
			
			return newClaimId;		
		}
	}	
	
	/**
	 * Deletes the claim associated with the claimID. Does this
	 * through the mapper(see mapper for details).
	 * @param claimId
	 * @see ClaimMapper#deleteClaim(int)
	 */
	public void deleteClaim(int claimId){
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.deleteClaim(claimId);
	}
	
	/**
	 * Adds a claim 
	 * @param claim
	 */
	public void addClaim(Claim claim) {
		claims.add(claim);
		notifyViews();
	}
	
	/**
	 * Removes a claim associated with the claim ID. 
	 * @param claimId
	 */
	public void removeClaim(int claimId){
		if (new Claim(claimId).getCanEdit()) {
			int removePosition = -1;
			
			for (int i = 0; i < claims.size(); i++){
				if (claimId == claims.get(i).getId()){
					removePosition = i;
					break;
				}
			}
			
			if (removePosition > -1){
				claims.remove(removePosition);
			}
			notifyViews();
		}		
	}
	

	/**
	 * 
	 * @return Size of claims list
	 */
	public int getCount() {
		return claims.size();
	}
	
	/**
	 * 
	 * @return True if the list is empty and false if not
	 */
	public boolean isEmpty(){
		return claims.size()==0;
	}
	
	public void deleteUserClaims(int userId) {
		ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
		mapper.deleteUserClaims(userId, ClaimApplication.getContext());
	}
}
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

/**
 *  Class is a list that holds Claims. When doing tasks that involve the
 *  ClaimList class it should be done through the ClaimListController class.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimList extends SModel {
	protected ArrayList<Claim> claims;
    	
	/**
	 * Constructor is set to private as the ClaimList class uses the 
	 * Singleton design pattern.
	 */
	public ClaimList(){
		ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
		claims = mapper.loadClaims();
	}
	
	public ClaimList(User user) {
		ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
		claims = mapper.loadUserClaims(user);
	}
	
	public ClaimList(User user, String mode) {
		if (mode.equals(Constants.APPROVER_MODE)) {
			ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
			claims = mapper.loadNotUserClaims(user);
		} 
	}
	
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
	 * Get claim from the claim list that is associated with claimPos.
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
	 * through the mapper.
	 * @param claimId
	 * @see ClaimMapper#deleteClaim(int)
	 */
	public void deleteClaim(int claimId){
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.deleteClaim(claimId);
	}
	
	/**
	 * 
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
	
	/**
	 * Currently incomplete. Method allows you to search for
	 * a specific tag given the string.
	 * @param tag
	 * @return List of claims with associated tag
	 */
	public ArrayList<Claim> searchTag(String tag){
		
		return null;
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
	
}

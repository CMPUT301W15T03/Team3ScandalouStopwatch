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

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ClaimListMapper loads all the claims in the list using ClaimMapper.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimListMapper {

	private Context context;
	private User user;
	
	/**
	 * Constructor needs the current context to make a ClaimListMapper.
	 * @param context
	 */
	public ClaimListMapper(Context context){
		this.context = context;
	}	
	
	public ClaimListMapper(Context context, User user) {
		this.context = context;
		this.user = user;
	}

	/**
	 * 
	 * @return List of all the saved claims
	 */
	public ArrayList<Claim> loadClaims(){
		ArrayList<Claim> claims = new ArrayList<Claim>();
			
		SharedPreferences claimCounterFile = this.context.getSharedPreferences("claimCounter", 0);
		int mostRecentClaimId = claimCounterFile.getInt("claimCount", 0);	
		
		Claim claim;
		
		for (int i = 1; i <= mostRecentClaimId; i++){
			claim = new Claim(i);
			if (claim.getId() != -1){
				claims.add(claim);
			}
		}

		return claims;
		
	}	
	
	public ArrayList<Claim> loadUserClaims(User user) {
		ArrayList<Claim> claims = new ArrayList<Claim>();
		
		SharedPreferences claimCounterFile = this.context.getSharedPreferences("claimCounter", 0);
		int mostRecentClaimId = claimCounterFile.getInt("claimCount", 0);	
		
		Claim claim;
		User currentUser;
		int currentUserId;
		int actualUserId = user.getId();
		
		for (int i = 1; i <= mostRecentClaimId; i++){
			claim = new Claim(i);
			currentUser = claim.getUser();
			if (currentUser != null) {
				currentUserId = currentUser.getId();
				if (claim.getId() != -1 && currentUserId == actualUserId){
					claims.add(claim);
				}
			}
		}
		
		return claims;
	}
	
	public ArrayList<Claim> loadNotUserClaims(User user) {
		ArrayList<Claim> claims = new ArrayList<Claim>();
		
		SharedPreferences claimCounterFile = this.context.getSharedPreferences("claimCounter", 0);
		int mostRecentClaimId = claimCounterFile.getInt("claimCount", 0);	
		
		Claim claim;
		User currentUser;
		int currentUserId;
		int actualUserId = user.getId();
		
		for (int i = 1; i <= mostRecentClaimId; i++){
			claim = new Claim(i);
			currentUser = claim.getUser();
			if (currentUser != null) {
				currentUserId = currentUser.getId();
				
				// Checks to make sure the Claim does NOT belong to the Actual
				// user and that the claims have a submitted status
				if ( (claim.getId() != -1) &&
					 (actualUserId != currentUserId) && 
					 (claim.getStatus().equals(Constants.statusSubmitted))){
					claims.add(claim);	
				}
			}
		}
		
		return claims;
	}
	
	public ArrayList<String> getAllTags() {
		ArrayList<String> tags = new ArrayList<String>();
		ArrayList<String> currentTags;
		
		SharedPreferences claimCounterFile = this.context.getSharedPreferences("claimCounter", 0);
		int mostRecentClaimId = claimCounterFile.getInt("claimCount", 0);	
		
		Claim claim;
		User currentUser;
		int currentUserId;
		int actualUserId = user.getId();
		
		for (int i = 1; i <= mostRecentClaimId; i++){
			claim = new Claim(i);
			currentUser = claim.getUser();
			if (currentUser != null) {
				currentUserId = currentUser.getId();
				
				// Checks to make sure the Claim belongs to the actual user and
				// adds the tag to the CharSequence if it is not already included
				if ( (claim.getId() != -1) && (actualUserId == currentUserId) ){
					currentTags = claim.getTags();
					
					for (String tag : currentTags) {
						if (!tags.contains(tag) && !tag.equals("")) {
							tags.add(tag);
						}
					}
				}
			}
		}
		
		return tags;
	}
	
	public ArrayList<Claim> getFilteredClaims(User user, ArrayList<String> selectedTags) {
		ArrayList<Claim> claims = new ArrayList<Claim>();
		
		SharedPreferences claimCounterFile = this.context.getSharedPreferences("claimCounter", 0);
		int mostRecentClaimId = claimCounterFile.getInt("claimCount", 0);	
		
		Claim claim;
		User currentUser;
		int currentUserId;
		int actualUserId = user.getId();
		ArrayList<String> currentTags;
		
		for (int i = 1; i <= mostRecentClaimId; i++){
			claim = new Claim(i);
			currentUser = claim.getUser();
			// Check that the current user loaded is not null and that it
			// is actually the user that is logged in
			if (currentUser != null) {
				currentUserId = currentUser.getId();
				if (claim.getId() != -1 && currentUserId == actualUserId){
					currentTags = claim.getTags();
					
					// For all the tags in the current tags tag list, if any tag
					// is in the list, it will add it to the claims list
					for (String tag : currentTags) {
						if (selectedTags.contains(tag)) {
							claims.add(claim);
							break;
						}
					}
				}
			}
		}
		
		return claims;
	}
}

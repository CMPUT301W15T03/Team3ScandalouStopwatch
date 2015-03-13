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

/* ClaimListController.java Basic Info:
 *  When making edits to the Claim List model it should be done through
 *  this ClaimListController class.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.Collections;

public class ClaimListController {
	// TODO: Have ClaimListController load the list of claims using Claim Manager
	// Currently done through the ClaimListMapper
	
	private ClaimList claimList = null;
	
	// Constructor
	public ClaimListController() {
		claimList = ClaimList.getClaimList();
	}
	
	public void addView(ViewInterface view) {
		claimList.addView(view);
	}
	
	public void removeView(ViewInterface view) {
		claimList.removeView(view);
	}
	
	public void notifyViews() {
		claimList.notifyViews();
	}	
	
	public void addClaim(Claim claim) {
		claimList.addClaim(claim);
		Collections.sort(claimList.getClaims());
	}
	
	public void removeClaim(int claimPos) {	
		// Get the claim id
		int claimId = claimList.getClaim((int) claimPos).getId();
		
		// Delete the claim from the claim list
		claimList.deleteClaim((int) claimPos);			
		
		// Delete the claim from the shared preferences
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.deleteClaim(claimId);
	}
	
	public Claim getClaim(int position) {
		return claimList.getClaim(position);
	}
	
	public ClaimList getClaimList() {
		return claimList;
	}
}

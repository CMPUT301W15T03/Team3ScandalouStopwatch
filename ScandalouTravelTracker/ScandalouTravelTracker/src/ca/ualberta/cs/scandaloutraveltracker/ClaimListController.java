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
	
	private ClaimList claims = null;
	
	// Constructor
	public ClaimListController() {
		claims = ClaimList.getClaimList();
	}
	
	public void addView(ViewInterface view) {
		claims.addView(view);
	}
	
	public void removeView(ViewInterface view) {
		claims.removeView(view);
	}
	
	public void addClaim(Claim claim) {
		claims.addClaim(claim);
		Collections.sort(claims.getClaims());
	}
	
	public void removeClaim(Claim claim) {
		claims.deleteClaim(claim);
	}
	
	public void notifyViews() {
		claims.notifyViews();
	}
	
	public Claim getClaim() {
		return new Claim();
	}
	
	public ClaimList getClaimList() {
		return claims;
	}
}

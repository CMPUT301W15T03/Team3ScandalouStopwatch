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

import ca.ualberta.cs.scandaloutraveltracker.Constants;

/**
 *  This is the Approver class which represents a user that has the
 *  capabilities of a Approver, that is approve or return a claim.
 *  
 *  
 * @author Team3ScandalouStopwatch
 *
 */
public class Approver extends User {

	/**
	 * Constructor that just currently sets the name of the Approver.
	 * @param name Name of the Approver
	 */
	public Approver(int id) {
		super(id);
	}
	
	/**
	 * Constructor used in testing the approver model.
	 */
	public Approver() {
		
	}
	
	/**
	 * Approves a claim, changes boolean canedit to false, gets approver name,
	 * and adds a comment
	 * 
	 */	
	public void approveClaim(Claim claim, String comment) {
		if (claim.getStatus().equals(Constants.statusSubmitted)) {
			claim.setStatus(Constants.statusApproved);
			claim.setCanEdit(false);
			claim.setApproverName(this.getName());
			ArrayList<String> comments = claim.getApproverComments();
			comments.add(comment);
			claim.setApproverComment(comments);
		}
	}
	/**
	 * Returns a claim, changes boolean canedit to true, gets approver name,
	 * and adds a comment
	 * 
	 */	
	public void returnClaim(Claim claim, String comment) {
		if (claim.getStatus().equals(Constants.statusSubmitted)) {
			claim.setStatus(Constants.statusReturned);
			claim.setCanEdit(true);
			claim.setApproverName(this.getName());
			ArrayList<String> comments = claim.getApproverComments();
			comments.add(comment);
			claim.setApproverComment(comments);
		}
	}
}

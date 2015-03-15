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

/* ClaimList.java Basic Info:
 *  Class is a list that holds Claims. When doing tasks that involve the
 *  ClaimList class it should be done through the ClaimListController class.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ClaimList extends SModel {
	private static ClaimList claimList;
	protected static ArrayList<Claim> claims;
    	
	private ClaimList(){
		ClaimListMapper mapper = new ClaimListMapper(ClaimApplication.getContext());
		claims = mapper.loadClaims();
	}
	
	public static ClaimList getClaimList() {
		if (claimList == null) {
			claimList = new ClaimList();
		}
		
		return claimList;
	}
	
	public ArrayList<Claim> getClaims(){
		return claims;
	}
	
	public Claim getClaim(int claimPos) {
		return claims.get(claimPos);
	}
	
	/*
	public static void addClaim(Claim claim){
		claimList.add(string);
		if (claimList.size()>1){
			Collections.sort(claimList, new CustomComparator())
		}
		ClaimListgetClaims

	} */
	
	public int createClaim(String name, Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, ArrayList<String> tagsList, String status,
			boolean canEdit, ArrayList<Expense> expenses){

		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		int newClaimId = mapper.createClaim(name, startDate, endDate, description, destinations, 
				tagsList, status, canEdit, expenses);
		
		return newClaimId;
	}	
	
	public void deleteClaim(int claimId){
		ClaimMapper mapper = new ClaimMapper(ClaimApplication.getContext());
		mapper.deleteClaim(claimId);
	}
	
	public void addClaim(Claim claim) {
		claims.add(claim);
		notifyViews();
	}
	
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
	
	public ArrayList<Claim> searchTag(String tag){
		
		return null;
	}

	public int getCount() {
		return claims.size();
	}
	
	public static boolean isEmpty(){
		return claims.size()==0;
	}
	
	public void sortByStartDate(){
		
	}

	public static void addListener(int i) {
		// TODO Auto-generated method stub
		
	}
}

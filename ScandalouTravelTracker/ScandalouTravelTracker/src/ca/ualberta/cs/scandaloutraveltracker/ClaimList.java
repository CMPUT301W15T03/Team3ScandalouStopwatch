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

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ClaimList extends SModel {
	private static ClaimList claimList;
	protected static ArrayList<Claim> claims;
    	
	private ClaimList(){
		claims = new ArrayList<Claim>();
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
	
	public Claim getClaim(int position) {
		return claims.get(position);
	}
	
	/*
	public static void addClaim(Claim claim){
		claimList.add(string);
		if (claimList.size()>1){
			Collections.sort(claimList, new CustomComparator())
		}
		ClaimListgetClaims

	} */
	
	// Temporary add claim 
	public void addClaim(Claim claim) {
		claims.add(claim);
		notifyViews();
	}
	
	public void deleteClaim(Claim removeclaim){
		claims.remove(removeclaim);
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

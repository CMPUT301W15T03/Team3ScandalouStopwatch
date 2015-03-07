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
import java.util.Collection;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ClaimList extends SModel {
	protected static ArrayList<Claim> claimList;
    protected ArrayList<Listener> listeners;
    	
	public ClaimList(){
		claimList= new ArrayList<Claim>();
		listeners= new ArrayList<Listener>();
	}
	public Collection<Claim> getClaims(){
		return claimList;
	}
	public ArrayList<Claim> searchTag(String tag){
		
		return null;
	}
	public static void addClaim(Claim claim){
		/*claimList.add(string);
		if (claimList.size()>1){
			Collections.sort(claimList, new CustomComparator())
		}
		ClaimListgetClaims
		*/
	}
	public int getCount() {
		return claimList.size();
	}
	
	public void deleteClaim(Claim removeclaim){
		claimList.remove(removeclaim);
		notifyViews();
	}
	public static boolean isEmpty(){
		return claimList.size()==0;
	}
	public Claim getClaim(int position) {
		return claimList.get(position);
	}
	
	public static void addListener(int i) {
		// TODO Auto-generated method stub
		
	}
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
}

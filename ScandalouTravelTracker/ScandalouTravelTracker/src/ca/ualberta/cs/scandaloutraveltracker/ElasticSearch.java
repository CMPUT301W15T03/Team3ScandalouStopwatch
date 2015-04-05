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
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;

import com.google.gson.Gson;

public class ElasticSearch {

	/*private ClaimListAdapter claimListAdapter;
	private ExpenseListActivity expenseListAdapter;
	private ClaimController claimController;
	private ClaimListController claimListController;*/
	
	private static Context applicationContext;
	
	
	public ElasticSearch() {
		
	}
	public static void initializeContext(Context context) {
	applicationContext = context.getApplicationContext();
	}
	
	public static ArrayList<Claim> getSubmittedClaims(){
		return null;
		
		/*add httpget
		 * add try/ catch exceptions
		 */
		//http://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string for converting images to strings
	}
	
	public static Claim getClaim(String claimID){
		Gson gson = new Gson();
		return null;
		/*add httpget
		 * add try/ catch exceptions
		 */
	}

	public static void addClaim(Claim claim){
		Gson gson = new Gson();
		/*
		 * add try/ catch exceptions
		 */
	}
	
		
	public static void deleteClaim(String claimID){
		/*add httpget
		 * add try/ catch exceptions
		 */
	}
	 
	public static void updateClaim(Claim claim){
		/*add httpget
		 * add try/ catch exceptions
		 */
	}
	 
	public static void saveUser(){
		/*
		 * add try/ catch exceptions
		 */
	}
	public static void loadUser(String name) {
		/*add httpget
		 * add try/ catch exceptions
		 */
	}
	
	public static void loadClaims(String user) {
		/*add httpget
		 * add try/ catch exceptions
		 */
	}
	
	
}

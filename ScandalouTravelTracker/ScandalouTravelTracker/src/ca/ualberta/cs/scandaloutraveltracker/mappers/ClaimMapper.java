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

package ca.ualberta.cs.scandaloutraveltracker.mappers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import ca.ualberta.cs.scandaloutraveltracker.ConnectivityChangeReceiver;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Destination;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * The ClaimMapper helps us save all the data related to a claim. 
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimMapper {

	private Context context;
	OnlineMapper onlineMapper;
	
	/**
	 * ClaimMapper needs the context to run
	 * @param context
	 */
	public ClaimMapper(Context context){
		this.context = context;
		onlineMapper = new OnlineMapper(context);
	}
	
	/**
	 * When creating a new claim, this method gets called so it can
	 * save all the data of the newly created claim.
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param destinations
	 * @param tags
	 * @param status
	 * @param canEdit
	 * @param expenses
	 * @param user
	 * @return The new claim's ID
	 */
	public int createClaim(Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, ArrayList<String> tags, String status, 
			boolean canEdit, ArrayList<Expense> expenses, int userId) {
		
		int claimId = incrementClaimCounter();		
		
		saveClaimData(claimId, "id", claimId);
		saveClaimData(claimId, "startDate", startDate);
		saveClaimData(claimId, "endDate", endDate);
		saveClaimData(claimId, "description", description);
		saveClaimData(claimId, "destinations", destinations);
		saveClaimData(claimId, "tags", tags);
		saveClaimData(claimId, "status", status);
		saveClaimData(claimId, "canEdit", canEdit);		
		saveClaimData(claimId, "expenses", expenses);
		saveClaimData(claimId, "userId", userId);
		saveClaimData(claimId, "approverComments", new ArrayList<String>());
		
		saveOnline(claimId);
		
		return claimId;
	}
	
	/**
	 * Every time a claim is created we increment the claim counter.
	 * This helps us give a unique ID for each claim the user creates.
	 * @return new unique claim ID
	 */
	private int incrementClaimCounter(){

		int mostRecentId;
		int newId;
		SharedPreferences counterFile;
		Editor editor;		
		
		counterFile = this.context.getSharedPreferences("claimCounter", 0);
		mostRecentId = counterFile.getInt("claimCount", 0);
		newId = mostRecentId+1;
		editor = counterFile.edit();
		editor.putInt("claimCount", newId);
		editor.commit();
		
		return newId;
	}
	
	/**
	 * Updates the claim associated with claimId with all the other
	 * parameters passed.
	 * @param claimId
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param destinations
	 * @param canEdit
	 */
	public void updateClaim(int claimId, Date startDate, Date endDate, 
			String description, ArrayList<Destination> destinations, 
			boolean canEdit){
		
		saveClaimData(claimId, "startDate", startDate);
		saveClaimData(claimId, "endDate", endDate);
		saveClaimData(claimId, "description", description);
		saveClaimData(claimId, "destinations", destinations);
		saveClaimData(claimId, "canEdit", canEdit);
		
		saveOnline(claimId);
	}
	
	/**
	 * Updates the claim associated with claimId with all the other
	 * parameters passed.
	 * @param expenses
	 */
	public void updateExpenses(int claimId, ArrayList<Expense> expenses){
		saveClaimData(claimId, "expenses", expenses);
		//saveOnline(claimId); Leave this commented out until Abram confirms whether pushing a base64-encoded picture to the server is ok
	}	
	
	/**
	 * Updates the tags of the claim associated with claimId.
	 * @param claimId
	 * @param tags
	 */
	public void updateTags(int claimId, ArrayList<String> tags){
		saveClaimData(claimId, "tags", tags);
		saveOnline(claimId);
	}	
	
	/**
	 * Updates the claim status.
	 * @param claimId
	 * @param status
	 * @param canEdit
	 */
	public void changeClaimStatus(int claimId, String status, boolean canEdit){
		saveClaimData(claimId, "status", status);	
		saveClaimData(claimId, "canEdit", canEdit);
		saveOnline(claimId);
	}
	
	public void changeApproverName(int claimId, String approverName) {
		saveClaimData(claimId, "approverName", approverName);
		saveOnline(claimId);		
	}

	public void updateComments(int claimId, ArrayList<String> comments) {
		saveClaimData(claimId, "approverComments", comments);
		saveOnline(claimId);		
	}
	
	/**
	 * Saves the claim data, one field at a time. The field it saves
	 * is associated with the key. The warning is suppressed as we
	 * will always know the object type of the destination list, 
	 * tag list, and expense list.
	 * @param claimId
	 * @param key
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void saveClaimData(int claimId, String key, Object data){
		
		SharedPreferences claimFile = this.context.getSharedPreferences("claim"+Integer.toString(claimId), 0);
		Editor editor = claimFile.edit();
		Gson gson = new Gson();
		
		if (key.equals("id")){
			editor.putInt("id", (Integer)data);
		} else if (key.equals("description")){
			editor.putString(key, (String)data);			
		} else if (key.equals("startDate")){
	    	// CITATION http://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
	    	// 2015-03-12
	    	// MuhammadAamirALi's answer			
			String startDateJson = gson.toJson((Date)data);
		    editor.putString(key, startDateJson);
		} else if (key.equals("endDate")){
			String endDateJson = gson.toJson((Date)data);
		    editor.putString(key, endDateJson);
		} else if (key.equals("destinations")){
			String destinationsJson = gson.toJson((ArrayList<Destination>)data);
		    editor.putString(key, destinationsJson);
		} else if (key.equals("tags")){
			String tagsJson = gson.toJson((ArrayList<String>)data);
		    editor.putString(key, tagsJson);
		} else if (key.equals("status")){
			editor.putString(key, (String)data);
		} else if (key.equals("approverName")){
			editor.putString(key, (String)data);
		} else if (key.equals("approverComments")){
			String commentsJson = gson.toJson((ArrayList<String>)data);
		    editor.putString(key, commentsJson);
		} else if (key.equals("canEdit")){
			editor.putBoolean(key, (Boolean)data);
		} else if (key.equals("expenses")) {
			removeExpenseViews(claimId, key, data);
			String expensesJson = gson.toJson((ArrayList<Expense>)data);
			editor.putString(key, expensesJson);
		} else if (key.equals("userId")) {;
			editor.putInt("userId", (Integer)data);
		}
		
		editor.commit();
	}	
	
	/**
	 * Removes the expense views - these views were causing the application
	 * to hang whenever converting the expense list to json format. Here we 
	 * remove the view and in the activities where expenses need to be displayed
	 * we need a function to re-add that view to their list of views.
	 * @param claimId
	 * @param key
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	private void removeExpenseViews(int claimId, String key, Object data) {
		for (Expense expenses : (ArrayList<Expense>) data) {
			expenses.removeAllViews();
		}
	}

	/**
	 * Loads claim data one field at a time. The field loaded is associated
	 * with the key.
	 * @param claimId
	 * @param key
	 * @return Claim associated with claimId
	 */
	public Object loadClaimData(int claimId, String key){
		
		Object data = 0;
		SharedPreferences claimFile = this.context.getSharedPreferences("claim"+Integer.toString(claimId), 0);
	    Gson gson = new Gson();
		
	    if (key.equals("id")){
	    	data = claimFile.getInt(key, -1);	    
	    } else if (key.equals("description")){
		    data = claimFile.getString(key, "");
	    } else if (key.equals("startDate")){
	    	// CITATION http://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
	    	// 2015-03-12
	    	// MuhammadAamirALi's answer
		    String startDateJson = claimFile.getString(key, "");
		    data = gson.fromJson(startDateJson, Date.class);
	    } else if (key.equals("endDate")){
		    String startDateJson = claimFile.getString(key, "");
		    data = gson.fromJson(startDateJson, Date.class);	    	
	    } else if (key.equals("destinations")){
		    String destinationsJson = claimFile.getString(key, "");
		    // http://stackoverflow.com/questions/14981233/android-arraylist-of-custom-objects-save-to-sharedpreferences-serializable
		    // 2015-03-12
		    // SpyZip's answer
		    Type type = new TypeToken<ArrayList<Destination>>(){}.getType();
		    data = gson.fromJson(destinationsJson, type);		    
	    } else if (key.equals("tags")){
		    String tagsJson = claimFile.getString(key, "");
		    if (tagsJson == "") {
		    	data = new ArrayList<String>();
		    } else {
			    data = gson.fromJson(tagsJson, ArrayList.class);
		    }
	    } else if (key.equals("status")){
		    data = claimFile.getString(key, "");
	    } else if (key.equals("approverName")){
		    data = claimFile.getString(key, "");
	    } else if (key.equals("approverComments")){
	    	String commentsJson = claimFile.getString(key, "");
		    data = gson.fromJson(commentsJson, ArrayList.class);
	    } else if (key.equals("canEdit")){
	    	data = claimFile.getBoolean(key, false);
	    } else if (key.equals("expenses")) {
	    	String expensesJson = claimFile.getString(key, "");
	    	Type type = new TypeToken<ArrayList<Expense>>(){}.getType();
	    	data = gson.fromJson(expensesJson, type);
	    } else if (key.equals("userId")) {
		    data = claimFile.getInt("userId", 0);   		
	    }
	    
		return data;
	}
	
	/**
	 * Deletes the claim located at claimId. 
	 * @param claimId
	 */
	public void deleteClaim(int claimId){
		
		SharedPreferences claimFile;
		Editor editor;
		
		claimFile = this.context.getSharedPreferences("claim"+Integer.toString(claimId), 0);
		editor = claimFile.edit();
		editor.clear();
		editor.commit();
		
		deleteOnline(claimId);
	}
	
	private void saveOnline(int claimId){
		if (Constants.CONNECTIVITY_STATUS == true){
			onlineMapper.save("claim"+Integer.toString(claimId), new Claim(claimId));
		} else {
			onlineMapper.saveWhenConnected("claim"+Integer.toString(claimId), new Claim(claimId));
		}
	}
	
	private void deleteOnline(int claimId){
		if (Constants.CONNECTIVITY_STATUS == true){
			onlineMapper.delete("claim"+Integer.toString(claimId));
		} else {
			onlineMapper.deleteWhenConnected("claim"+Integer.toString(claimId));
		}
	}
	
}
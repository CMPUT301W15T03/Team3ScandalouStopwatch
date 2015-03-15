package ca.ualberta.cs.scandaloutraveltracker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ClaimMapper {

	private Context context;
	
	public ClaimMapper(Context context){
		this.context = context;
	}
	
	public int createClaim(String name, Date startDate, Date endDate, String description,
			ArrayList<Destination> destinations, ArrayList<String> tags, String status, 
			boolean canEdit, ArrayList<Expense> expenses) {
		
		int claimId = incrementClaimCounter();		
		
		saveClaimData(claimId, "id", claimId);
		saveClaimData(claimId, "name", name);
		saveClaimData(claimId, "startDate", startDate);
		saveClaimData(claimId, "endDate", endDate);
		saveClaimData(claimId, "description", description);
		saveClaimData(claimId, "destinations", destinations);
		saveClaimData(claimId, "tags", tags);
		saveClaimData(claimId, "status", status);
		saveClaimData(claimId, "canEdit", canEdit);		
		saveClaimData(claimId, "expenses", expenses);
		
		return claimId;
	}
	
	public int incrementClaimCounter(){

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
	
	public void updateClaim(int claimId, Date startDate, Date endDate, 
			String description, ArrayList<Destination> destinations, 
			boolean canEdit){
		
		saveClaimData(claimId, "startDate", startDate);
		saveClaimData(claimId, "endDate", endDate);
		saveClaimData(claimId, "description", description);
		saveClaimData(claimId, "destinations", destinations);
		saveClaimData(claimId, "canEdit", canEdit);
		
	}
	
	public void updateTags(int claimId, ArrayList<String> tags){
		
		saveClaimData(claimId, "tags", tags);
		
	}	
	
	public void submitClaim(int claimId, String status, boolean canEdit){
		
		saveClaimData(claimId, "status", status);	
		saveClaimData(claimId, "canEdit", canEdit);
	}	
	
	public void saveClaimData(int claimId, String key, Object data){
		
		SharedPreferences claimFile = this.context.getSharedPreferences("claim"+Integer.toString(claimId), 0);
		Editor editor = claimFile.edit();
		Gson gson = new Gson();		
		
		if (key.equals("id")){
			editor.putInt("id", (Integer)data);
		} else if (key.equals("name")){
			editor.putString(key, (String)data);
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
		} else if (key.equals("approverComment")){
			editor.putString(key, (String)data);
		} else if (key.equals("canEdit")){
			editor.putBoolean(key, (Boolean)data);
		} else if (key.equals("expenses")) {
			removeExpenseViews(claimId, key, data);
			String expensesJson = gson.toJson((ArrayList<Expense>)data);
			editor.putString(key, expensesJson);
		}
		
		editor.commit();	
	}	
	
	// Removes the expense views - these views were causing the application
	// to hang whenever converting the expense list to json format. Here we 
	// remove the view and in the activities where expenses need to be displayed
	// we need a function to re-add that view to their list of views.
	// (See ExpenseListActivity)
	private void removeExpenseViews(int claimId, String key, Object data) {
		for (Expense expenses : (ArrayList<Expense>) data) {
			expenses.removeAllViews();
		}
	}

	public Object loadClaimData(int claimId, String key){
		
		Object data = 0;
		SharedPreferences claimFile = this.context.getSharedPreferences("claim"+Integer.toString(claimId), 0);
	    Gson gson = new Gson();
		
	    if (key.equals("id")){
	    	data = claimFile.getInt(key, -1);	    
	    } else if (key.equals("name")){
	    	data = claimFile.getString(key, "");
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
		    data = gson.fromJson(tagsJson, ArrayList.class);
	    } else if (key.equals("status")){
		    data = claimFile.getString(key, "");
	    } else if (key.equals("approverName")){
		    data = claimFile.getString(key, "");
	    } else if (key.equals("approverComment")){
		    data = claimFile.getString(key, "");
	    } else if (key.equals("canEdit")){
	    	data = claimFile.getBoolean(key, false);
	    } else if (key.equals("expenses")) {
	    	String expensesJson = claimFile.getString(key, "");
	    	Type type = new TypeToken<ArrayList<Expense>>(){}.getType();
	    	data = gson.fromJson(expensesJson, type);
	    }
	    
		return data;
	}
	
	public void deleteClaim(int claimId){
		
		SharedPreferences claimFile;
		Editor editor;
		
		claimFile = this.context.getSharedPreferences("claim"+Integer.toString(claimId), 0);
		editor = claimFile.edit();
		editor.clear();
		editor.commit();
	}	
	
}

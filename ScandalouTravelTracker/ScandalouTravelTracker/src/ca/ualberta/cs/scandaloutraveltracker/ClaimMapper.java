package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ClaimMapper {

	private Context context;
	
	public ClaimMapper(Context context){
		this.context = context;
	}
	
	public Object loadClaimData(int claimId, String key){
		
		Object data = 0;
		SharedPreferences claimFile = context.getSharedPreferences("claim"+Integer.toString(claimId), 0);
	    Gson gson = new Gson();
		
	    if (key.equals("name")){
	    	data = claimFile.getString("name", "No name found.");
	    } else if (key.equals("description")){
		    data = claimFile.getString("description", "No description found.");
	    } else if (key.equals("startDate")){
		    String startDateJson = claimFile.getString("startDate", "");
		    data = gson.fromJson(startDateJson, Date.class);
	    } else if (key.equals("endDate")){
		    String startDateJson = claimFile.getString("endDate", "");
		    data = gson.fromJson(startDateJson, Date.class);	    	
	    } else if (key.equals("destinations")){
		    String destinationsJson = claimFile.getString("destinations", "");
		    data = gson.fromJson(destinationsJson, ArrayList.class);
	    }
	    
		return data;
	}
	
	public void saveNewClaimData(String key, Object data){
		
		int claimId = incrementClaimCounter();
		
		SharedPreferences claimFile = this.context.getSharedPreferences("claim"+Integer.toString(claimId), 0);
		Editor editor = claimFile.edit();
		Gson gson = new Gson();		
		
		if (key.equals("name")){
			editor.putString("name", (String)data);
		} else if (key.equals("description")){
			editor.putString("description", (String)data);			
		} else if (key.equals("startDate")){
			String startDateJson = gson.toJson((Date)data);
		    editor.putString("startDate", startDateJson);
		} else if (key.equals("endDate")){
			String endDateJson = gson.toJson((Date)data);
		    editor.putString("endDate", endDateJson);
		} else if (key.equals("destinations")){
			String destinationsJson = gson.toJson((ArrayList<Destination>)data);
		    editor.putString("destinations", destinationsJson);
		}
		
		editor.commit();	
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
	
}

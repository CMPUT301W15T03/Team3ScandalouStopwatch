package ca.ualberta.cs.scandaloutraveltracker.mappers;

import java.lang.reflect.Type;
import java.util.LinkedList;

import ca.ualberta.cs.scandaloutraveltracker.SyncQueueItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SyncQueueMapper {

	private Context context;
	
	/**
	 * ClaimMapper needs the context to run
	 * @param context
	 */
	public SyncQueueMapper(Context context){
		this.context = context;
	}
	
	public void updateSyncQueue(LinkedList syncQueue){
		
		SharedPreferences syncQueueFile = this.context.getSharedPreferences("syncQueue", 0);
		Editor editor = syncQueueFile.edit();
		Gson gson = new Gson();
		
		String syncQueueJson = gson.toJson(syncQueue);
		editor.putString("syncQueue", syncQueueJson);
		
		editor.commit();		
	}
	
	public LinkedList loadSyncQueue(){
		
		LinkedList syncQueue;
		SharedPreferences syncQueueFile = this.context.getSharedPreferences("syncQueue", 0);
		Gson gson = new Gson();
		
    	String syncQueueJson = syncQueueFile.getString("syncQueue", "");
	    // http://stackoverflow.com/questions/14981233/android-arraylist-of-custom-objects-save-to-sharedpreferences-serializable
	    // 2015-03-12
	    // SpyZip's answer
	    Type type = new TypeToken<LinkedList<SyncQueueItem>>(){}.getType();
	    syncQueue = gson.fromJson(syncQueueJson, type);
		
		return syncQueue;
	}
	
}

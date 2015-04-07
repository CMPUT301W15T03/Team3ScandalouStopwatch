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
import java.util.LinkedList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import ca.ualberta.cs.scandaloutraveltracker.SyncQueueItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Persistently saves a queue of changes that still need to be saved to the online server,
 * for when local changes were made without an internet connection
 * @author Team3ScandalouStopwatch
 *
 */
public class SyncQueueMapper {

	private Context context;
	
	/**
	 * ClaimMapper needs the context to run
	 * @param context
	 */
	public SyncQueueMapper(Context context){
		this.context = context;
	}
	
	/**
	 * Saves changes to the queue
	 * @param syncQueue that you wish to be the new syncQueue
	 */
	@SuppressWarnings("rawtypes")
	public void updateSyncQueue(LinkedList syncQueue){
		
		SharedPreferences syncQueueFile = this.context.getSharedPreferences("syncQueue", 0);
		Editor editor = syncQueueFile.edit();
		Gson gson = new Gson();
		
		String syncQueueJson = gson.toJson(syncQueue);
		editor.putString("syncQueue", syncQueueJson);
		
		editor.commit();		
	}
	
	/**
	 * Loads the claims that still need to be synced to the online server
	 * @return LinkedList of items tha need to be synced
	 */
	@SuppressWarnings("rawtypes")
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

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

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserMapper {

	private Context context;
	
	/**
	 * UserMapper needs the context to run
	 * @param context
	 */
	public UserMapper(Context context){
		this.context = context;
	}
	
	public int createUser(String name){
		
		int userId = incrementUserCounter();		
		
		saveUserData(userId, "id", userId);
		saveUserData(userId, "name", name);
		
		return userId;
	}
	
	/**
	 * Every time a user is created we increment the user counter.
	 * This helps us give a unique ID for each user that is created.
	 * @return new unique user ID
	 */
	private int incrementUserCounter(){

		int mostRecentId;
		int newId;
		SharedPreferences counterFile;
		Editor editor;		
		
		counterFile = this.context.getSharedPreferences("userCounter", 0);
		mostRecentId = counterFile.getInt("userCount", 0);
		newId = mostRecentId+1;
		editor = counterFile.edit();
		editor.putInt("userCount", newId);
		editor.commit();
		
		return newId;
	}	
	
	/**
	 * Saves the user data, one field at a time. The field it saves
	 * is associated with the key.
	 * @param userId
	 * @param key
	 * @param data
	 */
	public void saveUserData(int userId, String key, Object data){
		
		SharedPreferences userFile = this.context.getSharedPreferences("user"+Integer.toString(userId), 0);
		Editor editor = userFile.edit();		
		Gson gson = new Gson();
		
		if (key.equals("id")){
			editor.putInt("id", (Integer)data);
		} else if (key.equals("name")){
			editor.putString(key, (String)data);
		} else if (key.equals("location")) {
			String locationsJson = gson.toJson((Location)data);
		    editor.putString(key, locationsJson);
		}
		
		editor.commit();	
	}
	
	/**
	 * Loads user data one field at a time. The field loaded is associated
	 * with the key.
	 * @param userId
	 * @param key
	 * @return User associated with userId
	 */
	public Object loadUserData(int userId, String key){
		
		Object data = 0;
		SharedPreferences userFile = this.context.getSharedPreferences("user"+Integer.toString(userId), 0);
		Gson gson = new Gson();
		
	    if (key.equals("id")){
	    	data = userFile.getInt(key, -1);	    
	    } else if (key.equals("name")){
	    	data = userFile.getString(key, "");
	    } else if (key.equals("location")) {
	    	String locationsJson = userFile.getString(key, "");
		    Type type = new TypeToken<Location>(){}.getType();
		    data = gson.fromJson(locationsJson, type);		    
	    }
	    
		return data;
	}
	
	/**
	 * Deletes the user located at userId. 
	 * @param userId
	 */
	public void deleteUser(int userId){
		
		SharedPreferences userFile;
		Editor editor;
		
		userFile = this.context.getSharedPreferences("user"+Integer.toString(userId), 0);
		editor = userFile.edit();
		editor.clear();
		editor.commit();
	}	
	
}

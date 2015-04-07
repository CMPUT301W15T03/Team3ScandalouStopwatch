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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import ca.ualberta.cs.scandaloutraveltracker.models.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * UserMapper load/save a user
 * @author Team3ScandalouStopwatch
 *
 */
public class UserMapper {

	private Context context;
	OnlineMapper onlineMapper;
	
	/**
	 * UserMapper needs the context to run
	 * @param context
	 */
	public UserMapper(Context context){
		this.context = context;
		onlineMapper = new OnlineMapper(context);
	}
	
	/**
	 * Creates a user and saves the associated information
	 * @param name of user you wish to create
	 * @return userId of newly created user
	 */
	public int createUser(String name){
		
		int userId = incrementUserCounter();		
		
		saveUserData(userId, "id", userId);
		saveUserData(userId, "name", name);
		
		saveOnline(userId);
		
		return userId;
	}
	
	/**
	 * Updates the location of the given user
	 * @param userId of user whose location you want to update
	 * @param location new location you want to update user with
	 */
	public void updateLocation(int userId, Location location){	
		saveUserData(userId, "location", location);
		saveOnline(userId);
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
		
		SharedPreferences userFile = this.context.getSharedPreferences(getUserFileName(userId), 0);
		Editor editor = userFile.edit();		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());
		gsonBuilder.registerTypeAdapter(Location.class, new LocationSerializer());
		Gson gson = gsonBuilder.create(); 
		
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
		SharedPreferences userFile = this.context.getSharedPreferences(getUserFileName(userId), 0);
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());
		gsonBuilder.registerTypeAdapter(Location.class, new LocationSerializer());
		Gson gson = gsonBuilder.create(); 
		
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
		
		userFile = this.context.getSharedPreferences(getUserFileName(userId), 0);
		
		editor = userFile.edit();
		editor.clear();
		editor.commit();
		
		deleteOnline(userId);
	}
	
	private void saveOnline(int userId){
		onlineMapper.save(getUserFileName(userId), new User(userId));
	}
	
	private void deleteOnline(int userId){
		onlineMapper.delete(getUserFileName(userId));
	}
	
	public String getUserFileName(int userId){
		return "user"+Integer.toString(userId);
	}	
	
}

// http://stackoverflow.com/questions/13944346/runtimeexception-in-gson-parsing-json-failed-to-invoke-protected-java-lang-clas, 03/26/2015
/**
 * Helps to serialize a Location object so it can be saved in sharedpreferences
 * @author Team3ScandalouStopwatch
 *
 */
class LocationSerializer implements JsonSerializer<Location> {
	@Override
	public JsonElement serialize(Location location, Type arg1,
			JsonSerializationContext arg2) {
		
		JsonObject jo = new JsonObject();
		jo.addProperty("provider", location.getProvider());
		jo.addProperty("accuracy", location.getAccuracy());
		jo.addProperty("longitude", location.getLongitude());
		jo.addProperty("latitude", location.getLatitude());
		
		return jo;
	}

}

/**
 * Helps to deserialize a location that was serialized and stored in sharedpreferences
 * @author Team3ScandalouStopwatch
 *
 */
class LocationDeserializer implements JsonDeserializer<Location> {
	@Override
	public Location deserialize(JsonElement element, Type arg1,
			JsonDeserializationContext jdc) throws JsonParseException {
		
		JsonObject jo = element.getAsJsonObject();
		Location location = new Location(jo.getAsJsonPrimitive("provider").getAsString());
		location.setAccuracy(jo.getAsJsonPrimitive("accuracy").getAsFloat());
		location.setLatitude(jo.getAsJsonPrimitive("latitude").getAsDouble());
		location.setLongitude(jo.getAsJsonPrimitive("longitude").getAsDouble());
		
		return location;
	}

}
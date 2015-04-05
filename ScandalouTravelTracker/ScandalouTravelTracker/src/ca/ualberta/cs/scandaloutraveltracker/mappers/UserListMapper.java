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

import java.util.ArrayList;

import ca.ualberta.cs.scandaloutraveltracker.models.User;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * UserListMapper loads all the users in the list using UserMapper.
 * @author Team3ScandalouStopwatch
 *
 */
public class UserListMapper {

	private Context context;
	
	/**
	 * Constructor needs the current context to make a UserListMapper.
	 * @param context
	 */
	public UserListMapper(Context context){
		this.context = context;
	}	
	
	/**
	 * 
	 * @return List of all the saved users
	 */
	public ArrayList<User> loadUsers(){
		ArrayList<User> users = new ArrayList<User>();
			
		SharedPreferences userCounterFile = this.context.getSharedPreferences("userCounter", 0);
		int mostRecentUserId = userCounterFile.getInt("userCount", 0);	
		
		User user;
		
		for (int i = 1; i <= mostRecentUserId; i++){
			user = new User(i);
			if (user.getId() != -1){
				users.add(user);
			}
		}

		return users;
		
	}	
	
}

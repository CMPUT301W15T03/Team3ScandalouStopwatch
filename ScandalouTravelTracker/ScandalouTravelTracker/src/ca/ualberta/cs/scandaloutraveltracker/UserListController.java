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

import java.util.Collections;

/**
 *  When making edits to the User List model it should be done through
 *  this UserListController class.
 * @author Team3ScandalouStopwatch
 *
 */
public class UserListController {
	
	private UserList userList = null;
	
	/**
	 * Constructor just sets the userList to the only UserList
	 * that exists in the app (UserList is a Singleton).
	 */
	public UserListController() {
		userList = UserList.getUserList();
	}
	
	/**
	 * Adds a view that the UserList is on.
	 * @param view
	 */
	public void addView(ViewInterface view) {
		userList.addView(view);
	}

	/**
	 * 
	 * @param view
	 */
	public void removeView(ViewInterface view) {
		userList.removeView(view);
	}
	
	/**
	 * Notify all the views associated with the userList that data
	 * has changed.
	 */
	public void notifyViews() {
		userList.notifyViews();
	}	
	
	/**
	 * Creates a new user.
	 * @param name
	 * @return Newly created user's ID
	 */
	public int createUser(String name){
		
		int newUserId = userList.createUser(name);
		
		return newUserId;
		
	}
	
	/**
	 * Deletes the user from storage
	 * @param userId
	 */
	public void deleteUser(int userId){
		userList.deleteUser(userId);
	}	
	
	/**
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		userList.addUser(user);
		Collections.sort(userList.getUsers());
	}
	
	/**
	 * Removes the user from the list
	 * @param userId
	 */
	public void removeUser(int userId) {
		userList.removeUser((int) userId);
	}
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public User getUser(int position) {
		return userList.getUser(position);
	}
	
	/**
	 * 
	 * @return
	 */
	public UserList getUserList() {
		return userList;
	}
	
	/**
	 * 
	 * @return Size of UserList
	 */
	public int getCount() {
		return userList.getCount();
	}
	
}

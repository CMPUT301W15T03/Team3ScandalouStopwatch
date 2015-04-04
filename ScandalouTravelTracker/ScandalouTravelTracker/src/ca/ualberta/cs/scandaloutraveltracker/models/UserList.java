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

package ca.ualberta.cs.scandaloutraveltracker.models;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.scandaloutraveltracaker.mappers.UserListMapper;
import ca.ualberta.cs.scandaloutraveltracaker.mappers.UserMapper;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;

/**
 *  Class is a list that holds Users. When doing tasks that involve the
 *  UserList class it should be done through the UserListController class.
 * @author Team3ScandalouStopwatch
 *
 */
public class UserList extends SModel {
	private static UserList userList;
	protected static ArrayList<User> users;
    	
	/**
	 * Constructor is set to private as the UserList class uses the 
	 * Singleton design pattern.
	 */
	private UserList(){
		UserListMapper mapper = new UserListMapper(ClaimApplication.getContext());
		users = mapper.loadUsers();
	}
	
	/**
	 * Constructor used for testing purposes
	 * @param test
	 */
	public UserList(boolean test) {
		users = new ArrayList<User>();
	}
	
	/**
	 * Uses lazy initialization and won't create the userList until it
	 * is needed to be created by the app. Also uses the Singleton design
	 * pattern as it will return the only instance of userList in the
	 * entire app.
	 * @return List of users
	 */
	public static UserList getUserList() {
		if (userList == null) {
			userList = new UserList();
		}
		
		return userList;
	}
	
	/**
	 * 
	 * @return ArrayList of users
	 */
	public ArrayList<User> getUsers(){
		return users;
	}
	
	/**
	 * Get user from the user list that is associated with userPos.
	 * @param userPos
	 * @return User at userPos
	 */
	public User getUser(int userPos) {
		return users.get(userPos);
	}
	
	/**
	 * Takes all the values that a User can have and creates the user
	 * within the UserMapper (user is saved) and returns the newly created
	 * users ID.
	 * @param name
	 * @return newly created user ID
	 * @see UserMapper#createUser(String, Date, Date, String, ArrayList, ArrayList, String, boolean, ArrayList)
	 */
	public int createUser(String name){

		UserMapper mapper = new UserMapper(ClaimApplication.getContext());
		int newUserId = mapper.createUser(name);
		
		return newUserId;
	}	
	
	/**
	 * Deletes the user associated with the userID. Does this
	 * through the mapper.
	 * @param userId
	 * @see UserMapper#deleteUser(int)
	 */
	public void deleteUser(int userId){
		UserMapper mapper = new UserMapper(ClaimApplication.getContext());
		mapper.deleteUser(userId);
	}
	
	/**
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		users.add(user);
		notifyViews();
	}
	
	/**
	 * Removes a user associated with the user ID. 
	 * @param userId
	 */
	public void removeUser(int userId){
		
		int removePosition = -1;
		
		for (int i = 0; i < users.size(); i++){
			if (userId == users.get(i).getId()){
				removePosition = i;
				break;
			}
		}
		
		if (removePosition > -1){
			users.remove(removePosition);
		}
		notifyViews();		
	}
	


	/**
	 * 
	 * @return Size of users list
	 */
	public int getCount() {
		return users.size();
	}
	
	/**
	 * 
	 * @return True if the list is empty and false if not
	 */
	public static boolean isEmpty(){
		return users.size()==0;
	}
}

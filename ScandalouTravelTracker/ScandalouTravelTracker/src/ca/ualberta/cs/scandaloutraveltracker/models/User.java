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

import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.mappers.UserMapper;
import java.util.ArrayList;
import android.location.Location;

/**
 *  The superclass that all types of users will have to inherit.
 * @author Team3ScandalouStopwatch
 *
 */
public class User extends SModel implements Comparable<User> {

	private int id;
	private String name;
	// mode = 0 (Claimant View), mode = 1 (Approver View)
	private int mode;
	private Location homeLocation;
	/**
	 * creates a user using the mapper and providing the user id to get name and home location
	 */
	public User(int id) {
		UserMapper mapper = new UserMapper(ClaimApplication.getContext());		
		
		this.id = (Integer)mapper.loadUserData(id, "id");
		this.name = (String)mapper.loadUserData(id, "name");
		this.mode = 0;
		this.homeLocation = (Location)mapper.loadUserData(id, "location");
	}
	
	public User() {
		this.mode = 0;
	}
	/**
	 * @param current home location using the usermapper
	 */
	public void setCurrentLocation(Location location) {
		this.homeLocation = location;
		UserMapper mapper = new UserMapper(ClaimApplication.getContext());
		mapper.updateLocation(id, location);
	}
	/**
	 * @return home location for current user
	 */
	public Location getHomeLocation() {
		return this.homeLocation;
	}
	/**
	 * @return id for user
	 */
	public int getId() {
		return id;
	}
	/**
	 * 
	 * @param id for current user
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return user name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name for current user
	 */
	public void setName(String name) {
		this.name = name;
	}	
	/**
	 * @return user mode (approver or claimant) 
	 */
	public int getMode() {
		return mode;
	}
	/**
	 * 
	 * @param user mode (approver or claimant) for current user
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	@Override
	public int compareTo(User another) {
		return name.compareTo(another.getName());
	}

	/**
	 * @param destinations
	 * @return  int between 1-100 that corresponds with how close the first  destination in the claim is to the user's set home location
	 */
	public int getProgress(ArrayList<Destination> destinations) {
		ClaimListController clc = new ClaimListController(this);
		float[] results = { 0, 0, 0 };
		int maxDistance = clc.getMaxLocation(this);
		
		Location claimLocation;
		Location homeLocation;
		if (destinations.size() == 0) {
			claimLocation = null;
			return 0;
		} else {
			claimLocation = destinations.get(0).getLocation();
		}
		homeLocation = getHomeLocation();
		Location.distanceBetween(homeLocation.getLatitude(),
				homeLocation.getLongitude(), claimLocation.getLatitude(),
				claimLocation.getLongitude(), results);
		float distance = (((float) results[0]) / maxDistance) * 100;
		if (distance < 5) {
			return 5;
		} else
			return Math.round(distance);
	}	
	
}

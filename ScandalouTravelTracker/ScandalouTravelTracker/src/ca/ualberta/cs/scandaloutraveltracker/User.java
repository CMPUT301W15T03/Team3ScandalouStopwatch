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

	public User(int id) {
		UserMapper mapper = new UserMapper(ClaimApplication.getContext());		
		
		this.id = (Integer)mapper.loadUserData(id, "id");
		this.name = (String)mapper.loadUserData(id, "name");
		this.mode = 0;
		this.homeLocation = (Location)mapper.loadUserData(id, "location");
	}
	
	public void setCurrentLocation(Location location) {
		this.homeLocation = location;
		UserMapper mapper = new UserMapper(ClaimApplication.getContext());	
		mapper.saveUserData(id, "location", location);
	}
	
	public Location getHomeLocation() {
		return this.homeLocation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public int getMode() {
		return mode;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	@Override
	public int compareTo(User another) {
		return name.compareTo(another.getName());
	}	
	
}

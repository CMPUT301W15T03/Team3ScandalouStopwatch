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

import android.location.Location;

/**
 *  Class contains all the information that a Destination needs. A
 *  destination(s) is contained inside a Claim.
 * @author Team3ScandalouStopwatch
 *
 */
public class Destination extends SModel  {

	String name;
	String description;
	Location location;
	
	/**
	 * Default constructor that sets the Destination object properties.
	 * @param name
	 * @param description
	 */
	public Destination(String name, String description) {
		this.name = name;
		this.description = description;
		this.location = null;
	}

	/**
	 * 
	 * @return Name of Destination
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return Description of Destination
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @return Location of Destination
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Converts the destination and it's description into
	 * a string. In format: Destination name: description.
	 * @return Destination converted to a string
	 */
	public String toString(){
		String destination = this.name +": " + this.description;
		return destination;
	}
}

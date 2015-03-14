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

/* Destination.java Basic Info:
 *  Class contains all the information that a Destination needs. A
 *  destination(s) is contained inside a Claim.
 */

package ca.ualberta.cs.scandaloutraveltracker;
import android.view.View;
public class Destination extends SModel  {

	String name;
	String description;
	
	public Destination(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString(){
		String destination = this.name +": " + this.description;
		return destination;
	}

	
}

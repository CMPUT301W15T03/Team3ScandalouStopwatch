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

/**
 *  This is the Claimant class which represents a user that has the
 *  capabilities of a Claimant.
 *  
 *  This is the default for a user so functionality has mainly been
 *  implemented with considerations to a Claimant type user.
 * @author Team3ScandalouStopwatch
 *
 */
public class Claimant extends User {

	/**
	 * Sets the Claimant's name to the string passed.
	 * @param name
	 */
	public Claimant(int id) {
		super(id);
	}

}

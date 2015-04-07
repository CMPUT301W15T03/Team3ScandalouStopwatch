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
/**
 * Basic class that just stores two integers in an object. Used in
 * storing the starting character and ending character location in
 * a tag. 
 * 
 * @author Team3ScandalouStopwatch
 *
 */
public class IntegerPair {
	private int x;
	private int y;
	
	/**
	 * 
	 * sets x and y to their corresponding ints
	 */
	public IntegerPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}
	/**
	 * 
	 * @param set x
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}
	/**
	 * 
	 * @param set y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
}

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

import ca.ualberta.cs.scandaloutraveltracker.views.ViewInterface;

/**
 *  Part of MVC; all models must extend the SModel. Allows the model to
 *  keep track of its views and to add and delete them.
 * @author Team3ScandalouStopwatch
 *
 */
public class SModel {
	protected ArrayList<ViewInterface> views;
	/**
	 * 
	 * creates an arraylist made up of viewinterface
	 */
	public SModel() {
		views = new ArrayList<ViewInterface>();
	}
	/**
	 * adds a view to the arraylist 
	 * 
	 */
	public void addView(ViewInterface view) {
		views.add(view);
		
	}
	/**
	 * removes a view from the arraylist
	 * 
	 */
	public void removeView(ViewInterface view) {
		views.remove(view);
		
	}
	/**
	 * updates views
	 * 
	 */
	public void notifyViews() {
		for (ViewInterface view : views) {
			view.update();
		}
	}
}

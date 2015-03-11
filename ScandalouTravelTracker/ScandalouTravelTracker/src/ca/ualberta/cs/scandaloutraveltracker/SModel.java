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

/* SModel.java Basic Info:
 *  Part of MVC. All models must extend the SModel. Allows the model to
 *  keep track of its views and to add and delete them.
 */

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

public class SModel {
	protected ArrayList<ViewInterface> views;
	
	public SModel() {
		views = new ArrayList<ViewInterface>();
	}
	
	public void addView(ViewInterface view) {
		views.add(view);
		
	}
	public void removeView(ViewInterface view) {
		views.remove(view);
		
	}
	public void notifyViews() {
		for (ViewInterface view : views) {
			view.update();
		}
	}
}

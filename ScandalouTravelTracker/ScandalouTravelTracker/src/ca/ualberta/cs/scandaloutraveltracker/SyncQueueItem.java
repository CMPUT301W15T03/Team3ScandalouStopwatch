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
 * The SyncQueueItem class is a helper class for syncing the claim data
 * to the online elastic search servers. The class forms a queue for all
 * the data items that still need to be synced online. 
 * @author Team3ScandalouStopwatch
 *
 */
public class SyncQueueItem {
	
	private String key;
	private Object object;
	private int mode;
	
	/**
	 * Constructor
	 * @param key Key of the object
	 * @param object Data you wish to save
	 * @param mode Mode that you want to use
	 */
	public SyncQueueItem(String key, Object object, int mode){
		this.key = key;
		this.object = object;
		this.mode = mode;
	}

	/**
	 * Constructor
	 * @param key Key of the object
	 * @param mode Mode that you want to use
	 */
	public SyncQueueItem(String key, int mode){
		this.key = key;
		this.object = null;
		this.mode = mode;
	}		
	
	/**
	 * 
	 * @return Key associated with the SyncQueueItem
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @return Object associated with the SyncQueueItem
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * 
	 * @return Mode associated with the SyncQueueItem
	 */
	public int getMode() {
		return mode;
	}		
}

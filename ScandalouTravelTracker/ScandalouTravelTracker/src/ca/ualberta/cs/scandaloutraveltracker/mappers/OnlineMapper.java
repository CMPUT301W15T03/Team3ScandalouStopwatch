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

package ca.ualberta.cs.scandaloutraveltracker.mappers;

import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.SyncQueueItem;

import com.google.gson.Gson;

/**
 * Saves local data online. Code for making server requests was
 * inspired by https://github.com/joshua2ua/AndroidElasticSearch, 2015-04-03
 * @author Team3ScandalouStopwatch
 *
 */
public class OnlineMapper {

	@SuppressWarnings("unused")
	private Context context;
	private SyncQueueMapper syncQueueMapper;
	private static LinkedList<SyncQueueItem> syncQueue;
	private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t03/";
	private Gson gson = new Gson();
	private static int SAVE = 1;
	private static int DELETE = 2;
	
	/**
	 * Constructor
	 * @param context of application
	 */
	@SuppressWarnings("unchecked")
	public OnlineMapper(Context context){
		this.context = context;
		syncQueueMapper = new SyncQueueMapper(context);
		syncQueue = syncQueueMapper.loadSyncQueue();
		if (syncQueue == null) syncQueue = new LinkedList<SyncQueueItem>();
	}
	
	/**
	 * Gets the resource url for the online server
	 * @return
	 */
	public String getResourceURL(){
		return RESOURCE_URL;
	}

	/**
	 * Saves the data to the server
	 * @param filename of file you wish to save
	 * @param data that you wish to save
	 */
	public void save(String filename, Object data){
		if (Constants.CONNECTIVITY_STATUS == true){
			delete(filename);		
			
			Thread saveThread = new SaveThread(filename, data);
			saveThread.start();
		} else {
			saveWhenConnected(filename, data);
		}
	}
	
	/**
	 * Deletes file associated with filename from the server
	 * @param filename of file you wish to delete
	 */
	public void delete(String filename){
		if (Constants.CONNECTIVITY_STATUS == true){
			Thread deleteThread = new DeleteThread(filename);
			deleteThread.start();
		} else {
			deleteWhenConnected(filename);
		}
	}
	
	/**
	 * Syncs the local information with the information online
	 */
	public void sync(){
		while (!syncQueue.isEmpty()){
			SyncQueueItem item = syncQueue.remove();
			if (item.getMode() == SAVE){
				save(item.getKey(), item.getObject());
			} else if (item.getMode() == DELETE){
				delete(item.getKey());
			}
		}
		
		syncQueueMapper.updateSyncQueue(syncQueue);
	}
	
	/**
	 * Saves the local data to the online server when connected to the server
	 * @param objectName of object you wish to save
	 * @param object that you wish to save
	 */
	public void saveWhenConnected(String objectName, Object object){
		int i = 0;
		boolean old  = false;
		for (SyncQueueItem item : syncQueue){
			if (item.getKey().equals(objectName)){
				old = true;
				break;
			}
			i++;
		}
		if (old == true) syncQueue.remove(i);
		syncQueue.add(new SyncQueueItem(objectName, object, SAVE));
		
		syncQueueMapper.updateSyncQueue(syncQueue);
	}
	
	/**
	 * Deletes the object associated with objectName when connected to the server
	 * @param objectName of object you wish to delete
	 */
	public void deleteWhenConnected(String objectName){
		int i = 0;
		boolean old  = false;
		for (SyncQueueItem item : syncQueue){
			if (item.getKey().equals(objectName)){
				old = true;
				break;
			}
			i++;
		}
		if (old == true) syncQueue.remove(i);
		syncQueue.add(new SyncQueueItem(objectName, DELETE));
		
		syncQueueMapper.updateSyncQueue(syncQueue);
	}	
	
	/**
	 * Saves the current thread
	 * @author Team3ScandalouStopwatch
	 */
	class SaveThread extends Thread {

		private String filename;
		private Object data;		
		
		public SaveThread (String filename, Object data){
			this.filename = filename;
			this.data = data;
		}
		
		@Override
		public void run() {
			saveFile(filename, data);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * Deletes the current thread
	 * @author Team3ScandalouStopwatch
	 */
	class DeleteThread extends Thread {

		private String filename;
		@SuppressWarnings("unused")
		private Object data;
		
		public DeleteThread (String filename){
			this.filename = filename;
		}
		
		@Override
		public void run() {
			deleteFile(filename);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * Saves a file
	 * @param filename of file you wish to save
	 * @param data that you wish to save
	 */
	@SuppressWarnings("unused")
	public void saveFile(String filename, Object data) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost saveRequest = new HttpPost(RESOURCE_URL + filename);

			StringEntity stringEntity = new StringEntity(gson.toJson(data));
			saveRequest.setEntity(stringEntity);

			saveRequest.setHeader("Accept", "application/json");

			HttpResponse response = httpClient.execute(saveRequest);
			String status = response.getStatusLine().toString();
			
			//Log.d("Test1", status);

		} catch (Exception e) {
			//Log.d("Test1", "exception called");
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a file
	 * @param filename of file you wish to delete
	 * @param data that you wish to delete
	 */
	@SuppressWarnings("unused")
	public void deleteFile(String filename) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpDelete deleteRequest = new HttpDelete(RESOURCE_URL + filename);
			
			deleteRequest.setHeader("Accept", "application/json");

			HttpResponse response = httpClient.execute(deleteRequest);
			String status = response.getStatusLine().toString();
			
			//Log.d("SaveFile", status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

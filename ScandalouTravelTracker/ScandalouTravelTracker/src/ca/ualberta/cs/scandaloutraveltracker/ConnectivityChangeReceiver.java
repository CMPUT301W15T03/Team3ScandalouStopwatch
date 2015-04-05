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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ca.ualberta.cs.scandaloutraveltracker.mappers.OnlineMapper;

/**
 * The ConnectivityChangeReceiver helps the application determine if there is
 * or is not a connection to the Internet. This is used in helping the app
 * check if it can push data onto the online elastic search server.
 * @author Team3ScandalouStopwatch
 *
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {

	@Override
	/**
	 * When the application receives an online connection, this method will help
	 * push all the local data onto the online server. If there is no connection,
	 * the connectivity is just set to false and no data is pushed.
	 * @param context Application's current context
	 * @param intent Intent used in online connection
	 */
	public void onReceive(Context context, Intent intent) {
	
		if (isOnline(context) == true){
			Constants.CONNECTIVITY_STATUS = true;
			
			OnlineMapper onlineMapper = new OnlineMapper(ClaimApplication.getContext());
			onlineMapper.sync();
			
		} else {
			Constants.CONNECTIVITY_STATUS = false;
		}
		
	}
	
	// CITATION http://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app, 2015-04-02
	/**
	 * The isOnline method determines if the device is connected to the Internet
	 * and returns a boolean based on the connection.
	 * @param context Application's current context
	 * @return boolean value that is set to true if the app has online connectivity
	 */
	public boolean isOnline(Context context) {
		
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	
	}	   

}
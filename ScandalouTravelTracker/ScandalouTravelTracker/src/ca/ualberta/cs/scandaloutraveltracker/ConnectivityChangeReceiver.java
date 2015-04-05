package ca.ualberta.cs.scandaloutraveltracker;

import ca.ualberta.cs.scandaloutraveltracker.mappers.OnlineMapper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	
		if (isOnline(context) == true){
			Constants.CONNECTIVITY_STATUS = true;
			
			OnlineMapper onlineMapper = new OnlineMapper(ClaimApplication.getContext());
			onlineMapper.sync();
			
			//Log.d("MyConnectivity", "Connected");
		} else {
			Constants.CONNECTIVITY_STATUS = false;
			//Log.d("MyConnectivity", "Not connected");
		}
		
	}
	
	// CITATION http://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app, 2015-04-02
	public boolean isOnline(Context context) {
		
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	
	}	   

}
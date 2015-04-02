package ca.ualberta.cs.scandaloutraveltracker;

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
		}
		
		// Debugging
        if (Constants.CONNECTIVITY_STATUS == true){     
            Log.d("MyConnectivity", "Connected");
        } else {
        	Log.d("MyConnectivity", "Not connected");
		}
		
	}
	   
	public boolean isOnline(Context context) {
		
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    //should check null because in air plan mode it will be null
	    return (netInfo != null && netInfo.isConnected());
	
	}	   

}